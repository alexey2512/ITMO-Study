package game;

import java.util.Random;

public class RandomPlayer implements Player{

    private final Random random = new Random();

    @Override
    public Move makeMove(Position position) {

        while (true) {

            int row = random.nextInt(position.getHeight());
            int col = random.nextInt(position.getWidth());
            Move move = new Move(row, col, position.getTurn());

            if (position.isValid(move)) {
                return move;
            }

        }

    }

}
