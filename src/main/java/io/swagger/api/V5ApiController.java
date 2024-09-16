package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.entity.UserEntity;
import io.swagger.model.SellItempayload;
import io.swagger.model.UserLoginBody;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")
@RestController
public class V5ApiController implements V5Api {

    private static final Logger log = LoggerFactory.getLogger(V5ApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private SecurityApi securityApi;

    @Autowired
    private  UserService userService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private  ResponseHandeler responseHandler;

    @Autowired
    private ErrorHandler errorHandler;



    @org.springframework.beans.factory.annotation.Autowired
    public V5ApiController(ObjectMapper objectMapper, HttpServletRequest request , SecurityApi securityApi ,UserService userService, InventoryService inventoryService , ErrorHandler errorHandler ,ResponseHandeler responseHandler) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.securityApi = securityApi;
        this.errorHandler = errorHandler;
        this.userService = userService;
        this.inventoryService=inventoryService;
        this.responseHandler=responseHandler;
    }

    public ResponseEntity<Object> findUserById(@Parameter(in = ParameterIn.PATH, description = "unique id assigned for an user", required=true, schema=@Schema()) @PathVariable("userid") String userid,HttpServletRequest request
) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String role = userService.getUserbyUserId(userid).getWorkspace();

        var authrole = securityApi.getRoleFromAuthorization(authorization);

        boolean isRoleMatching = authrole.equals(role);

        boolean isAuthenticated = isRoleMatching
                ? securityApi.authenticateUserWithCredentials(authorization, role)
                : securityApi.authenticateUserWithCredentials(authorization, "ADMIN");

        if (!isAuthenticated) {
            return responseHandler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong", "1001",
                    "Authentication is worng", "logout", "AUTH_CHECK", HttpStatus.BAD_REQUEST);
        }

        UserEntity u = userService.getUserbyUserId(userid);
        return new ResponseEntity<Object>( u, HttpStatus.OK);

