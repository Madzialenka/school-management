package com.madzialenka.schoolmanagement.db.entity;

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
@Table(name = "schools", uniqueConstraints = @UniqueConstraint(columnNames = {"town", "school_number"}))
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "town", nullable = false)
    private String town;

    @Column(name = "school_number", nullable = false)
    private String schoolNumber;

    @OneToMany(mappedBy = "school")
    private List<SchoolSubject> subjects;

    @ManyToMany(mappedBy = "schools")
    private List<Student> students;
}
