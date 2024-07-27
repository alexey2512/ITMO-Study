package expression.generic.bricks;

public class Divide<T> extends BinaryOperation<T> {

    public Divide(TripleExpression<T> left, TripleExpression<T> right) {
        super(left, right);
    }

    @Override
    protected int getIndexOfOperation() {
        return 3;
    }

}
