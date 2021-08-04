import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;



public class Shell {
    private boolean verbose;

    public static void main(String[] args) throws IOException {

        BufferedReader stdin
                = new BufferedReader(new InputStreamReader(System.in));
        execute(stdin);
    }

    public boolean getVerbose() {
        return verbose;
    }

    private static void execute(BufferedReader stdin) throws IOException {
        boolean quit = false;
        boolean verbose;
        while (!quit) {
            System.out.print("smm> ");
            String input = stdin.readLine();
            if (input == null) {
                break;
            }

            input = input.toLowerCase();
            String[] tokens = input.trim().split("\\s+");

            //simple commands
            if (tokens.length == 1) {
                if (tokens[0].equals("quit") || tokens[0].equals("q")) {
                    quit = true;
                }
                if (tokens[0].equals("help") || tokens[0].equals("h")) {
                    System.out.println("Type 'quit' to quit.");
                }
            }
            //Two word commands
            else if (tokens.length == 2) {
                if (tokens[0].equals("verbose") || tokens[0].equals("v")) {
                    if (tokens[1].equals("off")) {
                        verbose = false;
                    } else if (tokens[1].equals("on")) {
                        verbose = true;
                    } else {
                        System.out.println("Error! Verbose has to be on or off.");
                    }
                }
            } else if (tokens.length == 7) {
                if (tokens[0].equals("m") || tokens[0].equals("mult") ) {
                    try {
                        int n = parseInt(tokens[1]);
                        int nZero = parseInt(tokens[2]);
                        int xOne = parseInt(tokens[3]);
                        int xTwo = parseInt(tokens[4]);
                        int yOne = parseInt(tokens[5]);
                        int yTwo = parseInt(tokens[6]);

                        Mult.mult(n, nZero, xOne, xTwo, yOne, yTwo);


                    } catch (NumberFormatException exception) {
                        throw new NumberFormatException("Error! Parameters wrong.");
                    }
                }
            }
        }
    }
}
