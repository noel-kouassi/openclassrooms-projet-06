package com.openclassrooms.mddapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {

    private long topidId;

    private String title;

    private String description;

    private Set<UserDto> users;
}
