package tr.havelsan.ueransim.core.exceptions;

public class NotImplementedException extends RuntimeException {
    private String message;

    public NotImplementedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
