package expression;

public class Const extends ExpressionElement {

    private final int value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const) {
            return value == ((Const) obj).evaluate(0);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value;
    }

}
