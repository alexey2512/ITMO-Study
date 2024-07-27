package expression.exceptions;

import expression.*;

public class CheckedAdd extends BinaryOperation {

    public CheckedAdd(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected char getOperator() {
        return '+';
    }

    @Override
    protected int calculate(int a, int b) {
        if ((a >= 0 && b >= 0 && a > Integer.MAX_VALUE - b) ||
                (a < 0 && b < 0 && a < Integer.MIN_VALUE - b)) {
            throw new OverflowException("overflow in add: " + a + " + " + b);
        }
        return a + b;
    }

}