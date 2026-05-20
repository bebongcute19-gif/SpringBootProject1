package re.edu.exception;


public class JwtExceptionCustom extends RuntimeException {
    public JwtExceptionCustom(String message) {
        super(message);
    }
}
