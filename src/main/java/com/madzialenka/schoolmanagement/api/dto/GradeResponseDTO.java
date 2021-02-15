package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GradeResponseDTO {
    private Long id;
    private Short value;
    private StudentSimpleResponseDTO student;
    private SchoolSubjectResponseDTO schoolSubject;
}
