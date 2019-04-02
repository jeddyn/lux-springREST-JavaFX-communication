package pl.zar.luxspringRESTJavaFXcommunication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.CourseDTO;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.ReviewDTO;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;
import pl.zar.luxspringRESTJavaFXcommunication.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getSingleCourse(long id) {
        return courseRepository.getById(id);
    }

    public Course addCourse(@RequestBody CourseDTO courseDTO) {
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());

        course.setReview(convertReviewDtoToReview(courseDTO));
        course.setId(courseDTO.getId());
        return courseRepository.save(course);

    }


    public void deleteCourse(long id) {
        courseRepository.deleteById(id);
    }

    public Course updateCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setTitle(courseDTO.getTitle());
        course.setReview(convertReviewDtoToReview(courseDTO));

        return courseRepository.save(course);
    }

    private List<Review> convertReviewDtoToReview(CourseDTO courseDTO) {
        List<Review> reviews = new ArrayList<>();
        for (ReviewDTO r : courseDTO.getReview()) {
            reviews.add(new Review(r.getId(), r.getComment()));
        }
        return reviews;
    }



}
