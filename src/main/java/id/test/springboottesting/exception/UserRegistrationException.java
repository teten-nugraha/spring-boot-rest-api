package id.test.springboottesting.exception;

/***
 * Project Name     : spring-boot-testing
 * Username         : Teten Nugraha
 * Date Time        : 12/18/2019
 * email            : Teten.nugraha@allianz.co.id
 * Telegram         : @tennugraha
 */

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}