package expression;

public class Negate extends UnaryOperation {

    public Negate(CommonExpression expr) {
        super(expr);
    }

    @Override
    protected String getOperator() {
        return "-";
    }

    @Override
    protected int calculate(int a) {
        return -a;
    }

}
