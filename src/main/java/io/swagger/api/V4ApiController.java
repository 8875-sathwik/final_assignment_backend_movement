package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.entity.InventoryEntity;
import io.swagger.entity.UserEntity;
import io.swagger.model.AddMoney;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")
@RestController
public class V4ApiController implements V4Api {

    private static final Logger log = LoggerFactory.getLogger(V4ApiController.class);

    private final ObjectMapper objectMapper;


    private final HttpServletRequest request;

    @Autowired
    private SecurityApi securityApi;

    @Autowired
    private UserService userService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ErrorHandler errorHandler;

    @Autowired
    private ResponseHandeler responseHandeler;


    @org.springframework.beans.factory.annotation.Autowired
    public V4ApiController(ObjectMapper objectMapper, HttpServletRequest request , SecurityApi securityApi ,UserService userService, InventoryService inventoryService , ErrorHandler errorHandler ,ResponseHandeler responseHandeler) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.securityApi = securityApi;
        this.errorHandler = errorHandler;
        this.userService = userService;
        this.inventoryService=inventoryService;
        this.responseHandeler=responseHandeler;
    }


    public ResponseEntity<Object> findInventory(@Parameter(in = ParameterIn.QUERY, description = "Name of the inventory item (optional, if provided, filters the inventory)" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name,HttpServletRequest request
    ) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);



        String role = securityApi.getRoleFromAuthorization(authorization) ;
        if (!securityApi.authenticateUserWithCredentials(authorization , role)) {
            return responseHandeler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong or user is not authorized to perform this action", "1001",
                    "Authentication is worng", "Auth_check", "AUTH_CHECK", HttpStatus.UNAUTHORIZED);
        }


        List<InventoryEntity> response = inventoryService.fetchInventory(name);

        if (response == null) {
            return responseHandeler.buildErrorResponse(
                    "BR1001", "inventory is null", "1004",
                    "no inventory is not present", "check.inventory", "Check_Inventory", HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(response,HttpStatus.OK);

    }


    public ResponseEntity<Object> addMoney(AddMoney body,HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);



        if (accept == null || !accept.contains("application/json")) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }

        String role = securityApi.getRoleFromAuthorization(authorization) ;
        if (!securityApi.authenticateUserWithCredentials(authorization , role)) {
            return responseHandeler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong or user is not authorized to perform this action", "1001",
                    "Authentication is worng", "Auth_check", "AUTH_CHECK", HttpStatus.UNAUTHORIZED);
        }

        List<String> fieldsToValidate = List.of( "ammount", "cardnumber", "cvv", "authcode");
        if (!errorHandler.checkbody(body, fieldsToValidate)) {
            return responseHandeler.buildErrorResponse(
                    "IV001", "Provided " + errorHandler.getErrorValue() + " is " + errorHandler.getErrorType(),
                    errorHandler.getErrorType() + "." + errorHandler.getErrorValue(),
                    errorHandler.getErrorType() + " " + errorHandler.getErrorValue(),
                    "validate_request_body", "V5_VALIDATE", HttpStatus.BAD_REQUEST);
        }

        String userId = securityApi.getUserIdFromAuthorization(authorization);
        UserEntity existingUser = userService.getUserbyUserId(userId);

        if (existingUser == null || "N".equals(existingUser)) {
            return responseHandeler.buildErrorResponse(
                    "BR1001", "user does not exist", "1004",
                    "user is not present", "logout", "Check_Inventory", HttpStatus.BAD_REQUEST);
        }

        userService.addmoney(body);
        return responseHandeler.buildSuccessResponse( "ADD_MONEY", "add_money", "User succefully credited " + body.getAmmount() + " rupees", HttpStatus.OK);
    }

}
