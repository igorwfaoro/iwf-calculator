package com.iwf.calculator.model.auth;

import com.iwf.calculator.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthUser {

    private Long id;
    private String username;
    private String fullName;
    private LocalDateTime createdAt;

    public static AuthUser fromEntity(User entity) {
        var viewDto = new AuthUser();
        viewDto.id = entity.getId();
        viewDto.username = entity.getUsername();
        viewDto.fullName = entity.getFullName();
        viewDto.createdAt = entity.getCreatedAt();
        return viewDto;
    }

}
