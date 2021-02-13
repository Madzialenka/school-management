package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolSimpleResponseDTO {
    private Long id;
    private String town;
    private String schoolNumber;
}
