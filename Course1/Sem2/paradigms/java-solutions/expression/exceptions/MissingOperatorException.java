package expression.exceptions;

public class MissingOperatorException extends IncorrectExpressionException {
    public MissingOperatorException(String message) {
        super(message);
    }
}
