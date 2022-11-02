package com.kmalki.app.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Table(name = "Teacher", uniqueConstraints = {
        @UniqueConstraint(name = "teacher_email_unique", columnNames = "email")
})
@Entity(name = "Teacher")
public class Teacher {
    @Id
    @SequenceGenerator(name = "teacher_id_seq",
            sequenceName = "teacher_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "teacher_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "first_name", nullable = false)
    @NonNull
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @NonNull
    private String lastName;
    @Column(name = "email", nullable = false)
    @NonNull
    private String email;
    @ElementCollection
    @CollectionTable(
            name = "teacher_discipline",
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(name = "pair_teacher_discipline_unique", columnNames = {"teacher_id", "discipline"})
    )
    @Column(name = "discipline")
    @NonNull
    private Set<Discipline> disciplines;

    public boolean isQualifiedFor(Discipline discipline) {
        return disciplines.contains(discipline);
    }

}
