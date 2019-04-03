package pl.zar.luxspringRESTJavaFXcommunication.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.ReviewDTO;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Mock
    private CourseService courseService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldUpdateReview() {
        //given
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setComment("Good");
        reviewDTO.setId(1L);

        //when
        Review reviewUpdated = reviewService.updateReview(reviewDTO);

        //then
        Assert.assertEquals("Good", reviewUpdated.getComment());
        Assert.assertEquals(reviewDTO.getId(), reviewUpdated.getId());

    }


    @Test
    public void exampleMockTest() {
        Mockito.when(courseService.getSingleCourse(1)).thenReturn(new Course(1L, "nazwa", new ArrayList<>()));
        Course course1 = new Course(1L, "nazwa", new ArrayList<>());
        Assert.assertEquals(course1.getTitle(), courseService.getSingleCourse(1).getTitle());
    }

    @Test
    public void shouldBeNotNull() {
        List<Review> allReviews = reviewService.getAllReviews();

        Assert.assertNotNull(allReviews);
    }

    @Test
    public void shouldBeNotEmpty() {
        List<Review> allReviews = reviewService.getAllReviews();

        Assert.assertFalse(allReviews.isEmpty());
    }

//    @Test
//    public void
}