package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.entity.UserEntity;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")
@RestController
public class V2ApiController implements V2Api {

    private static final Logger log = LoggerFactory.getLogger(V2ApiController.class);

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
    public V2ApiController(ObjectMapper objectMapper, HttpServletRequest request , SecurityApi securityApi ,UserService userService, InventoryService inventoryService , ErrorHandler errorHandler ,ResponseHandeler responseHandeler) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.securityApi = securityApi;
        this.errorHandler = errorHandler;
        this.userService = userService;
        this.inventoryService=inventoryService;
        this.responseHandeler=responseHandeler;
    }

    public ResponseEntity<Object> deleteInventory(@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the item to delete." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "itemId", required = true) Long itemId,HttpServletRequest request
) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!securityApi.authenticateUserWithCredentials(authorization , "ADMIN")) {
            return responseHandeler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong or user is not authorized to perform this action", "1001",
                    "Authentication is worng", "Auth_check", "AUTH_CHECK", HttpStatus.UNAUTHORIZED);
        }


        if (inventoryService.checkInventoryById(itemId)) {
            return responseHandeler.buildErrorResponse(
                    "BR1001", "item does not exist", "1004",
                    "inventory is not present", "check.inventory", "Check_Inventory", HttpStatus.BAD_REQUEST);
        }

        inventoryService.deleteInventory(itemId);
        return responseHandeler.buildSuccessResponse( "UPDATE_INVENTORY", "update.inventory", "Inventory updated successfully", HttpStatus.OK);

    }

    public ResponseEntity<Object> deleteUser(@Parameter(in = ParameterIn.PATH, description = "The name that needs to be deleted", required=true, schema=@Schema()) @PathVariable("userid") String userid,HttpServletRequest request
) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!securityApi.authenticateUserWithCredentials(authorization , "ADMIN")) {
            return responseHandeler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong or user is not authorized to perform this action", "1001",
                    "Authentication is worng", "Auth_check", "AUTH_CHECK", HttpStatus.UNAUTHORIZED);
        }


        UserEntity existingUser = userService.getUserbyUserId(userid);

        if (existingUser == null || "N".equals(existingUser.getStatus())) {
            return responseHandeler.buildErrorResponse(
                    "BR1001", "user does not exist", "1004",
                    "user is not present", "user_check", "Check_user", HttpStatus.BAD_REQUEST);
        }


        userService.deleteUserService(userid);
        return responseHandeler.buildSuccessResponse( "DELETE_USER", "delete_user", "User deleted successfully", HttpStatus.OK);

    }

}
