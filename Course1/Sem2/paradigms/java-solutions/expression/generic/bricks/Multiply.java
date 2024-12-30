package expression.generic.bricks;

public class Multiply<T> extends BinaryOperation<T> {

    public Multiply(TripleExpression<T> left, TripleExpression<T> right) {
        super(left, right);
    }

    @Override
    protected int getIndexOfOperation() {
        return 2;
    }

}
