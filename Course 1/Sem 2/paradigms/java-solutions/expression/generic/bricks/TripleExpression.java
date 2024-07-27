package expression.generic.bricks;

import expression.generic.calculators.Evaluators;

public interface TripleExpression<T> {
    T evaluate(T x, T y, T z, Evaluators<T> evaluators);
}
