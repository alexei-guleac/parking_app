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
 * Request for user password reset.
 */
@Schema(description = "Request for user password reset. ")
@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
public class ResetPasswordRequest {
    @JsonProperty("deviceInfo")
    private DeviceInfo deviceInfo = null;

    @JsonProperty("resetDetails")
    private ResetDetails resetDetails = null;

    public ResetPasswordRequest deviceInfo(DeviceInfo deviceInfo) {
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

    public ResetPasswordRequest resetDetails(ResetDetails resetDetails) {
        this.resetDetails = resetDetails;
        return this;
    }

    /**
     * Get resetDetails
     *
     * @return resetDetails
     **/
    @Schema(required = true, description = "")
    public ResetDetails getResetDetails() {
        return resetDetails;
    }

    public void setResetDetails(ResetDetails resetDetails) {
        this.resetDetails = resetDetails;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResetPasswordRequest resetPasswordRequest = (ResetPasswordRequest) o;
        return Objects.equals(this.deviceInfo, resetPasswordRequest.deviceInfo) &&
                Objects.equals(this.resetDetails, resetPasswordRequest.resetDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceInfo, resetDetails);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResetPasswordRequest {\n");

        sb.append("    deviceInfo: ").append(toIndentedString(deviceInfo)).append("\n");
        sb.append("    resetDetails: ").append(toIndentedString(resetDetails)).append("\n");
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