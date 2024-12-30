package expression.generic;

import expression.exceptions.IncorrectExpressionException;
import expression.generic.bricks.TripleExpression;
import expression.generic.calculators.*;
import java.math.BigInteger;
import static expression.generic.calculators.TypedOperations.*;

public class GenericTabulator implements Tabulator {

    private static <T> Object[][][] fill(String expression, int x1, int x2, int y1, int y2, int z1, int z2, Evaluators<T> evaluators) throws IncorrectExpressionException {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        TripleExpression<T> building = new ExpressionParser<T>().parse(expression, evaluators.converter());
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        T value = building.evaluate(evaluators.casterFromInt().apply(x1 + i),
                                evaluators.casterFromInt().apply(y1 + j),
                                evaluators.casterFromInt().apply(z1 + k), evaluators);
                        result[i][j][k] = value;
                    } catch (Exception e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws IncorrectExpressionException {
        return switch (mode) {
            case "i" -> {
                Evaluators<Integer> evaluatorsInteger = new Evaluators<>(integerBinaryOperations,
                        integerUnaryOperations, intToInteger, stringToInt);
                yield fill(expression, x1, x2, y1, y2, z1, z2, evaluatorsInteger);
            }
            case "d" -> {
                Evaluators<Double> evaluatorsDouble = new Evaluators<>(doubleBinaryOperations,
                        doubleUnaryOperations, intToDouble, stringToDouble);
                yield fill(expression, x1, x2, y1, y2, z1, z2, evaluatorsDouble);
            }
            case "bi" -> {
                Evaluators<BigInteger> evaluatorsBigInteger = new Evaluators<>(bigIntegerBinaryOperations,
                        bigIntegerUnaryOperations, intToBigInteger, stringToBigInt);
                yield fill(expression, x1, x2, y1, y2, z1, z2, evaluatorsBigInteger);
            }
            case "u" -> {
                Evaluators<Integer> evaluatorsIntegerNC = new Evaluators<>(integerBinaryOperationsNC,
                        integerUnaryOperationsNC, intToInteger, stringToInt);
                yield fill(expression, x1, x2, y1, y2, z1, z2, evaluatorsIntegerNC);
            }
            case "b" -> {
                Evaluators<Byte> evaluatorsIntegerNC = new Evaluators<>(byteBinaryOperations,
                        byteUnaryOperation, intToByte, stringToByte);
                yield fill(expression, x1, x2, y1, y2, z1, z2, evaluatorsIntegerNC);
            }
            case "bool" -> {
                Evaluators<Boolean> evaluatorsBoolean = new Evaluators<>(booleanBinaryOperations,
                        booleanUnaryOperations, intToBoolean, stringToBoolean);
                yield fill(expression, x1, x2, y1, y2, z1, z2, evaluatorsBoolean);
            }
            default -> throw new AssertionError("unknown mode: " + mode);
        };
    }
}
