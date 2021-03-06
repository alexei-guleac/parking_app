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
 * User authentication with social service provider.
 */
@Schema(description = "User authentication with social service provider. ")
@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
public class SocialAuthRequest {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("socialProvider")
    private String socialProvider = null;

    public SocialAuthRequest id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Social provider user ID
     *
     * @return id
     **/
    @Schema(required = true, description = "Social provider user ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SocialAuthRequest socialProvider(String socialProvider) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocialAuthRequest socialAuthRequest = (SocialAuthRequest) o;
        return Objects.equals(this.id, socialAuthRequest.id) &&
                Objects.equals(this.socialProvider, socialAuthRequest.socialProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, socialProvider);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SocialAuthRequest {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    socialProvider: ").append(toIndentedString(socialProvider)).append("\n");
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
