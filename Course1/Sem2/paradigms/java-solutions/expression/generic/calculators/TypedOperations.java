package expression.generic.calculators;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

public class TypedOperations {

    public static final List<BinaryOperator<Integer>> integerBinaryOperations = List.of(

            (Integer a, Integer b) -> { // add
                if ((a >= 0 && b >= 0 && a > Integer.MAX_VALUE - b) ||
                        (a < 0 && b < 0 && a < Integer.MIN_VALUE - b)) {
                    throw new OverflowException("overflow in add: " + a + " + " + b);
                }
                return a + b;
            },

            (Integer a, Integer b) -> { // subtract
                if ((a >= 0 && b <= 0 && a > Integer.MAX_VALUE + b) ||
                        (a < 0 && b > 0 && a < Integer.MIN_VALUE + b)){
                    throw new OverflowException("overflow in subtract: " + a + " - " + b);
                }
                return a - b;
            },

            (Integer a, Integer b) -> { // multiply
                if (a == Integer.MIN_VALUE && b != 1 && b != 0 || b == Integer.MIN_VALUE && a != 1 && a != 0) {
                    throw new OverflowException("overflow in multiply: " + a + " * " + b);
                }
                if (a > 0 && b > 0 && a > Integer.MAX_VALUE / b || a > 0 && b < 0 && b < Integer.MIN_VALUE / a ||
                        a < 0 && b > 0 && a < Integer.MIN_VALUE / b || a < 0 && b < 0 && (-a) > Integer.MAX_VALUE / (-b)) {
                    throw new OverflowException("overflow in multiply: " + a + " * " + b);
                }
                return a * b;
            },

            (Integer a, Integer b) -> { // divide
                if (b == 0) {
                    throw new DivisionByZeroException("divide by zero: " + a + " / " + 0);
                }
                if (a == Integer.MIN_VALUE && b == -1) {
                    throw new OverflowException("overflow in divide: " + a + " / " + b);
                }
                return a / b;
            }

    );

    public static final List<UnaryOperator<Integer>> integerUnaryOperations = List.of(
            (Integer a) -> {
                if (a == Integer.MIN_VALUE) {
                    throw new OverflowException("overflow in negate: " + "-(" + a + ")");
                }
                return -a;
            }
    );

    public static final List<BinaryOperator<Double>> doubleBinaryOperations = List.of(
            Double::sum,
            (Double a, Double b) -> a - b,
            (Double a, Double b) -> a * b,
            (Double a, Double b) -> a / b
    );

    public static final List<UnaryOperator<Double>> doubleUnaryOperations = List.of(
            (Double a) -> -a
    );

    public static final List<BinaryOperator<BigInteger>> bigIntegerBinaryOperations = List.of(
            BigInteger::add,
            BigInteger::subtract,
            BigInteger::multiply,
            BigInteger::divide
    );

    public static final List<UnaryOperator<BigInteger>> bigIntegerUnaryOperations = List.of(
            BigInteger::negate
    );

    public static final List<BinaryOperator<Integer>> integerBinaryOperationsNC = List.of(
            Integer::sum,
            (Integer a, Integer b) -> a - b,
            (Integer a, Integer b) -> a * b,
            (Integer a, Integer b) -> a / b

    );

    public static final List<UnaryOperator<Integer>> integerUnaryOperationsNC = List.of(
            (Integer a) -> -a
    );

    public static final List<BinaryOperator<Byte>> byteBinaryOperations = List.of(
            (Byte a, Byte b) -> (byte) (a.intValue() + b.intValue()),
            (Byte a, Byte b) -> (byte) (a.intValue() - b.intValue()),
            (Byte a, Byte b) -> (byte) (a.intValue() * b.intValue()),
            (Byte a, Byte b) -> (byte) (a.intValue() / b.intValue())
    );

    public static final List<UnaryOperator<Byte>> byteUnaryOperation = List.of(
            (Byte a) -> (byte) (-a.intValue())
    );

    public static final List<BinaryOperator<Boolean>> booleanBinaryOperations = List.of(
            (Boolean a, Boolean b) -> a || b,
            (Boolean a, Boolean b) -> a ^ b,
            (Boolean a, Boolean b) -> a && b,
            (Boolean a, Boolean b) -> {
                if (!b) {
                    throw new DivisionByZeroException("divide by zero: " + a + " / " + 0);
                }
                return a;
            }
    );

    public static final List<UnaryOperator<Boolean>> booleanUnaryOperations = List.of(
            (Boolean a) -> a
    );

    public static final IntFunction<Boolean> intToBoolean = (int x) -> x != 0;
    public static final IntFunction<Byte> intToByte = (int x) -> (byte) x;
    public static final IntFunction<Integer> intToInteger = (int x) -> x;
    public static final IntFunction<Double> intToDouble = (int x) -> (double) x;
    public static final IntFunction<BigInteger> intToBigInteger = BigInteger::valueOf;

    public static final Converter<Boolean> stringToBoolean = (String x) -> Integer.parseInt(x) != 0;
    public static final Converter<Byte> stringToByte = Byte::parseByte;
    public static final Converter<Integer> stringToInt = Integer::parseInt;
    public static final Converter<Double> stringToDouble = Double::parseDouble;
    public static final Converter<BigInteger> stringToBigInt = (String x) -> new BigInteger(x, 10);

}
