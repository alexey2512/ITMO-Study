package expression.generic.calculators;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

public record Evaluators<T>(
        List<BinaryOperator<T>> binaryOperations,
        List<UnaryOperator<T>> unaryOperations,
        IntFunction<T> casterFromInt,
        Converter<T> converter
) {}
