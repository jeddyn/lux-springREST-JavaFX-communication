package pl.zar.luxspringRESTJavaFXcommunication.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.zar.luxspringRESTJavaFXcommunication.service.CourseService;
import pl.zar.luxspringRESTJavaFXcommunication.service.ReviewService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewControllerTest {

    @Autowired
    private ReviewService reviewService;

    @Mock
    private CourseService courseService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldUpdateAndReturnDto() {

    }
}