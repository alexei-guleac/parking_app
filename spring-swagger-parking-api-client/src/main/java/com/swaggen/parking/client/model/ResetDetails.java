/*
 * Parking application Backend API
 * Parking IoT application project API reference for developers
 *
 * OpenAPI spec version: 0.2
 * Contact: parking_app_service@gmail.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.swaggen.parking.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.processing.Generated;


/**
 * User password reset details. 
 */
@Schema(description = "User password reset details. ")
@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
public class ResetDetails {
  @JsonProperty("confirmationToken")
  private String confirmationToken = null;

  @JsonProperty("password")
  private String password = null;

  public ResetDetails confirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
    return this;
  }

   /**
   * Confirmation token for verification. 
   * @return confirmationToken
  **/
  @Schema(required = true, description = "Confirmation token for verification. ")
  public String getConfirmationToken() {
    return confirmationToken;
  }

  public void setConfirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
  }

  public ResetDetails password(String password) {
    this.password = password;
    return this;
  }

   /**
   * New password. 
   * @return password
  **/
  @Schema(required = true, description = "New password. ")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetDetails resetDetails = (ResetDetails) o;
    return Objects.equals(this.confirmationToken, resetDetails.confirmationToken) &&
        Objects.equals(this.password, resetDetails.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(confirmationToken, password);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetDetails {\n");
    
    sb.append("    confirmationToken: ").append(toIndentedString(confirmationToken)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
