package expression.generic.bricks;

import expression.generic.calculators.Evaluators;

public abstract class BinaryOperation<T> implements TripleExpression<T> {

    private final TripleExpression<T> left;
    private final TripleExpression<T> right;

    public BinaryOperation(TripleExpression<T> left, TripleExpression<T> right) {
        this.left = left;
        this.right = right;
    }

    protected abstract int getIndexOfOperation();

    @Override
    public T evaluate(T x, T y, T z, Evaluators<T> evaluators) {
        return evaluators.binaryOperations().get(getIndexOfOperation()).apply(
                left.evaluate(x, y, z, evaluators),
                right.evaluate(x, y, z, evaluators));
    }

}
