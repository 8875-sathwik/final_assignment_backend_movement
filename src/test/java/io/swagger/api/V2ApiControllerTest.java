package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class V2ApiControllerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SecurityApi securityApi;

    @Mock
    private UserService userService;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private ErrorHandler errorHandler;

    @Mock
    private ResponseHandeler responseHandeler;

    @InjectMocks
    private V2ApiController v2ApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteInventory_Success() {
        // Arrange
        doReturn("validAuth").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(inventoryService.checkInventoryById(123L)).thenReturn(false);
        when(responseHandeler.buildSuccessResponse(anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("Inventory updated successfully", HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = v2ApiController.deleteInventory(123L, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inventory updated successfully", response.getBody());
    }

    @Test
    public void testDeleteInventory_Unauthorized() {
        // Arrange
        doReturn("invalidAuth").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        when(securityApi.authenticateUserWithCredentials("invalidAuth", "ADMIN")).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("provided Authentication is wrong or user is not authorized to perform this action", HttpStatus.UNAUTHORIZED));

        // Act
        ResponseEntity<Object> response = v2ApiController.deleteInventory(123L, request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("provided Authentication is wrong or user is not authorized to perform this action", response.getBody());
    }

    @Test
    public void testDeleteInventory_ItemNotExist() {
        // Arrange
        doReturn("validAuth").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(inventoryService.checkInventoryById(123L)).thenReturn(true);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("item does not exist", HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<Object> response = v2ApiController.deleteInventory(123L, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("item does not exist", response.getBody());
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange
        doReturn("validAuth").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        UserEntity existingUser = new UserEntity("user123", "John", "Doe", "john.doe@example.com", "password", "1234567890", new Date(System.currentTimeMillis()), "Y", "johnny", "workspace");
        when(userService.getUserbyUserId("user123")).thenReturn(existingUser);
        when(responseHandeler.buildSuccessResponse(anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("User deleted successfully", HttpStatus.OK));

        // Act
        ResponseEntity<Object> response = v2ApiController.deleteUser("user123", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteUser_Unauthorized() {
        // Arrange
        doReturn("invalidAuth").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        when(securityApi.authenticateUserWithCredentials("invalidAuth", "ADMIN")).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("provided Authentication is wrong or user is not authorized to perform this action", HttpStatus.UNAUTHORIZED));

        // Act
        ResponseEntity<Object> response = v2ApiController.deleteUser("user123", request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("provided Authentication is wrong or user is not authorized to perform this action", response.getBody());
    }

    @Test
    public void testDeleteUser_UserNotExist() {
        // Arrange
        doReturn("validAuth").when(request).getHeader(HttpHeaders.AUTHORIZATION);
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(userService.getUserbyUserId("user123")).thenReturn(null);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("user does not exist", HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<Object> response = v2ApiController.deleteUser("user123", request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("user does not exist", response.getBody());
    }
}

