package expression.exceptions;

public class NegativeArgumentException extends ArithmeticException {
    public NegativeArgumentException(String message) {
        super(message);
    }
}
