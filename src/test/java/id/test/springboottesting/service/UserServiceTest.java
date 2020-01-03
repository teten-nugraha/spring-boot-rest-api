package id.test.springboottesting.service;

import id.test.springboottesting.exception.UserRegistrationException;
import id.test.springboottesting.model.User;
import id.test.springboottesting.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/***
 * Project Name     : spring-boot-testing
 * Username         : Teten Nugraha
 * Date Time        : 12/18/2019
 * Telegram         : @tennugraha
 */

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSavedUserSuccessFully() {
        final User user = new User(null, "ten@mail.com","teten","teten");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(userRepository.save(user)).willAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.createUser(user);

        assertThat(savedUser).isNotNull();

        verify(userRepository).save(any(User.class));

    }

    @Test
    void shouldThrowErrorWhenSaveUserWithExistingEmail() {
        final User user = new User(1L, "ten@mail.com","teten","teten");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(UserRegistrationException.class,() -> {
            userService.createUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser() {
        final User user = new User(1L, "ten@mail.com","teten","teten");

        given(userRepository.save(user)).willReturn(user);

        final User expected = userService.updateUser(user);

        assertThat(expected).isNotNull();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnFindAll() {
        List<User> datas = new ArrayList();
        datas.add(new User(1L, "ten@mail.com","teten","teten"));
        datas.add(new User(2L, "ten@mail.com","teten","teten"));
        datas.add(new User(3L, "ten@mail.com","teten","teten"));

        given(userRepository.findAll()).willReturn(datas);

        List<User> expected = userService.findAllUsers();

        assertEquals(expected, datas);
    }

    @Test
    void findUserById(){
        final Long id = 1L;
        final User user = new User(1L, "ten@mail.com","teten","teten");

        given(userRepository.findById(id)).willReturn(Optional.of(user));

        final Optional<User> expected  =userService.findUserById(id);

        assertThat(expected).isNotNull();

    }

    @Test
    void shouldBeDelete() {
        final Long userId=1L;

        userService.deleteUserById(userId);
        userService.deleteUserById(userId);

        verify(userRepository, times(2)).deleteById(userId);
    }

}