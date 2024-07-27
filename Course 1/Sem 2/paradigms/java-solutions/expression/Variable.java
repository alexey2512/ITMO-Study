package expression;

import java.util.List;
import java.util.Objects;
import expression.exceptions.ExpressionParser.VariablesController;

public class Variable extends CommonExpression {

    private final int index;

    public Variable(String var) {
        this.index = VariablesController.indexOf(var);
    }

    public Variable(int index) {
        this.index = index;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (index) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IllegalArgumentException("unknown variable");
        };
    }

    @Override
    public String toString() {
        return VariablesController.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return Objects.equals(index, ((Variable) obj).index);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return variables.get(index);
    }

}
