package io.swagger.api;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
public class SecurityApi {

    private final Key secretKey;


    public SecurityApi() {
        this.secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    // Dummy method to simulate user authentication
    public boolean authenticate(String userid, String password) {
        // Perform authentication logic here
        return true; // For demo purposes, always return true
    }



    // Method to generate JWT token with user information including role
    public String generateToken(String userid, String password, String role) {


        // Check if user is authenticated
        if (authenticate(userid, password)) {
            // Generate JWT token with user information


            return Jwts.builder()
                    .setSubject(userid)
                    .claim("userid", userid)
                    .claim("role", role) // Add role information to the token
                    .signWith(secretKey) // Use the generated secure key
                    .compact();
        } else {
            // Handle authentication failure
            throw new RuntimeException("Authentication failed");
        }
    }

    public String getUserIdFromAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }

        String token = authorization.substring(7); // Remove "Bearer " prefix
        DecodedJWT jwt = JWT.decode(token);
        String userid = jwt.getClaim("userid").asString();

        return userid;
    }


    public String getRoleFromAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }

        String token = authorization.substring(7); // Remove "Bearer " prefix
        DecodedJWT jwt = JWT.decode(token);
        String role = jwt.getClaim("role").asString();

        return role;
    }



    public boolean authenticateBasicAuth(String authorization) {
        return authorization != null && authorization.startsWith("Basic ") &&
                new String(Base64.getDecoder().decode(authorization.substring(6))).split(":")[0].equals("admin") &&
                new String(Base64.getDecoder().decode(authorization.substring(6))).split(":")[1].equals("secret");
    }

    public boolean authenticateUserWithCredentials(String authorization, String role) {
            // Validate username and password from JWT token for "Bearer" authentication
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7); // Extract token after "Bearer "
                try {
                    // Parse the token and extract user information
                    Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
                    String username = claims.getSubject();
                    String tokenRole = claims.get("role", String.class);

                    // Validate the role and other conditions if needed
                    return username != null && role.equals(tokenRole);
                } catch (JwtException e) {
                    // Token validation failed
                    return false;
                }

        } else {
              return   authenticateBasicAuth(authorization);
        }
    }

}
