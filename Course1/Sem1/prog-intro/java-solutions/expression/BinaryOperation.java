package expression;

import java.util.Objects;

public abstract class BinaryOperation extends ExpressionElement {

    private final ExpressionElement left;
    private final ExpressionElement right;

    public BinaryOperation(ExpressionElement left, ExpressionElement right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + getOperator() + " " + right.toString() + ")";
    }

    protected abstract char getOperator();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation) {
            return getOperator() == ((BinaryOperation) obj).getOperator() &&
                    left.equals(((BinaryOperation) obj).getLeft()) &&
                    right.equals(((BinaryOperation) obj).getRight());
        }
        return false;
    }

    public ExpressionElement getLeft() {
        return left;
    }

    public ExpressionElement getRight() {
        return right;
    }

    protected abstract int calculate(int a, int b);

    @Override
    public int evaluate(int x) {
        return calculate(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, getOperator());
    }

}
