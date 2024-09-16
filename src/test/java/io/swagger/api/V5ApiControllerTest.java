package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.entity.UserEntity;
import io.swagger.model.SellItempayload;
import io.swagger.model.UserLoginBody;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class V5ApiControllerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private SecurityApi mockSecurityApi;

    @Mock
    private UserService mockUserService;

    @Mock
    private InventoryService mockInventoryService;

    @Mock
    private ResponseHandeler mockResponseHandler;

    @Mock
    private ErrorHandler mockErrorHandler;

    @InjectMocks
    private V5ApiController v5ApiController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindUserById_Success() {
        // Mock HttpServletRequest
        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer abcdef123456");

        // Mock UserService behavior
        UserEntity mockUser = new UserEntity("testUserId", "John", "Doe", "john.doe@example.com",
                "password", "1234567890", new Date(System.currentTimeMillis()), "active", "johndoe", "workspace");
        when(mockUserService.getUserbyUserId(anyString())).thenReturn(mockUser);

        when(mockSecurityApi.authenticateUserWithCredentials(anyString(), anyString())).thenReturn(true);
        when(mockResponseHandler.buildSuccessResponse(anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("find user by id", HttpStatus.OK));


        // Test the method
        ResponseEntity<Object> response = v5ApiController.findUserById("testUserId", mockRequest);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }


    @Test
    public void testFindUserById_AuthenticationFailure() {
        // Mock HttpServletRequest
        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer abcdef123456");

        // Mock UserService behavior to return a user
        UserEntity mockUser = new UserEntity("testUserId", "John", "Doe", "john.doe@example.com",
                "password", "1234567890", new Date(System.currentTimeMillis()), "active", "johndoe", "workspace");
        when(mockUserService.getUserbyUserId(anyString())).thenReturn(mockUser);

        // Mock SecurityApi authentication failure
        when(mockSecurityApi.authenticateUserWithCredentials(anyString(), anyString())).thenReturn(false);
        when(mockResponseHandler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("provided Authentication is wrong or user is not authorized to perform this action", HttpStatus.UNAUTHORIZED));

        // Test the method
        ResponseEntity<Object> response = v5ApiController.findUserById("testUserId", mockRequest);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        // Add more assertions as per your error handling logic
    }

    @Test
    public void testSellInventory_Success() {
        // Mock HttpServletRequest
        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer abcdef123456");

        // Mock SecurityApi and UserService behaviors
        when(mockSecurityApi.getRoleFromAuthorization(anyString())).thenReturn("role");
        when(mockSecurityApi.getUserIdFromAuthorization(anyString())).thenReturn("testUserId");
        when(mockSecurityApi.authenticateUserWithCredentials(anyString(), anyString())).thenReturn(true);

        // Mock InventoryService behaviors
        SellItempayload mockPayload = new SellItempayload(1L, 5);
        when(mockInventoryService.checkInventoryById(anyLong())).thenReturn(false);
        when(mockInventoryService.checkWalletbalance(eq(mockPayload), anyString(), anyString())).thenReturn(false);
        when(mockInventoryService.checkInventoryQuantity(eq(mockPayload))).thenReturn(false);
        when(mockInventoryService.sellInventory(eq(mockPayload), anyString(), anyString())).thenReturn("Inventory sold successfully");

        when(mockErrorHandler.checkbody(mockPayload, List.of("quantity", "id"))).thenReturn(true);
        when(mockResponseHandler.buildSuccessResponse(anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("sell inventory success", HttpStatus.OK));


        // Test the method
        ResponseEntity<Object> response = v5ApiController.sellInventory(mockPayload, mockRequest);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as per your success response logic
    }



    @Test
    public void testLoginUser_Success() {
        // Mock HttpServletRequest
        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic QWxhZGRpbjpPcGVuU2VzYW1l");

        // Mock UserService behavior
        UserLoginBody mockBody = new UserLoginBody();
        mockBody.setUsername("username");
        mockBody.setPassword("password");
        UserEntity mockUser = new UserEntity("testUserId", "John", "Doe", "john.doe@example.com",
                "password", "1234567890", new Date(System.currentTimeMillis()), "active", "johndoe", "workspace");
        when(mockUserService.Authenticatelogin(any())).thenReturn(true);
        when(mockUserService.getUserbyUserName(anyString())).thenReturn(mockUser);
        when(mockUserService.getUserbyUserId(anyString())).thenReturn(mockUser);
        when(mockErrorHandler.checkbody(mockBody, List.of("username", "password"))).thenReturn(true);
        when(mockResponseHandler.buildLoginUserSuccessResponse(anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Mock SecurityApi behavior
        when(mockSecurityApi.authenticateBasicAuth(anyString())).thenReturn(true);
        when(mockSecurityApi.generateToken(anyString(), anyString(), anyString())).thenReturn("generatedAuthToken");

        // Test the method
        ResponseEntity<Object> response = v5ApiController.loginUser(mockBody, mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as per your success response logic
    }



    @Test
    public void testLogoutUser_Success() {
        // Mock HttpServletRequest
        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer abcdef123456");

        // Mock UserService behavior
        UserEntity mockUser = new UserEntity("testUserId", "John", "Doe", "john.doe@example.com",
                "password", "1234567890", new Date(System.currentTimeMillis()), "active", "johndoe", "workspace");
        when(mockUserService.getUserbyUserId(anyString())).thenReturn(mockUser);
        when(mockUserService.checkUserisAlreadyLoggedOut(anyString())).thenReturn(false);

        // Mock SecurityApi behavior
        when(mockSecurityApi.authenticateUserWithCredentials(anyString(), anyString())).thenReturn(true);

        // Test the method
        ResponseEntity<Object> response = v5ApiController.logoutUser("testUserId", mockRequest);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as per your success response logic
    }

    @Test
    public void testLogoutUser_UserAlreadyLoggedOut() {
        // Mock HttpServletRequest
        when(mockRequest.getHeader("Accept")).thenReturn("application/json");
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer abcdef123456");

        // Mock UserService behavior
        UserEntity mockUser = new UserEntity("testUserId", "John", "Doe", "john.doe@example.com",
                "password", "1234567890", new Date(System.currentTimeMillis()), "active", "johndoe", "workspace");
        when(mockUserService.getUserbyUserId(anyString())).thenReturn(mockUser);
        when(mockUserService.checkUserisAlreadyLoggedOut(anyString())).thenReturn(true);
        when(mockResponseHandler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));


        // Test the method
        ResponseEntity<Object> response = v5ApiController.logoutUser("testUserId", mockRequest);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}
