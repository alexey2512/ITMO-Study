package expression;

import java.util.Objects;

public class Variable extends ExpressionElement {

    private final String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (var) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("Variable must be x, y or z");
        };
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return Objects.equals(var, obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return var.hashCode();
    }

}
