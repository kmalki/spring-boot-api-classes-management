package com.kmalki.app.entities;

import lombok.*;

import javax.persistence.*;


@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "Class", uniqueConstraints = {
        @UniqueConstraint(name = "class_unique_name", columnNames = "name")
})
@Entity(name = "Class")
public class Classe {
    @Id
    @SequenceGenerator(name = "class_id_seq",
            sequenceName = "class_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "class_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    @NonNull
    private String name;
}
