package expression;

public class Add extends BinaryOperation {

    public Add(ExpressionElement left, ExpressionElement right) {
        super(left, right);
    }

    @Override
    protected char getOperator() {
        return '+';
    }

    @Override
    protected int calculate(int a, int b) {
        return a + b;
    }

}
