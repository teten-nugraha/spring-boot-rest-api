package id.test.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.test.springboottesting.model.User;
import id.test.springboottesting.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/***
 * Project Name     : spring-boot-testing
 * Username         : Teten Nugraha
 * Date Time        : 12/18/2019
 * Telegram         : @tennugraha
 */

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        this.userList = new ArrayList<>();
        this.userList.add(new User(1L, "user1@gmail.com", "@Pwd123123", "User1"));
        this.userList.add(new User(2L, "user2@gmail.com", "@Pwd123123", "User2"));
        this.userList.add(new User(3L, "user3@gmail.com", "@Pwd123123", "User3"));

        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());
    }

    @Test
    void shouldFetchAllUsers() throws Exception {

        given(userServiceImpl.findAllUsers()).willReturn(userList);

        this.mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userList.size())));
    }

    @Test
    void shouldFetchOneUserById() throws Exception {
        final Long userId = 1L;
        final User user = new User(1L, "ten@mail.com", "@Teten123123", "teten");

        given(userServiceImpl.findUserById(userId)).willReturn(Optional.of(user));

        this.mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    void shouldReturn404WhenFindUserById() throws Exception {
        final Long userId = 1L;
        given(userServiceImpl.findUserById(userId)).willReturn(Optional.empty());

        this.mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        given(userServiceImpl.createUser(any(User.class))).willAnswer((invocation) -> invocation.getArgument(0));

        User user = new User(666L, "newuser1@gmail.com", "@Pwd123123", "Name");

        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.name", is(user.getName())))
        ;
    }

    @Test
    void shouldReturn400WhenCreateNewUserWithoutEmail() throws Exception {
        User user = new User(1001L, null, "@Pwd123123", "Name Aja");

        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("email")))
                .andExpect(jsonPath("$.violations[0].message", is("Email should not be empty")))
                .andReturn()
        ;
    }

    @Test
    void shouldUpdateUser() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "user1@gmail.com", "@Pwd123123", "Name");
        given(userServiceImpl.findUserById(userId)).willReturn(Optional.of(user));
        given(userServiceImpl.updateUser(any(User.class))).willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc.perform(put("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.name", is(user.getName())));

    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingUser() throws Exception {
        Long userId = 1L;
        given(userServiceImpl.findUserById(userId)).willReturn(Optional.empty());
        User user = new User(userId, "user1@gmail.com", "@Pwd123123", "Name");

        this.mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteUser() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "user1@gmail.com", "@Pwd123123", "Name");
        given(userServiceImpl.findUserById(userId)).willReturn(Optional.of(user));
        doNothing().when(userServiceImpl).deleteUserById(user.getId());

        this.mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.name", is(user.getName())));

    }

    @Test
    void shouldReturn404WhenDeletingNonExistingUser() throws Exception {
        Long userId = 1L;
        given(userServiceImpl.findUserById(userId)).willReturn(Optional.empty());

        this.mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

    }

}