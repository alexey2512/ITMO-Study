package expression.exceptions;

import expression.BinaryOperation;
import expression.CommonExpression;
import static java.lang.Math.abs;

public class CheckedMultiply extends BinaryOperation {

    public CheckedMultiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected char getOperator() {
        return '*';
    }

    @Override
    protected int calculate(int a, int b) {
        if (a == Integer.MIN_VALUE && b != 1 && b != 0 || b == Integer.MIN_VALUE && a != 1 && a != 0) {
            throw new OverflowException("overflow in multiply: " + a + " * " + b);
        }
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE / b || a > 0 && b < 0 && b < Integer.MIN_VALUE / a ||
        a < 0 && b > 0 && a < Integer.MIN_VALUE / b || a < 0 && b < 0 && (-a) > Integer.MAX_VALUE / (-b)) {
            throw new OverflowException("overflow in multiply: " + a + " * " + b);
        }
        return a * b;
    }
}
