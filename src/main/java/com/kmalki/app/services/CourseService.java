package com.kmalki.app.services;

import com.kmalki.app.dtos.post.CoursePostDto;
import com.kmalki.app.entities.Course;
import com.kmalki.app.entities.Teacher;
import com.kmalki.app.exceptions.CourseNotFound;
import com.kmalki.app.exceptions.TeacherNotFound;
import com.kmalki.app.exceptions.TeacherNotQualified;
import com.kmalki.app.repositories.CourseRepository;
import com.kmalki.app.utils.EntityDtoConvertor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CourseService {

    @Autowired
    public final CourseRepository courseRepository;

    private final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private final EntityDtoConvertor entityDtoConvertor;

    @Autowired
    private final TeacherService teacherService;

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByTeacher(Long id) throws TeacherNotFound {
        if (teacherService.getTeacher(id) == null) {
            return null;
        }
        return courseRepository.findAllByTeacherId(id);
    }


    public Course getCourse(Long id) throws CourseNotFound {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFound(String.format("Course id %d not found", id), HttpStatus.NOT_FOUND));
    }

    public boolean checkTeacherAvailability(Course course, Teacher teacher) {
        return !courseRepository.findAnyCourseAlreadyMatchingByTeacherId(teacher.getId(),
                course.getBegin_date(), course.getEnd_date(), course.getClasse().getId()).isPresent();
    }


    public CoursePostDto createCourse(CoursePostDto coursePostDto) {
        Course course = entityDtoConvertor.convert(coursePostDto, Course.class);
        Teacher teacher = teacherService.getTeacher(course.getTeacher().getId());
        if (teacher.isQualifiedFor(course.getDiscipline())) {
            if (checkTeacherAvailability(course, teacher)) {
                course.setTeacher(teacher);
                course = courseRepository.save(course);
                return entityDtoConvertor.convert(course, CoursePostDto.class);
            }
        } else {
            throw new TeacherNotQualified(String.format("Teacher with id %d is not qualified for %s",
                    teacher.getId(), course.getDiscipline().name()));
        }
        return null;
    }


}
