package game;

public class Game {

    private final Player player1, player2;
    private final boolean log;

    public Game(boolean log, Player player1, Player player2) {

        this.player1 = player1;
        this.player2 = player2;
        this.log = log;

    }

    public int play(Board board) {

        int res1, res2;
        while (true) {

            res1 = makeMove(board, player1, 1);
            if (res1 != -1) {
                return res1;
            }

            res2 = makeMove(board, player2, 2);
            if (res2 != -1) {
                return res2;
            }

        }
    }

    private int makeMove(Board board, Player player, int no) {

        Move move;
        Result result = Result.BONUS;

        while (result == Result.BONUS) {
            try {
                move = player.makeMove(board.getPosition());
            } catch (Exception e) {
                printLog("Got not a move\n");
                return 3 - no;
            }
            printLog(move);
            result = board.makeMove(move);
            if (result != Result.LOSE) printLog(board);
        }

        return switch (result) {
            case WIN -> no;
            case LOSE -> 3 - no;
            case DRAW -> 0;
            case UNKNOWN -> -1;
            default -> throw new AssertionError("impossible result");
        };

    }

    private void printLog(Object obj) {

        if (log) {
            System.out.println(obj);
        }

    }
}
