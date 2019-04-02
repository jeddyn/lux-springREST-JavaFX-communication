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
    public ResponseEntity<List<Course>> getCourses() {
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @PostMapping("/course")
    public ResponseEntity addCourse(@Valid @RequestBody CourseDTO courseDTO, HttpServletRequest httpServletRequest) {
        log.info("USER: ", httpServletRequest.getRemoteUser());
        Course course = courseService.addCourse(courseDTO);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }


    @DeleteMapping("/course/{id}")
    public ResponseEntity deleteCourse(@PathVariable("id") long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PutMapping("/course")
    public ResponseEntity updateCourse(@Valid @RequestBody CourseDTO courseDTO) {
        log.info("DTO {}", courseDTO.toString());
        Course course = courseService.updateCourse(courseDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity getById(@PathVariable("id") long id) {
        Course course = courseService.getSingleCourse(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }


}
