package servlet;

public class InvalidStateException extends Exception {

    public InvalidStateException() {
    }

    public InvalidStateException(String msg) {
        super(msg);
    }

    public InvalidStateException(Throwable cause) {
        super(cause);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

}
