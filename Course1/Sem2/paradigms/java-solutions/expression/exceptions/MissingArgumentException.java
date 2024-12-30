package expression.exceptions;

public class MissingArgumentException extends IncorrectExpressionException {
    public MissingArgumentException(String message) {
        super(message);
    }
}
