package expression.generic.bricks;

import expression.generic.calculators.Evaluators;

public class Const<T> implements TripleExpression<T> {

    private final T value;

    public Const(T value) {
        this.value = value;
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluators<T> evaluators) {
        return value;
    }
}
