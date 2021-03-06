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
 * Authentication user details.
 */
@Schema(description = "Authentication user details. ")
@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
public class AuthDetails {
    @JsonProperty("password")
    private String password = null;

    @JsonProperty("username")
    private String username = null;

    public AuthDetails password(String password) {
        this.password = password;
        return this;
    }

    /**
     * User password
     *
     * @return password
     **/
    @Schema(required = true, description = "User password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthDetails username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Username
     *
     * @return username
     **/
    @Schema(required = true, description = "Username")
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
        AuthDetails authDetails = (AuthDetails) o;
        return Objects.equals(this.password, authDetails.password) &&
                Objects.equals(this.username, authDetails.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthDetails {\n");

        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
