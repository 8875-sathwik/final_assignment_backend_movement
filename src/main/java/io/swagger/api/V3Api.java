/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.56).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")
@Validated
public interface V3Api {

    @Operation(summary = "Add a new inventory to the store", description = "Add a new inventory to the store", security = {
        @SecurityRequirement(name = "BasicAuth")    }, tags={ "inventory" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventorySuccessResponse.class))),

        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))) })
    @RequestMapping(value = "/v3/create-inventory",
        produces = { "application/json", "application/xml" },
        consumes = { "application/json", "application/xml", "application/x-www-form-urlencoded" },
        method = RequestMethod.POST)
    ResponseEntity<Object> addInventory(@Parameter(in = ParameterIn.DEFAULT, description = "Create a new inventory in the store", required=true, schema=@Schema()) @Valid @RequestBody InventoryItem body,HttpServletRequest request
);


    @Operation(summary = "Create user", description = "create user" ,security = {
            @SecurityRequirement(name = "BasicAuth")    } ,tags={ "user" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSuccessResponse.class))),

        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))) })
    @RequestMapping(value = "/v3/create/user",
        produces = { "application/json", "application/xml" },
        consumes = { "application/json", "application/xml", "application/x-www-form-urlencoded" },
        method = RequestMethod.POST)
    ResponseEntity<Object> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Created user object", schema=@Schema()) @Valid @RequestBody User body, HttpServletRequest request
);



}

