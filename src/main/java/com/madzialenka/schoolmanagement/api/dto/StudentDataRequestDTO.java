package com.madzialenka.schoolmanagement.api.dto;

import com.madzialenka.schoolmanagement.common.Gender;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StudentDataRequestDTO {
    private String surname;
    private String name;
    private String email;
    private String pesel;
    private Gender gender;
    private List<Long> schoolIds;
}
