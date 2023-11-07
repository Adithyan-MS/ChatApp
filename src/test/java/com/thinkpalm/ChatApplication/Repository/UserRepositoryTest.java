package com.thinkpalm.ChatApplication.Repository;

import com.thinkpalm.ChatApplication.Model.UserModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }
    @Test
    void saveUserWithDuplicatePhoneNoTest(){
        UserModel user = UserModel.builder()
                .name("akhil")
                .email("akhil@gmail.com")
                .password("akhil@123")
                .phone_number("0123453213")
                .build();
        ;
        assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.save(user));
    }

    @Test
    void findByNameIsUserTest() {
        UserModel user = userRepository.findByName("akhil").orElse(null);
        Assertions.assertThat(user.getName()).isEqualTo("akhil");
    }

    @Test
    void findByNameIsNullTest() {
        UserModel user = userRepository.findByName("akhi").orElse(null);
        Assertions.assertThat(user).isEqualTo(null);
    }

    @Test
    void updateUserBioTest() {
        userRepository.updateUserBio("aswani","hey everyone!");
        UserModel user = userRepository.findByName("aswani").orElse(null);
        assertEquals("hey everyone!", user.getBio());
    }

    @Test
    void findAllChatsOfUserHavingChatsTest() {
        var chats = userRepository.findAllChatsOfUser(19);
        Assertions.assertThat(chats.size()).isGreaterThan(0);
    }

    @Test
    void findAllChatsOfUserHavingNoChatsTest() {
        var chats = userRepository.findAllChatsOfUser(56);
        Assertions.assertThat(chats.size()).isEqualTo(0);
    }

    @Test
    void noExistByNameOrPhonenumber() {
        List<UserModel> users = userRepository.existByNameOrPhonenumber("ardora","4567456789");
        assertEquals(0,users.size());
    }

    @Test
    void isExistByNameOrPhonenumber() {
        List<UserModel> users = userRepository.existByNameOrPhonenumber("ardra","4567456789");
        Assertions.assertThat(users.size()).isGreaterThan(0);
    }
}