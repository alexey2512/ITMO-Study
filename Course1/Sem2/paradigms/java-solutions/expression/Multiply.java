package expression;

public class Multiply extends BinaryOperation {

    public Multiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected char getOperator() {
        return '*';
    }

    @Override
    protected int calculate(int a, int b) {
        return a * b;
    }

}
