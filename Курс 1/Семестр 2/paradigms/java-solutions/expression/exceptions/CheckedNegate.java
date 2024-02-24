package expression.exceptions;

import expression.CommonExpression;
import expression.UnaryOperation;

public class CheckedNegate extends UnaryOperation {

    public CheckedNegate(CommonExpression expr) {
        super(expr);
    }

    @Override
    protected String getOperator() {
        return "-";
    }

    @Override
    protected int calculate(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("overflow in negate: " + "-(" + a + ")");
        }
        return -a;
    }

}
