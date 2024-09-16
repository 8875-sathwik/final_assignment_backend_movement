package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * LoginSuccessResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")


public class LoginSuccessResponse   {
  @JsonProperty("Authtoken")
  private String authtoken = null;

  @JsonProperty("serviceRequestId")
  private String serviceRequestId = null;

  @JsonProperty("userid")
  private String userid = null;

  @JsonProperty("serviceFlow")
  private String serviceFlow = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("message")
  private String message = null;

  public LoginSuccessResponse authtoken(String authtoken) {
    this.authtoken = authtoken;
    return this;
  }

  /**
   * Service request ID
   * @return authtoken
   **/
  @Schema(example = "unique JWT token", description = "Service request ID")
  
    public String getAuthtoken() {
    return authtoken;
  }

  public void setAuthtoken(String authtoken) {
    this.authtoken = authtoken;
  }

  public LoginSuccessResponse serviceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
    return this;
  }

  /**
   * Service request ID
   * @return serviceRequestId
   **/
  @Schema(example = "unique string", description = "Service request ID")
  
    public String getServiceRequestId() {
    return serviceRequestId;
  }

  public void setServiceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
  }

  public LoginSuccessResponse userid(String userid) {
    this.userid = userid;
    return this;
  }

  /**
   * unique userid
   * @return userid
   **/
  @Schema(example = "unique string", description = "unique userid")
  
    public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public LoginSuccessResponse serviceFlow(String serviceFlow) {
    this.serviceFlow = serviceFlow;
    return this;
  }

  /**
   * Service flow
   * @return serviceFlow
   **/
  @Schema(example = "string", description = "Service flow")
  
    public String getServiceFlow() {
    return serviceFlow;
  }

  public void setServiceFlow(String serviceFlow) {
    this.serviceFlow = serviceFlow;
  }

  public LoginSuccessResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Status of the operation
   * @return status
   **/
  @Schema(example = "SUCCEEDED", description = "Status of the operation")
  
    public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LoginSuccessResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Code related to the success message
   * @return code
   **/
  @Schema(example = "code", description = "Code related to the success message")
  
    public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public LoginSuccessResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Success message
   * @return message
   **/
  @Schema(example = "message string", description = "Success message")
  
    public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginSuccessResponse loginSuccessResponse = (LoginSuccessResponse) o;
    return Objects.equals(this.authtoken, loginSuccessResponse.authtoken) &&
        Objects.equals(this.serviceRequestId, loginSuccessResponse.serviceRequestId) &&
        Objects.equals(this.userid, loginSuccessResponse.userid) &&
        Objects.equals(this.serviceFlow, loginSuccessResponse.serviceFlow) &&
        Objects.equals(this.status, loginSuccessResponse.status) &&
        Objects.equals(this.code, loginSuccessResponse.code) &&
        Objects.equals(this.message, loginSuccessResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authtoken, serviceRequestId, userid, serviceFlow, status, code, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginSuccessResponse {\n");
    
    sb.append("    authtoken: ").append(toIndentedString(authtoken)).append("\n");
    sb.append("    serviceRequestId: ").append(toIndentedString(serviceRequestId)).append("\n");
    sb.append("    userid: ").append(toIndentedString(userid)).append("\n");
    sb.append("    serviceFlow: ").append(toIndentedString(serviceFlow)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
