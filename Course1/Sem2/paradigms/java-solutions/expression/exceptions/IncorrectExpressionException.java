package expression.exceptions;

public abstract class IncorrectExpressionException extends Exception {
    public IncorrectExpressionException(String message) {
        super(message);
    }
}
