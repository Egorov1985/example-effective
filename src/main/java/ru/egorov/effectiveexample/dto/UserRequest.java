package ru.egorov.effectiveexample.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRequest extends UserDto{
    private String email;
    private String phone;
}
