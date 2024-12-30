package game;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HumanPlayer implements Player{

    private final Scanner in;
    private final PrintStream out;

    public HumanPlayer(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    @Override
    public Move makeMove(Position position) {

        String entering = "Enter row and column separated by a space: ";
        out.print(entering);

        while (true) {

            String rowString, colString;

            try {

                rowString = in.next();
                colString = in.next();
                in.nextLine();

            } catch (IllegalStateException | NoSuchElementException e) {
                out.print("\nSorry, but you lost(\n\n");
                return new Move(-1, -1, position.getTurn());
            }

            try {

                int row = Integer.parseInt(rowString) - 1;
                int col = Integer.parseInt(colString) - 1;
                Move move = new Move(row, col, position.getTurn());

                if (position.isValid(move)) {
                    return move;
                }

                out.print("Incorrect move. " + entering);

            } catch (NumberFormatException e) {
                out.print("Your input contains not an integer or not enough data. " + entering);
            }

        }

    }

}
