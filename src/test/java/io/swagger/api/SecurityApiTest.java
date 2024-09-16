package io.swagger.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityApiTest {

    @Test
    void authenticate() {
        SecurityApi securityApi = new SecurityApi();
        assertTrue(securityApi.authenticate("userid", "password"));
    }

    @Test
    void generateToken() {
        SecurityApi securityApi = new SecurityApi();
        String token = securityApi.generateToken("userid", "password", "role");
        assertNotNull(token);
    }

    @Test
    void getUserIdFromAuthorization() {
        SecurityApi securityApi = new SecurityApi();
        String token = securityApi.generateToken("userid", "password", "role");
        String authorization = "Bearer " + token;
        assertEquals("userid", securityApi.getUserIdFromAuthorization(authorization));
    }

    @Test
    void getRoleFromAuthorization() {
        SecurityApi securityApi = new SecurityApi();
        String token = securityApi.generateToken("userid", "password", "role");
        String authorization = "Bearer " + token;
        assertEquals("role", securityApi.getRoleFromAuthorization(authorization));
    }

    @Test
    void authenticateBasicAuth() {
        SecurityApi securityApi = new SecurityApi();
        assertTrue(securityApi.authenticateBasicAuth("Basic YWRtaW46c2VjcmV0")); // Base64 encoded "admin:secret"
        assertFalse(securityApi.authenticateBasicAuth("InvalidAuth"));
    }

    @Test
    void authenticateUserWithCredentials() {
        SecurityApi securityApi = spy(new SecurityApi());
        String mockToken = securityApi.generateToken("useridtest" , "userpasswordtest" ,"roletest");


        // Testing Bearer authentication
        String bearerAuthorization = "Bearer " + mockToken;
        assertTrue(securityApi.authenticateUserWithCredentials(bearerAuthorization, "roletest"));

        // Testing Basic authentication
        assertTrue(securityApi.authenticateUserWithCredentials("Basic YWRtaW46c2VjcmV0", "admin"));
    }

}
