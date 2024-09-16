package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.entity.InventoryEntity;
import io.swagger.entity.UserEntity;
import io.swagger.model.AddMoney;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class V4ApiControllerTest {

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
    private V4ApiController v4ApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindInventory() {
        String name = "item1";
        String authorization = "Bearer token";
        List<InventoryEntity> inventoryList = List.of(new InventoryEntity("item1", 10, 99.99f));

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorization);
        when(securityApi.getRoleFromAuthorization(authorization)).thenReturn("role");
        when(securityApi.authenticateUserWithCredentials(authorization, "role")).thenReturn(true);
        when(inventoryService.fetchInventory(name)).thenReturn(inventoryList);


        ResponseEntity<Object> response = v4ApiController.findInventory(name, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inventoryList, response.getBody());
    }

    @Test
    public void testFindInventoryUnauthorized() {
        String name = "item1";
        String authorization = "Bearer token";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorization);
        when(securityApi.getRoleFromAuthorization(authorization)).thenReturn("role");
        when(securityApi.authenticateUserWithCredentials(authorization, "role")).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("provided Authentication is wrong or user is not authorized to perform this action", HttpStatus.UNAUTHORIZED));


        ResponseEntity<Object> response = v4ApiController.findInventory(name, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(responseHandeler).buildErrorResponse(
                eq("AC1001"), anyString(), eq("1001"),
                anyString(), anyString(), anyString(), eq(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void testAddMoney() {
        AddMoney body = new AddMoney();
        body.setAmmount(100.0f);
        body.setCardnumber("1234567890123456");
        body.setCvv("123");
        body.setAuthcode("authcode");

        String authorization = "Bearer token";
        String userId = "user1";
        UserEntity userEntity = new UserEntity(
                userId, "FirstName", "LastName", "email@example.com",
                "password", "1234567890", new Date(System.currentTimeMillis()), "active", "username", "workspace"
        );

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorization);
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(securityApi.getRoleFromAuthorization(authorization)).thenReturn("role");
        when(securityApi.authenticateUserWithCredentials(authorization, "role")).thenReturn(true);
        when(errorHandler.checkbody(body, List.of("ammount", "cardnumber", "cvv", "authcode"))).thenReturn(true);
        when(securityApi.getUserIdFromAuthorization(authorization)).thenReturn(userId);
        when(userService.getUserbyUserId(userId)).thenReturn(userEntity);
        when(responseHandeler.buildSuccessResponse(anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("add money success response", HttpStatus.OK));


        ResponseEntity<Object> response = v4ApiController.addMoney(body, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).addmoney(body);
    }

    @Test
    public void testAddMoneyUnauthorized() {
        AddMoney body = new AddMoney();
        String authorization = "Bearer token";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorization);
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(securityApi.getRoleFromAuthorization(authorization)).thenReturn("role");
        when(securityApi.authenticateUserWithCredentials(authorization, "role")).thenReturn(false);
        when(responseHandeler.buildErrorResponse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>("provided Authentication is wrong or user is not authorized to perform this action", HttpStatus.UNAUTHORIZED));

        ResponseEntity<Object> response = v4ApiController.addMoney(body, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(responseHandeler).buildErrorResponse(
                eq("AC1001"), anyString(), eq("1001"),
                anyString(), anyString(), anyString(), eq(HttpStatus.UNAUTHORIZED));
    }
}
