package pl.zar.luxspringRESTJavaFXcommunication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.CourseDTO;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;
import pl.zar.luxspringRESTJavaFXcommunication.service.CourseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("/course")
    public ResponseEntity<List<Course>> getCourses(HttpServletRequest httpServletRequest) {
        List<Course> listOfCourses = courseService.getAllCourses();
        log.info("USER: ", httpServletRequest.getRemoteUser());
        log.info("Pobranie kursów: {}" + listOfCourses.toString());
        return new ResponseEntity<>(listOfCourses, HttpStatus.OK);
    }

    @PostMapping("/course")
    public ResponseEntity addCourse(@Valid @RequestBody CourseDTO courseDTO, HttpServletRequest httpServletRequest) {
        log.info("USER: ", httpServletRequest.getRemoteUser());
        log.info("Dodanie kursu: " + courseDTO);
        Course course = courseService.addCourse(courseDTO);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }


    @DeleteMapping("/course/{id}")
    public ResponseEntity deleteCourse(@PathVariable("id") long id) {
        log.info("Usuwanięcie kursu o id: {}" + id);
        courseService.deleteCourse(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PutMapping("/course")
    public ResponseEntity updateCourse(@Valid @RequestBody CourseDTO courseDTO) {
        log.info("Aktualizacja kurs {}", courseDTO.toString());
        Course course = courseService.updateCourse(courseDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity getById(@PathVariable("id") long id) {
        Course course = courseService.getSingleCourse(id);
        log.info("Pobranie kurs o id: {}" + id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }


}
