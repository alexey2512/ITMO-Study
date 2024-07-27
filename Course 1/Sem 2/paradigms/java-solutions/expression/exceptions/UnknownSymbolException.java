package expression.exceptions;

public class UnknownSymbolException extends IncorrectExpressionException {
    public UnknownSymbolException(String message) {
        super(message);
    }
}
