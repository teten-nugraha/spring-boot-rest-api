package id.test.springboottesting.exception;

import jakarta.validation.ValidationException;

public class NotValidPasswordException extends ValidationException {
    public NotValidPasswordException(String message) {
        super(message);
    }
}
