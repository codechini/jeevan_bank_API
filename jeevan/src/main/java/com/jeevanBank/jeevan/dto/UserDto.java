package com.jeevanBank.jeevan.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long user_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
    private String phone_number;
    private String address;
    private Double balance;
    private String account_number;
    private Timestamp create_at;
    private Timestamp update_at;
    private Timestamp deleted_at;
}
