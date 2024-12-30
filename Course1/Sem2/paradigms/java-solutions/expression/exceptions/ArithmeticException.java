package expression.exceptions;

public abstract class ArithmeticException extends RuntimeException {
    public ArithmeticException(String message) {
        super(message);
    }
}
