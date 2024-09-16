package io.swagger.api;

import io.swagger.entity.UserEntity;
import io.swagger.model.AddMoney;
import io.swagger.model.User;
import io.swagger.model.UserLoginBody;
import io.swagger.repository.userRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private userRepo userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUserbyUserId() {
        String userId = "testUserId";
        UserEntity expectedUser = new UserEntity(userId, "John", "Doe", "john@example.com",
                "password123", "1234567890", new Date(System.currentTimeMillis()), "ACTIVE", "john_doe", "workspace123");
        when(userRepo.findByUserId(userId)).thenReturn(expectedUser);

        UserEntity actualUser = userService.getUserbyUserId(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserbyUserName() {
        String userName = "testUserName";
        UserEntity expectedUser = new UserEntity("testUserId", "John", "Doe", "john@example.com",
                "password123", "1234567890", new Date(System.currentTimeMillis()), "ACTIVE", userName, "workspace123");
        when(userRepo.findByUserName(userName)).thenReturn(expectedUser);

        UserEntity actualUser = userService.getUserbyUserName(userName);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void changeloginStatus() {
        String userId = "testUserId";
        String loginStatus = "IN";
        UserEntity expectedUser = new UserEntity(userId, "John", "Doe", "john@example.com",
                "password123", "1234567890", new Date(System.currentTimeMillis()), "ACTIVE", "john_doe", "workspace123");
        when(userRepo.findByUserId(userId)).thenReturn(expectedUser);

        userService.changeloginStatus(userId, loginStatus);

        assertEquals(loginStatus, expectedUser.getLoginStatus());
        verify(userRepo, times(1)).save(expectedUser);
    }

    @Test
    void checkUserisAlreadyLoggedOut() {
        String userId = "testUserId";
        UserEntity expectedUser = new UserEntity(userId, "John", "Doe", "john@example.com",
                "password123", "1234567890", new Date(System.currentTimeMillis()), "OUT", "john_doe", "workspace123");
        when(userRepo.findByUserId(userId)).thenReturn(expectedUser);

        assertTrue(userService.checkUserisAlreadyLoggedOut(userId));
    }

    @Test
    void authenticatelogin() {
        UserLoginBody body = new UserLoginBody();
        body.setUsername("testUsername");
        body.setPassword("testPassword");
        UserEntity expectedUser = new UserEntity("testUserId", "John", "Doe", "john@example.com",
                "testPassword", "1234567890", new Date(System.currentTimeMillis()), "ACTIVE", "testUsername", "workspace123");
        when(userRepo.findByUserName(body.getUsername())).thenReturn(expectedUser);

        assertTrue(userService.Authenticatelogin(body));
    }

    @Test
    void createUserService() {
        User body = new User();
        body.setUserid("testUserId");
        body.setFirstName("John");
        body.setLastName("Doe");
        body.setEmail("john@example.com");
        body.setPassword("password123");
        body.setPhone("1234567890");
        body.setDob(new Date(System.currentTimeMillis()));
        body.setUsername("john_doe");
        body.setWorkspaceId("workspace123");

        userService.createUserService(body);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepo, times(1)).save(captor.capture());

        UserEntity savedUser = captor.getValue();
        assertEquals("testUserId", savedUser.getUserId());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("john@example.com", savedUser.getEmailId());
        assertEquals("password123", savedUser.getPassword());
        assertEquals("1234567890", savedUser.getMsisdn());
        assertEquals("john_doe", savedUser.getUserName());
        assertEquals("workspace123", savedUser.getWorkspace());

    }

    @Test
    void updateUserService() {
        User body = new User();

        body.setUserid("testUserId");
        body.setFirstName("John");
        body.setLastName("Doe");
        body.setEmail("john@example.com");
        body.setPassword("password123");
        body.setPhone("1234567890");
        body.setDob(new Date(System.currentTimeMillis()));
        body.setUsername("john_doe");
        body.setWorkspaceId("workspace123");

        UserEntity existingUser = new UserEntity("testUserId", "Old", "User", "old@example.com",
                "oldPassword", "0987654321", new Date(System.currentTimeMillis()), "ACTIVE", "old_user", "old_workspace");
        when(userRepo.findByUserId(body.getUserid())).thenReturn(existingUser);

        userService.updateUserService(body);

        assertEquals("John", existingUser.getFirstName());
        assertEquals("Doe", existingUser.getLastName());
        assertEquals("john@example.com", existingUser.getEmailId());
        assertEquals("password123", existingUser.getPassword());
        assertEquals("1234567890", existingUser.getMsisdn());
        assertEquals("john_doe", existingUser.getUserName());
        assertEquals("old_workspace", existingUser.getWorkspace()); // Ensure workspace is not updated
        verify(userRepo, times(1)).save(existingUser);
    }



    @Test
    void deleteUserService() {
        String userId = "testUserId";
        UserEntity user = new UserEntity(userId, "John", "Doe", "john@example.com",
                "password123", "1234567890", new Date(System.currentTimeMillis()), "ACTIVE", "john_doe", "workspace123");
        when(userRepo.findByUserId(userId)).thenReturn(user);

        userService.deleteUserService(userId);

        assertEquals("N", user.getStatus());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void addmoney() {
        AddMoney body = new AddMoney();
        body.setUserId("testUserId");
        body.setAmmount(100.0f);

        UserEntity user = new UserEntity("testUserId", "John", "Doe", "john@example.com",
                "password123", "1234567890", new Date(System.currentTimeMillis()), "ACTIVE", "john_doe", "workspace123");
        user.setWalBalnce(50.0f); // Assuming initial balance is 50.0f
        when(userRepo.findByUserId(body.getUserId())).thenReturn(user);

        userService.addmoney(body);

        assertEquals(150.0f, user.getWalBalnce()); // Expected balance after adding 100.0
        verify(userRepo, times(1)).save(user);
    }




}
