package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.InventoryItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * InventoryItemList
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-23T10:15:39.378212833Z[GMT]")


public class InventoryItemList   {
  @JsonProperty("inventory")
  @Valid
  private List<InventoryItem> inventory = null;

  public InventoryItemList inventory(List<InventoryItem> inventory) {
    this.inventory = inventory;
    return this;
  }

  public InventoryItemList addInventoryItem(InventoryItem inventoryItem) {
    if (this.inventory == null) {
      this.inventory = new ArrayList<InventoryItem>();
    }
    this.inventory.add(inventoryItem);
    return this;
  }

  /**
   * Get inventory
   * @return inventory
   **/
  @Schema(description = "")
      @Valid
    public List<InventoryItem> getInventory() {
    return inventory;
  }

  public void setInventory(List<InventoryItem> inventory) {
    this.inventory = inventory;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryItemList inventoryItemList = (InventoryItemList) o;
    return Objects.equals(this.inventory, inventoryItemList.inventory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inventory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryItemList {\n");
    
    sb.append("    inventory: ").append(toIndentedString(inventory)).append("\n");
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
