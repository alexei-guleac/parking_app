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
import com.swaggen.parking.client.model.DeviceInfo;
import com.swaggen.parking.client.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.processing.Generated;


/**
 * Registration user information. 
 */
@Schema(description = "Registration user information. ")
@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
public class RegistrationRequest {
  @JsonProperty("deviceInfo")
  private DeviceInfo deviceInfo = null;

  @JsonProperty("user")
  private User user = null;

  public RegistrationRequest deviceInfo(DeviceInfo deviceInfo) {
    this.deviceInfo = deviceInfo;
    return this;
  }

   /**
   * Get deviceInfo
   * @return deviceInfo
  **/
  @Schema(description = "")
  public DeviceInfo getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(DeviceInfo deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  public RegistrationRequest user(User user) {
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @Schema(required = true, description = "")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistrationRequest registrationRequest = (RegistrationRequest) o;
    return Objects.equals(this.deviceInfo, registrationRequest.deviceInfo) &&
        Objects.equals(this.user, registrationRequest.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceInfo, user);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegistrationRequest {\n");
    
    sb.append("    deviceInfo: ").append(toIndentedString(deviceInfo)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
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
