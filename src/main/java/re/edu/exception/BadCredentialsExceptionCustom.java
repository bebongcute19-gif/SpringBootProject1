package re.edu.exception;

public class BadCredentialsExceptionCustom extends RuntimeException {
    public BadCredentialsExceptionCustom(String message) {
        super(message);
    }
}

