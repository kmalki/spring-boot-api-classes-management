package com.kmalki.app.repositories;

import com.kmalki.app.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findAll();

    Optional<Teacher> findById(Long id);

    Optional<Teacher> findByEmail(String email);

    @Query(value = "Select * from teacher where id in (select teacher_id from teacher_discipline where discipline = ?1)",
            nativeQuery = true)
    List<Teacher> findAllByDiscipline(int discipline);

}
