package com.openclassrooms.mddapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String email;

    private String name;

    @JsonProperty(value = "created_at")
    private String createdAt;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "updated_by")
    private String updatedBy;

    @JsonProperty(value = "updated_at")
    private String updatedAt;
}
