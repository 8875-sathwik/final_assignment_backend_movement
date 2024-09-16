package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.LoginSuccessResponse;
import io.swagger.model.SuccessResponse;
import io.swagger.model.UserSuccessResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseHandlerTest {

    private final ResponseHandeler responseHandler = new ResponseHandeler();

    @Test
    void buildUserSuccessResponse() {
        // Prepare data
        String userId = "user123";
        String code = "123";
        String serviceFlow = "flow";
        String message = "Success";
        HttpStatus status = HttpStatus.OK;

        // Build response
        ResponseEntity<Object> responseEntity = responseHandler.buildUserSuccessResponse(userId, code, serviceFlow, message, status);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserSuccessResponse responseBody = (UserSuccessResponse) responseEntity.getBody();
        assertEquals(code, responseBody.getCode());
        assertEquals(serviceFlow, responseBody.getServiceFlow());
        assertEquals(message, responseBody.getMessage());
        assertEquals(userId, responseBody.getUserid());
        assertEquals("SUCCEEDED", responseBody.getStatus());
    }

    @Test
    void buildSuccessResponse() {
        // Prepare data
        String code = "123";
        String serviceFlow = "flow";
        String message = "Success";
        HttpStatus status = HttpStatus.OK;

        // Build response
        ResponseEntity<Object> responseEntity = responseHandler.buildSuccessResponse(code, serviceFlow, message, status);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponse responseBody = (SuccessResponse) responseEntity.getBody();
        assertEquals(code, responseBody.getCode());
        assertEquals(serviceFlow, responseBody.getServiceFlow());
        assertEquals(message, responseBody.getMessage());
        assertEquals("SUCCEEDED", responseBody.getStatus());
    }

    @Test
    void buildLoginUserSuccessResponse() {
        // Prepare data
        String userId = "user123";
        String authToken = "authToken";
        HttpStatus status = HttpStatus.OK;

        // Build response
        ResponseEntity<Object> responseEntity = responseHandler.buildLoginUserSuccessResponse(userId, authToken, status);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        LoginSuccessResponse responseBody = (LoginSuccessResponse) responseEntity.getBody();
        assertEquals("LOG01", responseBody.getCode());
        assertEquals("User_Login", responseBody.getServiceFlow());
        assertEquals("user logged in successfully", responseBody.getMessage());
        assertEquals(userId, responseBody.getUserid());
        assertEquals("SUCCEEDED", responseBody.getStatus());
    }

    @Test
    void buildErrorResponse() {
        // Prepare data
        String errorCode = "ERR001";
        String errorUserMsg = "Error Message";
        String code = "123";
        String message = "Error";
        String step = "Step";
        String serviceFlow = "Flow";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Build response
        ResponseEntity<Object> responseEntity = responseHandler.buildErrorResponse(errorCode, errorUserMsg, code, message, step, serviceFlow, status);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Error responseBody = (Error) responseEntity.getBody();
        assertEquals(errorCode, responseBody.getErrorCode());
        assertEquals(errorUserMsg, responseBody.getErrorUserMsg());
        assertEquals(step, responseBody.getStep());
        assertEquals(serviceFlow, responseBody.getServiceFlow());
        assertEquals("FAILED", responseBody.getStatus());
        assertEquals(String.valueOf(status.value()), responseBody.getHttpErrorCode());
    }
}
