package game;

public interface Position {

    boolean isValid(Move move);

    int getHeight();

    int getWidth();

    Cell getTurn();

}
