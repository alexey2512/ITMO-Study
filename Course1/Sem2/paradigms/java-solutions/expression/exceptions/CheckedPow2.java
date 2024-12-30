package expression.exceptions;

import expression.CommonExpression;
import expression.UnaryOperation;

public class CheckedPow2 extends UnaryOperation {

    protected CheckedPow2(CommonExpression expr) {
        super(expr);
    }

    @Override
    protected String getOperator() {
        return "pow2";
    }

    @Override
    protected int calculate(int a) {
        if (a < 0) {
            throw new IllegalArgumentException("negative argument for pow2");
        } else if (a > 30) {
            throw new OverflowException("overflow in pow2");
        }
        return 1 << a;
    }
}
