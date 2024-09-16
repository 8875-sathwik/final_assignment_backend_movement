package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.InventoryItem;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * InventorySuccessResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")


public class InventorySuccessResponse   {
  @JsonProperty("serviceRequestId")
  private String serviceRequestId = null;

  @JsonProperty("serviceFlow")
  private String serviceFlow = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("inventoryItem")
  private InventoryItem inventoryItem = null;

  public InventorySuccessResponse serviceRequestId(String serviceRequestId) {
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

  public InventorySuccessResponse serviceFlow(String serviceFlow) {
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

  public InventorySuccessResponse status(String status) {
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

  public InventorySuccessResponse code(String code) {
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

  public InventorySuccessResponse message(String message) {
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

  public InventorySuccessResponse inventoryItem(InventoryItem inventoryItem) {
    this.inventoryItem = inventoryItem;
    return this;
  }

  /**
   * Get inventoryItem
   * @return inventoryItem
   **/
  @Schema(description = "")
  
    @Valid
    public InventoryItem getInventoryItem() {
    return inventoryItem;
  }

  public void setInventoryItem(InventoryItem inventoryItem) {
    this.inventoryItem = inventoryItem;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventorySuccessResponse inventorySuccessResponse = (InventorySuccessResponse) o;
    return Objects.equals(this.serviceRequestId, inventorySuccessResponse.serviceRequestId) &&
        Objects.equals(this.serviceFlow, inventorySuccessResponse.serviceFlow) &&
        Objects.equals(this.status, inventorySuccessResponse.status) &&
        Objects.equals(this.code, inventorySuccessResponse.code) &&
        Objects.equals(this.message, inventorySuccessResponse.message) &&
        Objects.equals(this.inventoryItem, inventorySuccessResponse.inventoryItem);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceRequestId, serviceFlow, status, code, message, inventoryItem);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventorySuccessResponse {\n");
    
    sb.append("    serviceRequestId: ").append(toIndentedString(serviceRequestId)).append("\n");
    sb.append("    serviceFlow: ").append(toIndentedString(serviceFlow)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    inventoryItem: ").append(toIndentedString(inventoryItem)).append("\n");
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
