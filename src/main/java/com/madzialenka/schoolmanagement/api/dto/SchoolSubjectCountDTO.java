package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolSubjectCountDTO {
    private String subjectName;
    private Long count;
}
