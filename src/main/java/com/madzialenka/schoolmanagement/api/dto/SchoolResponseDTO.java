package com.madzialenka.schoolmanagement.api.dto;

import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolResponseDTO {
    private Long id;
    private String town;
    private String schoolNumber;
    private List<SchoolSubjectResponseDTO> subjects;
}
