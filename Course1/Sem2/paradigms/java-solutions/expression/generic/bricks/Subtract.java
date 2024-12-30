package expression.generic.bricks;

public class Subtract<T> extends BinaryOperation<T> {

    public Subtract(TripleExpression<T> left, TripleExpression<T> right) {
        super(left, right);
    }

    @Override
    protected int getIndexOfOperation() {
        return 1;
    }

}
