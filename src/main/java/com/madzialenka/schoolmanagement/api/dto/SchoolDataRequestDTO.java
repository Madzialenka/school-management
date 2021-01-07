package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolDataRequestDTO {
    private String town;
    private String schoolNumber;
    private List<SchoolSubjectDataRequestDTO> subjects;
}
