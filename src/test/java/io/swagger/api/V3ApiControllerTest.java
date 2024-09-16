package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.InventoryItem;
import io.swagger.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class V3ApiControllerTest {

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
    private V3ApiController v3ApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddInventory_Success() {
        InventoryItem inventoryItem = new InventoryItem(123L, "Test Item", 10, 100.0f);

        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(errorHandler.checkbody(inventoryItem, List.of("quantity", "price", "name"))).thenReturn(true);
        when(responseHandeler.buildSuccessResponse(anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("Inventory added successfully", HttpStatus.OK));

        ResponseEntity<Object> response = v3ApiController.addInventory(inventoryItem, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inventory added successfully", response.getBody());
    }

    @Test
    public void testAddInventory_Unauthorized() {
        InventoryItem inventoryItem = new InventoryItem(123L, "Test Item", 10, 100.0f);

        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("invalidAuth");
        when(securityApi.authenticateUserWithCredentials("invalidAuth", "ADMIN")).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("provided Authentication is wrong or user is not authorized to perform this action", HttpStatus.UNAUTHORIZED));

        ResponseEntity<Object> response = v3ApiController.addInventory(inventoryItem, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("provided Authentication is wrong or user is not authorized to perform this action", response.getBody());
    }

    @Test
    public void testAddInventory_InvalidBody() {
        InventoryItem inventoryItem = new InventoryItem(123L, null, 10, 100.0f);

        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(errorHandler.checkbody(inventoryItem, List.of("quantity", "price", "name"))).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("Provided name is null", HttpStatus.BAD_REQUEST));

        ResponseEntity<Object> response = v3ApiController.addInventory(inventoryItem, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Provided name is null", response.getBody());
    }

    @Test
    public void testCreateUser_Success() {
        User user = new User();
        user.setUsername("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setWorkspaceId("ADMIN");
        user.setPassword("password");
        user.setPhone("1234567890");

        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateBasicAuth("validAuth")).thenReturn(true);
        when(errorHandler.checkbody(user, List.of("username", "email", "workspaceId", "password", "phone"))).thenReturn(true);
        when(responseHandeler.buildUserSuccessResponse(anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("User created successfully", HttpStatus.OK));

        ResponseEntity<Object> response = v3ApiController.createUser(user, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created successfully", response.getBody());
    }

    @Test
    public void testCreateUser_Unauthorized() {
        User user = new User();
        user.setUsername("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setWorkspaceId("ADMIN");
        user.setPassword("password");
        user.setPhone("1234567890");

        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("invalidAuth");
        when(securityApi.authenticateBasicAuth("invalidAuth")).thenReturn(false);

        ResponseEntity<Object> response = v3ApiController.createUser(user, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testCreateUser_InvalidBody() {
        User user = new User(); // This will create a User object with default values (null)

        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateBasicAuth("validAuth")).thenReturn(true);
        when(errorHandler.checkbody(user, List.of("username", "email", "workspaceId", "password", "phone"))).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("Provided username is null", HttpStatus.BAD_REQUEST));

        ResponseEntity<Object> response = v3ApiController.createUser(user, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Provided username is null", response.getBody());
    }

    @Test
    public void testCreateUser_InvalidWorkspace() {
        User user = new User();
        user.setUsername("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setWorkspaceId("INVALID");
        user.setPassword("password");
        user.setPhone("1234567890");

        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateBasicAuth("validAuth")).thenReturn(true);
        when(errorHandler.checkbody(user, List.of("username", "email", "workspaceId", "password", "phone"))).thenReturn(true);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("Invalid workspace. Only ADMIN/STAFF is allowed", HttpStatus.BAD_REQUEST));

        ResponseEntity<Object> response = v3ApiController.createUser(user, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid workspace. Only ADMIN/STAFF is allowed", response.getBody());
    }
}
