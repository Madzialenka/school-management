package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SchoolSimpleDataRequestDTO {

    @NotBlank(message = "Town can't be blank")
    private String town;

    @NotBlank(message = "School number can't be blank")
    private String schoolNumber;
}
