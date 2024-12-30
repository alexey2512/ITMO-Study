package expression;

import java.util.Objects;

public abstract class UnaryOperation extends ExpressionElement {

    private final ExpressionElement expr;

    protected UnaryOperation(ExpressionElement expr) {
        this.expr = expr;
    }

    protected abstract String getOperator();

    protected abstract int calculate(int a);

    @Override
    public String toString() {
        return getOperator() + "(" + expr.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation) {
            return getOperator().equals(((UnaryOperation) obj).getOperator()) &&
                    expr.equals(((UnaryOperation) obj).getExpr());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr, getOperator());
    }

    public ExpressionElement getExpr() {
        return expr;
    }

    @Override
    public int evaluate(int x) {
        return calculate(expr.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(expr.evaluate(x, y, z));
    }
}
