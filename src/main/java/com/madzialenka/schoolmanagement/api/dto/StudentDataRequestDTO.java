package com.madzialenka.schoolmanagement.api.dto;

import com.madzialenka.schoolmanagement.common.Gender;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StudentDataRequestDTO {

    @NotBlank(message = "Surname can't be blank")
    private String surname;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotBlank(message = "Email can't be blank")
    private String email;

    @NotBlank(message = "Pesel can't be blank")
    private String pesel;

    @NotNull(message = "Gender can't be null")
    private Gender gender;

    private List<Long> schoolIds;
}
