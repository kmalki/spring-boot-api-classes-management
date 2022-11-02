package com.kmalki.app.repositories;

import com.kmalki.app.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAll();

    Optional<Course> findById(Long id);


    @Query(value = "select * from course where teacher_id = ?1", nativeQuery = true)
    List<Course> findAllByTeacherId(Long id);

    @Query(value = "select * from course where (teacher_id = ?1 and ((begin_date between ?2 and ?3) or (end_date between ?2 and ?3) or (?2 between begin_date and end_date) or (?3 between begin_date and end_date))) or (classe_id = ?4 and ((begin_date between ?2 and ?3) or (end_date between ?2 and ?3) or (?2 between begin_date and end_date) or (?3 between begin_date and end_date))) limit 1", nativeQuery = true)
    Optional<Course> findAnyCourseAlreadyMatchingByTeacherId(Long teacherId, LocalDateTime beginDate, LocalDateTime endDate, Long classeId);
}
