package expression.generic;

import expression.exceptions.IncorrectExpressionException;
import expression.generic.bricks.TripleExpression;
import expression.generic.calculators.Converter;

import java.util.function.IntFunction;

public interface TripleParser<T> {
    TripleExpression<T> parse(String expression, Converter<T> converter) throws IncorrectExpressionException;
}
