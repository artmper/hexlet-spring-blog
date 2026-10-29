package io.hexlet.blog.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
public class UserPatchDTO {
    @NotBlank
    @Size(min = 2, max = 30)
    private JsonNullable<String> firstName = JsonNullable.undefined();

    @NotBlank
    @Size(min = 2, max = 30)
    private JsonNullable<String> lastName = JsonNullable.undefined();

    private JsonNullable<LocalDate> birthday = JsonNullable.undefined();
}
