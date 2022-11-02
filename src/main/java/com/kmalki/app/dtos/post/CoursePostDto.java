package com.kmalki.app.dtos.post;

import com.kmalki.app.entities.Discipline;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoursePostDto {

    private Long id;
    private Discipline discipline;
    private LocalDateTime begin_date;
    private LocalDateTime end_date;
    private Long classeId;
    private Long teacherId;
    
}

