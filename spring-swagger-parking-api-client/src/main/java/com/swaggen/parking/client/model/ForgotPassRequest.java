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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.processing.Generated;
import java.util.Objects;


/**
 * Forgot user password request.
 */
@Schema(description = "Forgot user password request. ")
@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
public class ForgotPassRequest {
    @JsonProperty("deviceInfo")
    private DeviceInfo deviceInfo = null;

    @JsonProperty("email")
    private String email = null;

    public ForgotPassRequest deviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    /**
     * Get deviceInfo
     *
     * @return deviceInfo
     **/
    @Schema(description = "")
    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public ForgotPassRequest email(String email) {
        this.email = email;
        return this;
    }

    /**
     * User email.
     *
     * @return email
     **/
    @Schema(required = true, description = "User email. ")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForgotPassRequest forgotPassRequest = (ForgotPassRequest) o;
        return Objects.equals(this.deviceInfo, forgotPassRequest.deviceInfo) &&
                Objects.equals(this.email, forgotPassRequest.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceInfo, email);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ForgotPassRequest {\n");

        sb.append("    deviceInfo: ").append(toIndentedString(deviceInfo)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
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