package com.iwf.calculator.model.dto.auth;

import com.iwf.calculator.model.entity.User;

import java.time.LocalDateTime;

public class UserAuth {

    private Long id;
    private String username;
    private String fullName;
    private LocalDateTime createdAt;

    public static UserAuth fromUser(User entity) {
        var viewDto = new UserAuth();
        viewDto.id = entity.getId();
        viewDto.username = entity.getUsername();
        viewDto.fullName = entity.getFullName();
        viewDto.createdAt = entity.getCreatedAt();
        return viewDto;
    }
}
