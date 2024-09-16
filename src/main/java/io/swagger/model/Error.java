package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.ErrorErrors;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Error
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")


public class Error   {
  @JsonProperty("errors")
  @Valid
  private List<ErrorErrors> errors = null;

  @JsonProperty("errorCode")
  private String errorCode = null;

  @JsonProperty("errorUserMsg")
  private String errorUserMsg = null;

  @JsonProperty("step")
  private String step = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("serviceRequestId")
  private String serviceRequestId = null;

  @JsonProperty("httpErrorCode")
  private String httpErrorCode = null;

  @JsonProperty("serviceFlow")
  private String serviceFlow = null;

  public Error errors(List<ErrorErrors> errors) {
    this.errors = errors;
    return this;
  }

  public Error addErrorsItem(ErrorErrors errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<ErrorErrors>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Array of errors
   * @return errors
   **/
  @Schema(description = "Array of errors")
      @Valid
    public List<ErrorErrors> getErrors() {
    return errors;
  }

  public void setErrors(List<ErrorErrors> errors) {
    this.errors = errors;
  }

  public Error errorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  /**
   * Error code
   * @return errorCode
   **/
  @Schema(example = "errorCode", description = "Error code")
  
    public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public Error errorUserMsg(String errorUserMsg) {
    this.errorUserMsg = errorUserMsg;
    return this;
  }

  /**
   * User-friendly error message
   * @return errorUserMsg
   **/
  @Schema(example = "errorUserMsg in string", description = "User-friendly error message")
  
    public String getErrorUserMsg() {
    return errorUserMsg;
  }

  public void setErrorUserMsg(String errorUserMsg) {
    this.errorUserMsg = errorUserMsg;
  }

  public Error step(String step) {
    this.step = step;
    return this;
  }

  /**
   * Step in the process where the error occurred
   * @return step
   **/
  @Schema(example = "step in string", description = "Step in the process where the error occurred")
  
    public String getStep() {
    return step;
  }

  public void setStep(String step) {
    this.step = step;
  }

  public Error status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Status of the operation
   * @return status
   **/
  @Schema(example = "FAILED", description = "Status of the operation")
  
    public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Error serviceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
    return this;
  }

  /**
   * Service request ID
   * @return serviceRequestId
   **/
  @Schema(example = "string", description = "Service request ID")
  
    public String getServiceRequestId() {
    return serviceRequestId;
  }

  public void setServiceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
  }

  public Error httpErrorCode(String httpErrorCode) {
    this.httpErrorCode = httpErrorCode;
    return this;
  }

  /**
   * HTTP error code
   * @return httpErrorCode
   **/
  @Schema(example = "400", description = "HTTP error code")
  
    public String getHttpErrorCode() {
    return httpErrorCode;
  }

  public void setHttpErrorCode(String httpErrorCode) {
    this.httpErrorCode = httpErrorCode;
  }

  public Error serviceFlow(String serviceFlow) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Error error = (Error) o;
    return Objects.equals(this.errors, error.errors) &&
        Objects.equals(this.errorCode, error.errorCode) &&
        Objects.equals(this.errorUserMsg, error.errorUserMsg) &&
        Objects.equals(this.step, error.step) &&
        Objects.equals(this.status, error.status) &&
        Objects.equals(this.serviceRequestId, error.serviceRequestId) &&
        Objects.equals(this.httpErrorCode, error.httpErrorCode) &&
        Objects.equals(this.serviceFlow, error.serviceFlow);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errors, errorCode, errorUserMsg, step, status, serviceRequestId, httpErrorCode, serviceFlow);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");
    
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    errorUserMsg: ").append(toIndentedString(errorUserMsg)).append("\n");
    sb.append("    step: ").append(toIndentedString(step)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    serviceRequestId: ").append(toIndentedString(serviceRequestId)).append("\n");
    sb.append("    httpErrorCode: ").append(toIndentedString(httpErrorCode)).append("\n");
    sb.append("    serviceFlow: ").append(toIndentedString(serviceFlow)).append("\n");
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
