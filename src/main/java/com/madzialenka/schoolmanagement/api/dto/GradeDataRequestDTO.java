package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GradeDataRequestDTO {

    @Min(value = 1, message = "Value has to be higher or equal to 1")
    @Max(value = 5, message = "Value has to be lower or equal to 5")
    @NotNull(message = "Value can't be null")
    private Short value;

    @NotNull(message = "Student ID can't be null")
    private Long studentId;
}
