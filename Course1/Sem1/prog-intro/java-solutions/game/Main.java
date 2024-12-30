package game;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        try {

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter M, N, K: ");
            int m = sc.nextInt();
            int n = sc.nextInt();
            int k = sc.nextInt();
            Board board = new MNKBoard(m, n, k);
            System.out.println();
            System.out.print("Enter number of players: ");
            int num = sc.nextInt();
            if (num < 2) {
                throw new InputMismatchException("number of players must be integer more 1");
            }
            System.out.println();

            Player[] players = new Player[num];
            for (int i = 0; i < num; i++) {
                players[i] = new RandomPlayer();
            }
            players[0] = new HumanPlayer(System.in, System.out);
            Championship championship = new Championship(true, players);
            int res = championship.play(board);
            System.out.println("Player №" + res + " won!");

            sc.close();

        } catch (IllegalStateException e) {
            System.err.println("Input stream is closed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.err.println("Input data is exhausted: " + e.getMessage());
        } catch (AssertionError | NullPointerException e) {
            System.err.println("Something wrong: " + e.getMessage());
        }

    }
}