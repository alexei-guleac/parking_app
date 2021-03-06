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
 * Social provider profile disconnection request.
 */
@Schema(description = "Social provider profile disconnection request. ")
@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
public class SocialDisconnectRequest {
    @JsonProperty("socialProvider")
    private String socialProvider = null;

    @JsonProperty("username")
    private String username = null;

    public SocialDisconnectRequest socialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
        return this;
    }

    /**
     * Social provider short name
     *
     * @return socialProvider
     **/
    @Schema(required = true, description = "Social provider short name")
    public String getSocialProvider() {
        return socialProvider;
    }

    public void setSocialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
    }

    public SocialDisconnectRequest username(String username) {
        this.username = username;
        return this;
    }

    /**
     * User username
     *
     * @return username
     **/
    @Schema(required = true, description = "User username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocialDisconnectRequest socialDisconnectRequest = (SocialDisconnectRequest) o;
        return Objects.equals(this.socialProvider, socialDisconnectRequest.socialProvider) &&
                Objects.equals(this.username, socialDisconnectRequest.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socialProvider, username);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SocialDisconnectRequest {\n");

        sb.append("    socialProvider: ").append(toIndentedString(socialProvider)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
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
