package expression.generic.bricks;

public class Add<T> extends BinaryOperation<T> {

    public Add(TripleExpression<T> left, TripleExpression<T> right) {
        super(left, right);
    }

    @Override
    protected int getIndexOfOperation() {
        return 0;
    }

}
