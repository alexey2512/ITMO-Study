package expression.generic.bricks;

import expression.generic.calculators.Evaluators;

public class Variable<T> implements TripleExpression<T> {

    private final char name;

    public Variable(char name) {
        this.name = name;
        if (name < 'x' || name > 'z') {
            throw new AssertionError("invalid variable name '" + name + "'");
        }
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluators<T> evaluators) {
        return switch (name) {
            case 'x' -> x;
            case 'y' -> y;
            case 'z' -> z;
            default -> null;
        };
    }
}
