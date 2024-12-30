package expression;

public class BitOr extends BinaryOperation {

    public BitOr(ExpressionElement left, ExpressionElement right) {
        super(left, right);
    }

    @Override
    protected char getOperator() {
        return '|';
    }

    @Override
    protected int calculate(int a, int b) {
        return a | b;
    }
}
