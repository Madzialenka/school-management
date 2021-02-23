package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolSubjectGradesMeanDTO {
    private Long schoolSubjectId;
    private Double mean;
}
