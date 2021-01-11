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
@Table(name = "school_subjects", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "school_id"}))
public class SchoolSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @OneToMany(mappedBy = "schoolSubject", cascade = {CascadeType.REMOVE})
    private List<Grade> grade;
}
