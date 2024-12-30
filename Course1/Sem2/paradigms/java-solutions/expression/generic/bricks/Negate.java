package expression.generic.bricks;

public class Negate<T> extends UnaryOperation<T> {

    public Negate(TripleExpression<T> expr) {
        super(expr);
    }

    @Override
    protected int getIndexOfOperation() {
        return 0;
    }

}
