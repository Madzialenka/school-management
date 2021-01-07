package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolSubjectDataRequestDTO {
    private String name;
    private String teacherName;
}
