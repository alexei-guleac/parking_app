package com.isd.parking.web.rest.payload.account.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "User authentication with social service provider. ")
public class SocialAuthRequest {

    @JsonProperty()
    @JsonAlias({"id"})
    @ApiModelProperty(notes = "Social provider user ID", required = true)
    @NotBlank
    @NonNull
    private String id;

    @JsonProperty()
    @JsonAlias({"socialProvider"})
    @ApiModelProperty(notes = "Social provider short name", required = true)
    @NotBlank
    @NonNull
    private String socialProvider;
}
