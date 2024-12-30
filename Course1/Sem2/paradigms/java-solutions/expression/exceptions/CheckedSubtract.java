package expression.exceptions;

import expression.BinaryOperation;
import expression.CommonExpression;

public class CheckedSubtract extends BinaryOperation {


    public CheckedSubtract(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected char getOperator() {
        return '-';
    }

    @Override
    protected int calculate(int a, int b) {
        if ((a >= 0 && b <= 0 && a > Integer.MAX_VALUE + b) ||
                (a < 0 && b > 0 && a < Integer.MIN_VALUE + b)){
            throw new OverflowException("overflow in subtract: " + a + " - " + b);
        }
        return a - b;
    }
}