//        if(authrole != role){
//            if (!securityApi.authenticateUserWithCredentials(authorization , "ADMIN")) {
//                return responseHandler.buildErrorResponse(
//                        "AC1001", "provided Authentication is wrong", "1001",
//                        "Authentication is worng", "logout", "AUTH_CHECK", HttpStatus.BAD_REQUEST);
//            }
//
//            UserEntity u = userService.getUserbyUserId(userid);
//            return new ResponseEntity<Object>( u, HttpStatus.OK);
//        }else {
//            if (!securityApi.authenticateUserWithCredentials(authorization , role)) {
//                return responseHandler.buildErrorResponse(
//                        "AC1001", "provided Authentication is wrong", "1001",
//                        "Authentication is worng", "logout", "AUTH_CHECK", HttpStatus.BAD_REQUEST);
//            }
//
//            UserEntity u = userService.getUserbyUserId(userid);
//            return new ResponseEntity<Object>( u, HttpStatus.OK);
//        }

    }




    public ResponseEntity<Object> logoutUser(
            @Parameter(in = ParameterIn.PATH, description = "User ID to logout", required = true, schema = @Schema())
            @PathVariable("userId") String userId,HttpServletRequest request) {

        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String role = userService.getUserbyUserId(userId).getWorkspace();

        if (!securityApi.authenticateUserWithCredentials(authorization , role)) {
            return responseHandler.buildErrorResponse(
                    "AC1001", "providedAuthentication is wrong", "1001",
                    "Authentication is worng", "logout", "AUTH_CHECK", HttpStatus.BAD_REQUEST);
        }

        if (userService.checkUserisAlreadyLoggedOut(userId)) {
            return responseHandler.buildErrorResponse(
                    "BR1001", "User is already logged out", "1004",
                    "user logged out alrady", "logout", "V5_LOGOUT", HttpStatus.BAD_REQUEST);
        }

        userService.changeloginStatus(userId,"OUT");
        return new ResponseEntity<Object>("user logged out succesfully",HttpStatus.OK);
    }


    public ResponseEntity<Object> sellInventory(@Parameter(in = ParameterIn.DEFAULT, description = "sell inventory from store", required=true, schema=@Schema()) @Valid @RequestBody SellItempayload body,HttpServletRequest request
) {
        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (accept == null || !accept.contains("application/json")) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }

        String role = securityApi.getRoleFromAuthorization(authorization) ;
        String userId = securityApi.getUserIdFromAuthorization(authorization);
        if (!securityApi.authenticateUserWithCredentials(authorization , role)) {
            return responseHandler.buildErrorResponse(
                    "AC1001", "provided Authentication is wrong or user is not authorized to perform this action", "1001",
                    "Authentication is worng", "Auth_check", "AUTH_CHECK", HttpStatus.UNAUTHORIZED);
        }

        List<String> fieldsToValidate = List.of("quantity", "id");
        if (!errorHandler.checkbody(body, fieldsToValidate)) {
            return responseHandler.buildErrorResponse(
                    "IV001", "Provided " + errorHandler.getErrorValue() + " is " + errorHandler.getErrorType(),
                    errorHandler.getErrorType() + "." + errorHandler.getErrorValue(),
                    errorHandler.getErrorType() + " " + errorHandler.getErrorValue(),
                    "validate_request_body", "V5_VALIDATE", HttpStatus.BAD_REQUEST);
        }

        if (inventoryService.checkInventoryById(body.getId())) {
            return responseHandler.buildErrorResponse(
                    "BR1001", "item does not exist", "1004",
                    "inventory is not present", "check.inventory", "Check_Inventory", HttpStatus.BAD_REQUEST);
        }

        if (inventoryService.checkWalletbalance(body ,role, userId)) {
            return responseHandler.buildErrorResponse(
                    "BR1001", "balance can not be negetive", "1004",
                    "userwalletbalnce is low", "check.balnce", "Check_balance", HttpStatus.BAD_REQUEST);
        }

        if (inventoryService.checkInventoryQuantity(body)) {
            return responseHandler.buildErrorResponse(
                    "BR1001", "Inventory does not have " + body.getQuantity() + " " + body.getName() , "1004",
                    "number of items is low", "check.quantity", "Check_quantity", HttpStatus.BAD_REQUEST);
        }

       String message = inventoryService.sellInventory(body , role , userId);

        return responseHandler.buildSuccessResponse( "SELL_INVENTORY", "sell.inventory", message, HttpStatus.OK);

    }


    public ResponseEntity<Object> loginUser(
            @Parameter(in = ParameterIn.DEFAULT, description = "User login credentials", required = true, schema = @Schema())
            @Valid @RequestBody UserLoginBody body,HttpServletRequest request) {

        String accept = request.getHeader("Accept");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userId = userService.getUserbyUserName(body.getUsername()).getUserId();
        List<String> fieldsToValidate = List.of("username", "password");


        if (accept == null || !accept.contains("application/json")) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

        if (!securityApi.authenticateBasicAuth(authorization)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!userService.Authenticatelogin(body)) {
            return responseHandler.buildErrorResponse(
                    "BR1001", "provided password is incorrect", "1001",
                    "Incorrect username/password", "login", "V5_LOGIN", HttpStatus.BAD_REQUEST);
        }


        if (!errorHandler.checkbody(body , fieldsToValidate)){
            return responseHandler.buildErrorResponse(
                    "IV001", "provided "+ errorHandler.getErrorValue() + " is " + errorHandler.getErrorType(), errorHandler.getErrorType()+"."+ errorHandler.getErrorValue(),
                    errorHandler.getErrorType()+" " + errorHandler.getErrorValue(), "validate_request_body", "V5_VALIDATE", HttpStatus.BAD_REQUEST);
        }

        UserEntity u = userService.getUserbyUserId(userId);
        if (u == null || "N".equals(u.getStatus())) {
            return responseHandler.buildErrorResponse(
                    "BR1001", "user does not exist", "1004",
                    "user is not present", "user_check", "Check_user", HttpStatus.BAD_REQUEST);
        }



        String authToken = securityApi.generateToken(userId, body.getPassword(), u.getWorkspace());



        userService.changeloginStatus(userId,"IN");
        return responseHandler.buildLoginUserSuccessResponse(userId , authToken, HttpStatus.OK);
    }


}
