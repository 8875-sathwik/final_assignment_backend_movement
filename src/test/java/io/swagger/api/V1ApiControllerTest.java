package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.InventoryItem;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class V1ApiControllerTest {

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
    private V1ApiController v1ApiController;

    private InventoryItem inventoryItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryItem = new InventoryItem(123L, "Test Item", 10, 100.0f);
    }

    @Test
    public void testUpdateInventory_Success() {
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(errorHandler.checkbody(any(InventoryItem.class), anyList())).thenReturn(true);
        when(inventoryService.checkInventoryById(123L)).thenReturn(false);
        when(responseHandeler.buildSuccessResponse(anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("Inventory updated successfully", HttpStatus.OK));

        ResponseEntity<Object> response = v1ApiController.updateInventory(inventoryItem, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inventory updated successfully", response.getBody());
    }

    @Test
    public void testUpdateInventory_Unauthorized() {
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("invalidAuth");
        when(securityApi.authenticateUserWithCredentials("invalidAuth", "ADMIN")).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("provided Authentication is wrong or user is not authorized to perform this action", HttpStatus.UNAUTHORIZED));

        ResponseEntity<Object> response = v1ApiController.updateInventory(inventoryItem, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("provided Authentication is wrong or user is not authorized to perform this action", response.getBody());
    }

    @Test
    public void testUpdateInventory_BadRequest() {
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(errorHandler.checkbody(any(InventoryItem.class), anyList())).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("Provided quantity is invalid", HttpStatus.BAD_REQUEST));

        ResponseEntity<Object> response = v1ApiController.updateInventory(inventoryItem, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Provided quantity is invalid", response.getBody());
    }

    @Test
    public void testUpdateInventory_ItemNotExist() {
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("validAuth");
        when(securityApi.authenticateUserWithCredentials("validAuth", "ADMIN")).thenReturn(true);
        when(errorHandler.checkbody(any(InventoryItem.class), anyList())).thenReturn(true);
        when(inventoryService.checkInventoryById(123L)).thenReturn(true);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("item does not exist", HttpStatus.BAD_REQUEST));

        ResponseEntity<Object> response = v1ApiController.updateInventory(inventoryItem, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("item does not exist", response.getBody());
    }
}

