package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class V3ApiController implements V3Api {

    private static final Logger log = LoggerFactory.getLogger(V3ApiController.class);

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
    public V3ApiController(ObjectMapper objectMapper, HttpServletRequest request , SecurityApi securityApi ,UserService userService, InventoryService inventoryService , ErrorHandler errorHandler ,ResponseHandeler responseHandeler) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.securityApi = securityApi;
        this.errorHandler = errorHandler;
        this.userService = userService;
        this.inventoryService=inventoryService;
        this.responseHandeler=responseHandeler;
    }

    public ResponseEntity<Object> addInventory(@Parameter(in = ParameterIn.DEFAULT, description = "Create a new inventory in the store", required = true, schema = @Schema()) @Valid @RequestBody InventoryItem body,HttpServletRequest request
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

        List<String> fieldsToValidate = List.of("quantity", "price", "name");
        if (!errorHandler.checkbody(body, fieldsToValidate)) {
            return responseHandeler.buildErrorResponse(
                    "IV001", "Provided " + errorHandler.getErrorValue() + " is " + errorHandler.getErrorType(),
                    errorHandler.getErrorType() + "." + errorHandler.getErrorValue(),
                    errorHandler.getErrorType() + " " + errorHandler.getErrorValue(),
                    "validate_request_body", "V5_VALIDATE", HttpStatus.BAD_REQUEST);
        }

        inventoryService.createInventory(body);
        return responseHandeler.buildSuccessResponse( "ADD_INVENTORY", "add.inventory", "Inventory added successfully", HttpStatus.OK);
    }


    public ResponseEntity<Object> createUser(@Valid @RequestBody User body,HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (accept == null || !accept.contains("application/json")) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }

        if("ADMIN".equals(body.getWorkspaceId())){
            if (!securityApi.authenticateBasicAuth(authorization)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        if("STAFF".equals(body.getWorkspaceId())){
            if (!securityApi.authenticateUserWithCredentials(authorization,"ADMIN") || authorization.startsWith("Basic ")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }


        List<String> fieldsToValidate = List.of("username", "email", "workspaceId", "password", "phone");
        if (!errorHandler.checkbody(body, fieldsToValidate)) {
            return responseHandeler.buildErrorResponse(
                    "IV001", "Provided " + errorHandler.getErrorValue() + " is " + errorHandler.getErrorType(),
                    errorHandler.getErrorType() + "." + errorHandler.getErrorValue(),
                    errorHandler.getErrorType() + " " + errorHandler.getErrorValue(),
                    "validate_request_body", "V5_VALIDATE", HttpStatus.BAD_REQUEST);
        }

        if (!("ADMIN".equals(body.getWorkspaceId()) || "STAFF".equals(body.getWorkspaceId()))) {
            return responseHandeler.buildErrorResponse(
                    "AC1001", "Invalid workspace. Only ADMIN/STAFF is allowed", "1001",
                    "Role is invalid", "role_check", "role_CHECK", HttpStatus.BAD_REQUEST);
        }


        String userId = "US." + System.currentTimeMillis();
        body.setUserid(userId);
        userService.createUserService(body);

        return responseHandeler.buildUserSuccessResponse(userId, "ADD_USER", "add_user", "User created successfully", HttpStatus.OK);
    }
}


