package com.madzialenka.schoolmanagement.db.entity;

import com.madzialenka.schoolmanagement.common.Gender;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "pesel", nullable = false, unique = true)
    private String pesel;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @OneToMany(mappedBy = "student", cascade = {CascadeType.REMOVE})
    private List<Grade> grades;

    @ManyToMany
    @JoinTable(
        name = "students_schools",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "school_id"))
    private List<School> schools;
}
