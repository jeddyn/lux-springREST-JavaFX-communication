package pl.zar.luxspringRESTJavaFXcommunication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;
import pl.zar.luxspringRESTJavaFXcommunication.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class CourseController {
    @Autowired
    CourseRepository courseRepository;


    @GetMapping("/course")
    public ResponseEntity<List<Course>> getCourses(){
        List<Course> allCourses = courseRepository.findAll();
        //List <OutDto> responseList = new ArrayList<>();
//        for (Course item : allCourses){
//            responseList.add(new OutDto(item.getId(),item.getTitle(), item.getReview().getComment()));
//        }
        return new ResponseEntity<>(allCourses, HttpStatus.OK);
    }

    @PostMapping("/course")
    public ResponseEntity addCourse(@RequestBody Course course) {

        courseRepository.save(course);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/addAllCoursesWithReviews")
    public ResponseEntity addAllReviews() {
        List<Review> lista = new ArrayList<>();
        Review review = new Review();
        review.setComment("Bardzo dobry kurs polecam!");
        lista.add(review);

        review = new Review();
        review.setComment("Taki sobie");
        lista.add(review);

        review = new Review();
        review.setComment("Merytoryczny");
        lista.add(review);

        Course course = new Course();
        course.setTitle("Programowanie w .NET");
        course.setReview(lista);
        courseRepository.save(course);


        List<Review> lista2 = new ArrayList<>();
        Review review2 = new Review();
        review2.setComment("Widziałem lepsze");
        lista2.add(review2);

        review2 = new Review();
        review2.setComment("Polecam!");
        lista2.add(review2);

        review2 = new Review();
        review2.setComment("Wart swojej ceny!");
        lista2.add(review2);

        Course course2 = new Course();
        course2.setTitle("Programowanie w Javie");
        course2.setReview(lista2);
        courseRepository.save(course2);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity deleteCourse(@PathVariable("id") long id){
        courseRepository.deleteById(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}
