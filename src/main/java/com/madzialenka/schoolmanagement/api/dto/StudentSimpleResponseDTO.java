package com.madzialenka.schoolmanagement.api.dto;

import com.madzialenka.schoolmanagement.common.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StudentSimpleResponseDTO {
    private Long id;
    private String surname;
    private String name;
    private String email;
    private String pesel;
    private Gender gender;
}
