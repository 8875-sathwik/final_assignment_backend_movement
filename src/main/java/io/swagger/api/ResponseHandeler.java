package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Service
public class ResponseHandeler {

    public ResponseEntity<Object> buildUserSuccessResponse(String userId, String code, String serviceFlow, String message , HttpStatus status) {
        String serviceRequestId = UUID.randomUUID().toString();
        UserSuccessResponse response = new UserSuccessResponse();
        response.setServiceRequestId(serviceRequestId);
        response.setCode(code);
        response.setServiceFlow(serviceFlow);
        response.setMessage(message);
        response.setUserid(userId);
        response.setStatus("SUCCEEDED");

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<Object> buildSuccessResponse(String code, String serviceFlow, String message , HttpStatus status) {
        String serviceRequestId = UUID.randomUUID().toString();

        SuccessResponse response = new SuccessResponse();
        response.setServiceRequestId(serviceRequestId);
        response.setCode(code);
        response.setServiceFlow(serviceFlow);
        response.setMessage(message);
        response.setStatus("SUCCEEDED");

        return new ResponseEntity<>(response, status);
    }



    public ResponseEntity<Object> buildLoginUserSuccessResponse(String userId, String authToken, HttpStatus status) {
        String serviceRequestId = UUID.randomUUID().toString();

        LoginSuccessResponse response = new LoginSuccessResponse();
        response.setServiceRequestId(serviceRequestId);
        response.setCode("LOG01");
        response.setAuthtoken(authToken);
        response.setServiceFlow("User_Login");
        response.setMessage("user logged in successfully");
        response.setUserid(userId);
        response.setStatus("SUCCEEDED");

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<Object> buildErrorResponse(String errorCode, String errorUserMsg, String code, String message, String step, String serviceFlow, HttpStatus status) {
        Error error = new Error();
        ErrorErrors e = new ErrorErrors();
        e.setCode(code);
        e.setMessage(message);

        List<ErrorErrors> errorList = new ArrayList<>();
        errorList.add(e);

        error.setErrors(errorList);
        error.setErrorCode(errorCode);
        error.setErrorUserMsg(errorUserMsg);
        error.setStep(step);
        error.setStatus("FAILED");
        error.setServiceRequestId(UUID.randomUUID().toString());
        error.setHttpErrorCode(String.valueOf(status.value()));
        error.setServiceFlow(serviceFlow);

        return new ResponseEntity<>(error, status);
    }

}
