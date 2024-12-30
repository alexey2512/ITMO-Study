package game;

public class SequentialPlayer implements Player{

    @Override
    public Move makeMove(Position position) {

        for (int r = 0; r < position.getHeight(); r++) {
            for (int c = 0; c < position.getWidth(); c++) {
                Move move = new Move(r, c, position.getTurn());
                if (position.isValid(move)) {
                    return move;
                }
            }
        }

        throw new AssertionError("Empty cells not found");

    }

}
