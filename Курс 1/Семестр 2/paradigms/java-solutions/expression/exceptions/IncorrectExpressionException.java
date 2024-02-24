package expression.exceptions;

public abstract class IncorrectExpressionException extends RuntimeException {
    public IncorrectExpressionException(String message) {
        super(message);
    }
}
