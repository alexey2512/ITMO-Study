package game;

import java.util.Map;
import static java.lang.Math.max;

public class MNKBoard implements Board, Position {

    private final int height, width, maxLenOfSequence;
    private int empty;
    private Cell turn;
    private final Cell[][] field;
    private static final Map<Cell, Character> symbols = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '•'
    );

    public MNKBoard(int height, int width, int maxLenOfSequence) {

        if (height <= 0 || width <= 0 || maxLenOfSequence <= 0) {
            throw new IllegalArgumentException("M, N, and K must be positive");
        } else if (maxLenOfSequence > height || maxLenOfSequence > width) {
            throw new IllegalArgumentException("K must be no more than M and N");
        }

        this.height = height;
        this.width = width;
        this.maxLenOfSequence = maxLenOfSequence;
        this.empty = height * width;
        this.turn = Cell.X;
        this.field = new Cell[height][];
        for (int i = 0; i < height; i++) {
            field[i] = new Cell[width];
            for (int j = 0; j < width; j++) {
                field[i][j] = Cell.E;
            }
        }

    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Result makeMove(Move move) {

        if (!isValid(move)) {
            return Result.LOSE;
        }

        final int row = move.getRow(), col = move.getCol();
        field[row][col] = turn;
        empty--;

        if (comparingMaxSequence(row, col, maxLenOfSequence)) {
            return Result.WIN;
        }

        if (comparingMaxSequence(row, col, 4) && empty != 0) {
            return Result.BONUS;
        } else {
            turn = (turn == Cell.X) ? Cell.O : Cell.X;
            return empty == 0 ? Result.DRAW : Result.UNKNOWN;
        }

    }

    private boolean isInField(int row, int col) {

        return (0 <= row && row < height &&
                0 <= col && col < width);

    }

    @Override
    public void clear() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = Cell.E;
            }
        }
        turn = Cell.X;
        this.empty = height * width;

    }

    private int maxLenOfResultSequence(int row, int col, int verticalMove, int horizontalMove) {

        int count = 0;
        int r = row + verticalMove;
        int c = col + horizontalMove;

        while (isInField(r, c) && field[r][c] == turn) {
            count++;
            r += verticalMove;
            c += horizontalMove;
        }

        r = row - verticalMove;
        c = col - horizontalMove;

        while (isInField(r, c) && field[r][c] == turn) {
            count++;
            r -= verticalMove;
            c -= horizontalMove;
        }

        return count + 1;

    }

    private boolean comparingMaxSequence(int row, int col, int length) {

        int[][] help = new int[][]{new int[]{1, 0}, new int[]{0, 1}, new int[]{1, 1}, new int[]{1, -1}};
        boolean ans = false;
        for (int i = 0; i < 4; i++) {
            ans = ans || (maxLenOfResultSequence(row, col, help[i][0], help[i][1]) >= length);
        }
        return ans;

    }

    @Override
    public boolean isValid(Move move) {

        int row = move.getRow(), col = move.getCol();

        return (isInField(row, col) &&
                field[row][col] == Cell.E &&
                move.getValue() == turn);

    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        int numLen = max(Integer.toString(height + 1).length(),
                Integer.toString(width + 1).length()) + 3;

        sb.append(" ".repeat(numLen));
        for (int j = 0; j < width; j++) {
            sb.append(j + 1).append(" ".repeat(numLen - Integer.toString(j + 1).length()));
        }
        sb.append("\n\n");
        for (int i = 0; i < height; i++) {
            for (int j = -1; j < width; j++) {
                if (j == -1) {
                    sb.append(i + 1).append(" ".repeat(numLen - Integer.toString(i + 1).length()));
                } else {
                    sb.append(symbols.get(field[i][j])).append(" ".repeat(numLen - 1));
                }
            }
            sb.append("\n\n");
        }

        return sb.toString();

    }

}
