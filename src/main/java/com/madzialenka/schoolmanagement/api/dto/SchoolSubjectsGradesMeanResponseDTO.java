package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolSubjectsGradesMeanResponseDTO {
    private List<SchoolSubjectGradesMeanDTO> means;
}
