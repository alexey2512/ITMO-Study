package game;


import java.util.*;
import static java.lang.Math.*;

public class Championship {

    private final Player[] players;
    private int numberOfPlayers;
    private List<Integer> numbers;
    private final boolean log;


    public Championship(boolean log, Player[] players) {
        this.players = players;
        this.numberOfPlayers = players.length;
        this.numbers = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            numbers.add(i);
        }
        this.log = log;
    }

    public int play(Board board) {

        Collections.shuffle(numbers);

        int preOffGames = numberOfPlayers - (1 << (int) floor(log(numberOfPlayers) / log(2)));
        List<Integer> tempNumbers = new ArrayList<>();
        StringBuilder tree = new StringBuilder();

        treeLog("Pre-off:\n--------\n\n", tree);
        processLevel(tempNumbers, preOffGames, board, tree);
        for (int i = 2 * preOffGames; i < numberOfPlayers; i++) {
            tempNumbers.add(numbers.get(i));
            treeLog("( [" + (numbers.get(i) + 1) + "] )  ", tree);
        }
        treeLog("\n\n--------\n\n", tree);

        numbers = tempNumbers;
        numberOfPlayers = numbers.size();
        int level = 0;

        while (numberOfPlayers > 1) {
            tempNumbers = new ArrayList<>();
            level++;
            treeLog("LEVEL " + level + ":\n\n", tree);
            processLevel(tempNumbers, numberOfPlayers / 2, board, tree);
            treeLog("\n\n", tree);
            numbers = tempNumbers;
            numberOfPlayers = numbers.size();
        }

        System.out.println(tree);
        return numbers.get(0) + 1;
    }

    private void processLevel(List<Integer> tempNumbers, int count, Board board, StringBuilder tree) {

        for (int i = 0; i < count; i++) {

            Player player1 = players[numbers.get(2 * i)];
            Player player2 = players[numbers.get(2 * i + 1)];
            Game game;
            board.clear();
            int res = 0;

            while (res == 0) {

                if (log) {
                    System.out.println((numbers.get(2 * i) + 1) + " vs " + (numbers.get(2 * i + 1) + 1) + "\n");
                }

                game = new Game(log, player1, player2);
                board.clear();
                res = game.play(board);

                if (log && res == 0) {
                    System.out.println("Draw. One more game:\n");
                }

            }
            processGame(tempNumbers, 2 * i + res - 1, 2 * i + 2 - res, tree);

        }

    }

    private void treeLog(String data, StringBuilder tree) {

        if (log) {
            tree.append(data);
        }

    }

    private void processGame(List<Integer> tempNumbers, int winner, int loser, StringBuilder tree) {

        tempNumbers.add(numbers.get(winner));
        treeLog("( [" + (numbers.get(winner) + 1) + "] vs " + (numbers.get(loser) + 1) + " )  ", tree);

        if (log) {
            System.out.println("Player №" + (numbers.get(winner) + 1) + " won!\n");
        }

    }

}
