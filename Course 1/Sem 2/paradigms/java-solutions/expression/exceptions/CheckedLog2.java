package expression.exceptions;

import expression.CommonExpression;
import expression.UnaryOperation;

public class CheckedLog2 extends UnaryOperation {

    protected CheckedLog2(CommonExpression expr) {
        super(expr);
    }

    @Override
    protected String getOperator() {
        return "log2";
    }

    @Override
    protected int calculate(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException("not positive argument for log2");
        }
        int ans = 0;
        while (a > 1) {
            a /= 2;
            ans++;
        }
        return ans;
    }
}
