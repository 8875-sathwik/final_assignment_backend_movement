package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.entity.UserEntity;
import io.swagger.model.InventoryItem;
import io.swagger.model.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")
@RestController
public class V1ApiController implements V1Api {

    private static final Logger log = LoggerFactory.getLogger(V1ApiController.class);

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
    public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request , SecurityApi securityApi,ErrorHandler errorHandler,UserService userService,InventoryService inventoryService,ResponseHandeler responseHandeler) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.securityApi = securityApi;
        this.errorHandler = errorHandler;
        this.userService = userService;
        this.inventoryService=inventoryService;
        this.responseHandeler=responseHandeler;
    }

    public ResponseEntity<Object> updateInventory(@Parameter(in = ParameterIn.DEFAULT, description = "Update an existent inventory in the store", required=true, schema=@Schema()) @Valid @RequestBody InventoryItem body,HttpServletRequest request
) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (accept == null || !accept.contains("application/json")) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }

        if (!securityApi.authenticateUserWithCredentials(authorization , "ADMIN")) {
            return responseHandeler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong or user is not authorized to perform this action", "1001",
                    "Authentication is worng", "Auth_check", "AUTH_CHECK", HttpStatus.UNAUTHORIZED);
        }

        List<String> fieldsToValidate = List.of("quantity", "price", "name" , "id");
       if (!errorHandler.checkbody(body, fieldsToValidate)) {
            return responseHandeler.buildErrorResponse(
                    "IV001", "Provided " + errorHandler.getErrorValue() + " is " + errorHandler.getErrorType(),
                    errorHandler.getErrorType() + "." + errorHandler.getErrorValue(),
                    errorHandler.getErrorType() + " " + errorHandler.getErrorValue(),
                    "validate_request_body", "V5_VALIDATE", HttpStatus.BAD_REQUEST);
       }

        if (inventoryService.checkInventoryById(body.getId())) {
            return responseHandeler.buildErrorResponse(
                    "BR1001", "item does not exist", "1004",
                    "inventory is not present", "check.inventory", "Check_Inventory", HttpStatus.BAD_REQUEST);
        }

        inventoryService.updateInventory(body);
        return responseHandeler.buildSuccessResponse( "UPDATE_INVENTORY", "update.inventory", "Inventory updated successfully", HttpStatus.OK);

    }

    public ResponseEntity<Object> updateUser(@Parameter(in = ParameterIn.PATH, description = "Name that needs to be updated", required=true, schema=@Schema())
 @Valid @RequestBody User body,HttpServletRequest request
) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);


        if (accept == null || !accept.contains("application/json")) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

        if (!securityApi.authenticateUserWithCredentials(authorization , "ADMIN")) {
            return responseHandeler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong or user is not authorized to perform this action", "1001",
                    "Authentication is worng", "Auth_check", "AUTH_CHECK", HttpStatus.UNAUTHORIZED);
        }


        List<String> fieldsToValidate = List.of("username", "email", "password", "phone" , "id");
        if (!errorHandler.checkbody(body, fieldsToValidate)) {
            return responseHandeler.buildErrorResponse(
                    "IV001", "Provided " + errorHandler.getErrorValue() + " is " + errorHandler.getErrorType(),
                    errorHandler.getErrorType() + "." + errorHandler.getErrorValue(),
                    errorHandler.getErrorType() + " " + errorHandler.getErrorValue(),
                    "validate_request_body", "V5_VALIDATE", HttpStatus.BAD_REQUEST);
        }


        UserEntity existingUser = userService.getUserbyUserId(body.getUserid());

        if (existingUser == null || "N".equals(existingUser.getStatus())) {
            return responseHandeler.buildErrorResponse(
                    "BR1001", "user does not exist", "1004",
                    "user is not present", "user_check", "Check_user", HttpStatus.BAD_REQUEST);
        }

        boolean isAdmin = "ADMIN".equals(existingUser.getWorkspace());
        boolean isUserIdNull = body.getUserid() == null;
        boolean isWorkspaceIdChanged = body.getWorkspaceId() == existingUser.getWorkspace();

        if (isAdmin || isUserIdNull || isWorkspaceIdChanged) {
            String errorCode;
            String message;
            String details;
            String type;

            if (isAdmin) {
                errorCode = "AC1001";
                message = "Only staff user can be updated";
                details = "Role is invalid";
                type = "role_check";
            } else if (isUserIdNull) {
                errorCode = "AC1001";
                message = "User ID is not passed";
                details = "User ID is empty or invalid";
                type = "field_check";
            } else {
                errorCode = "AC1001";
                message = "Workspace cannot be changed";
                details = "Workspace cannot be changed";
                type = "field_check";
            }

            return responseHandeler.buildErrorResponse(
                    errorCode, message, "1001",
                    details, type, type.toUpperCase(), HttpStatus.BAD_REQUEST);
        }


        userService.updateUserService(body);
        return responseHandeler.buildSuccessResponse( "UPDATE_USER", "update_user", "User updated successfully", HttpStatus.OK);
    }


}
