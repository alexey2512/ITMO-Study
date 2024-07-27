package expression.generic.bricks;

import expression.generic.calculators.Evaluators;

public abstract class UnaryOperation<T> implements TripleExpression<T> {

    private final TripleExpression<T> expr;

    protected UnaryOperation(TripleExpression<T> expr) {
        this.expr = expr;
    }

    protected abstract int getIndexOfOperation();

    @Override
    public T evaluate(T x, T y, T z, Evaluators<T> evaluators) {
        return evaluators.unaryOperations().get(getIndexOfOperation()).apply(
                expr.evaluate(x, y, z, evaluators));
    }
}
