package pl.zar.luxspringRESTJavaFXcommunication.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.CourseDTO;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.ReviewDTO;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseServiceTest {

    @Autowired
    CourseService courseService;

    @Autowired
    ReviewService reviewService;

    @Test
    public void shouldCreateReviewWhenCreateCourse() {

        ReviewDTO review = new ReviewDTO();
        review.setComment("costam");
        List<ReviewDTO> lista = new ArrayList<>();
        lista.add(review);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("test");
        courseDTO.setReview(lista);

        //when
        Course course = courseService.addCourse(courseDTO);

        Assert.assertEquals(review.getComment(), course.getReview().get(0).getComment());
    }

}