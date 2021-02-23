package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolSubjectDataRequestDTO {

    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotBlank(message = "Teacher's name can't be blank")
    private String teacherName;
}
