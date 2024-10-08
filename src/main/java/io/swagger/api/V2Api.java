/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.56).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.SuccessResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")
@Validated
public interface V2Api {

    @Operation(summary = "Delete an existing inventory", description = "Delete an existing inventory by Id", security = {
        @SecurityRequirement(name = "adminToken")    }, tags={ "inventory" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
        
        @ApiResponse(responseCode = "400", description = "Inventory not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))) })
    @RequestMapping(value = "/v2/delete-inventory",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteInventory(@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the item to delete." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "itemId", required = true) Long itemId, HttpServletRequest request
);


    @Operation(summary = "Delete user", description = "This can only be done by the logged in user.", security = {
        @SecurityRequirement(name = "adminToken"),
@SecurityRequirement(name = "staffToken")    }, tags={ "user" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "400", description = "Invalid username supplied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))) })
    @RequestMapping(value = "/v2/delete/user/{userid}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteUser(@Parameter(in = ParameterIn.PATH, description = "The userid that needs to be deleted", required=true, schema=@Schema()) @PathVariable("userid") String userid,HttpServletRequest request
);

}

