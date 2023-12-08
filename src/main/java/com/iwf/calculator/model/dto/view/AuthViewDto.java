package com.iwf.calculator.model.dto.view;

import com.iwf.calculator.model.dto.auth.UserAuth;
import com.iwf.calculator.model.entity.User;
import lombok.Data;

@Data
public class AuthViewDto {

    private UserAuth user;
    private String token;

    public static AuthViewDto create(UserAuth user, String token) {
        var viewDto = new AuthViewDto();
        viewDto.user = user;
        viewDto.token = token;
        return viewDto;
    }
}
