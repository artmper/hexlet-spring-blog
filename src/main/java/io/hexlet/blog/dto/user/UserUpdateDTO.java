package io.hexlet.blog.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateDTO {

    @NotBlank
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов")
    private String lastName;

    private LocalDate birthday;
}
