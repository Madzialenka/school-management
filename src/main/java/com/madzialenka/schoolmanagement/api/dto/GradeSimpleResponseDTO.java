package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GradeSimpleResponseDTO {
    private Long id;
    private Short value;
    private StudentBasicDataResponseDTO student;
}
