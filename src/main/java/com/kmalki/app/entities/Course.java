package com.kmalki.app.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name = "Course")
@Table(name = "Course")
public class Course {

    @Id
    @SequenceGenerator(name = "course_id_seq",
            sequenceName = "course_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "course_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "discipline", updatable = false)
    @NonNull
    private Discipline discipline;

    @Column(name = "begin_date")
    @NonNull
    private LocalDateTime begin_date;

    @Column(name = "end_date")
    @NonNull
    private LocalDateTime end_date;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @NonNull
    private Classe classe;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @NonNull
    private Teacher teacher;

}
