package id.test.springboottesting.service;

import id.test.springboottesting.model.User;

import java.util.List;
import java.util.Optional;

/***
 * Project Name     : spring-boot-testing
 * Username         : Teten Nugraha
 * Date Time        : 12/18/2019
 * Telegram         : @tennugraha
 */

public interface UserService {
    Optional<User> login(String email, String password);
    User createUser(User user);
    User updateUser(User user);
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    void deleteUserById(Long id);
}
