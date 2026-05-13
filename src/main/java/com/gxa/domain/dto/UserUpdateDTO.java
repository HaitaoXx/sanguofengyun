package com.gxa.domain.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private String oldPassword;
    private String newPassword;
}