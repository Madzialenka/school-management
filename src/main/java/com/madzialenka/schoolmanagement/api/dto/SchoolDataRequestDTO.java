package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolDataRequestDTO {

    @NotBlank(message = "Town can't be blank")
    private String town;

    @NotBlank(message = "School number can't be blank")
    private String schoolNumber;

    private List<SchoolSubjectDataRequestDTO> subjects;
}
