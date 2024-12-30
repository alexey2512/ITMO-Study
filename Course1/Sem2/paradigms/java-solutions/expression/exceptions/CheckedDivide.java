package expression.exceptions;

import expression.BinaryOperation;
import expression.CommonExpression;

public class CheckedDivide extends BinaryOperation {

    public CheckedDivide(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected char getOperator() {
        return '/';
    }

    @Override
    protected int calculate(int a, int b) {
        if (b == 0) {
            throw new DivisionByZeroException("divide by zero: " + a + " / " + 0);
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException("overflow in divide: " + a + " / " + b);
        }
        return a / b;
    }
}
