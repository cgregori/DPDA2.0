import java.util.*;

public class Main {

    static String state = "p"; // Possible values: p, q, qa, qb, q$
    static Stack<Character> pda_Stack = new Stack();
    static Stack<Character> unread_Input = new Stack();
    static int step = 0;

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string to be accepted by L={a^nb^n | n >= 0}: ");
        System.out.println("Hint: ab$ | aabb$ | aaabbb$ | aaaabbbb$ | ...");
        String input = scanner.nextLine();
        System.out.println("NOTE: Java prints stacks backwards. Top of stack is at the right");
        return input;
    }

    private static void display(String rules) {
        System.out.printf("%10s", step + "\t\t\t");
        System.out.printf("%10s", state + "\t\t\t");
        System.out.printf("%10s", unread_Input + "\t\t\t\t");
        System.out.printf("%10s", pda_Stack + "\t\t\t\t");
        System.out.printf("%10s %n", rules);
        step++;
    }

    private static void transRule1() {
        // (p, e, e) -> (q, S)
        pda_Stack.push('S');
        state = "q";
        display("{1} (p, e, e) -> (q, S)");
    }

    private static void transRule2() {
        // (q, a, e) -> (qa, e)
        unread_Input.pop();
        state = "qa";
        display("{2} (p, e, e) -> (q, S)");
    }

    private static void transRule3() {
        // (qa, a, e) -> (q, e)
        pda_Stack.pop();
        state = "q";
        display("{3} (qa, a, e) -> (q, e)");
    }

    private static void transRule4() {
        // (q, b, e) -> (qb, e)
        unread_Input.pop();
        state = "qb";
        display("{4} (q, b, e) -> (qb, e)");
    }

    private static void transRule5() {
        // (qb, e, b) -> (q, e)
        pda_Stack.pop();
        state = "q";
        display("{5} (qb, e, b) -> (q, e)");
    }

    private static void transRule6() {
        // (q, $, e) -> (q$, e)
        unread_Input.pop();
        state = "q$";
        display("{6} (q, $, e) -> (q$, e)");
    }

    private static void transRule7() {
        // (qa, e, S) -> (qa, aSb)
        pda_Stack.pop();
        pda_Stack.push('b');
        pda_Stack.push('S');
        pda_Stack.push('a');
        display("{7} (qa, e, S) -> (qa, aSb) ||| S -> aSb");
    }

    private static void transRule8() {
        // (qb, e, S) -> (qb, e)
        pda_Stack.pop();
        display("{8} (qb, e, S) -> (qb, e) ||| S -> e");
    }


    public static void main(String[] args) {
        // Receive string for L={a^nb^n}
        String input = getInput();

        // Push input into unread_Input stack starting from back
        for(int i = input.length() - 1; i >= 0 ; i--)
            unread_Input.push(input.charAt(i));

        // Set headers for table
        System.out.println("Step\t\tState\t\t\tUnread Input\t\t\tStack\t\t\t\tΔ Rule\t\tR Rule");
        display("");

        do {
            if (state.equals("p")) {
                transRule1();
            } else if (state.equals("q")) {
                try {
                    if (unread_Input.peek() == 'a') {
                        transRule2();
                    } else if (unread_Input.peek() == 'b') {
                        transRule4();
                    } else if (unread_Input.peek() == '$') {
                        transRule6(); // State becomes q$
                    }
                } catch (EmptyStackException e) {
                }
            } else if (state.equals("qa")) {
                if (pda_Stack.peek() == 'a') {
                    transRule3();
                } else if (pda_Stack.peek() == 'S') {
                    transRule7();
                }
            } else if (state.equals("qb")) {
                if (pda_Stack.peek() == 'b') {
                    transRule5();
                } else if (pda_Stack.peek() == 'S') {
                    transRule8();
                }
            }
        } while (!state.equals("q$")) ;
    }
}
