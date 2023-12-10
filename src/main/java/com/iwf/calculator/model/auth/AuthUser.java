package com.iwf.calculator.model.auth;

import com.iwf.calculator.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
