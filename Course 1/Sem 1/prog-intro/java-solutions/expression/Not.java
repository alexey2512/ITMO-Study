package expression;

public class Not extends UnaryOperation {

    public Not(ExpressionElement expr) {
        super(expr);
    }

    @Override
    protected String getOperator() {
        return "~";
    }

    @Override
    protected int calculate(int a) {
        return ~a;
    }
}
