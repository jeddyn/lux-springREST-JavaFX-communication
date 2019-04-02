package pl.zar.luxspringRESTJavaFXcommunication.staticdataservis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;
import pl.zar.luxspringRESTJavaFXcommunication.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaticDataOfCourses {

    @Autowired
    private CourseRepository courseRepository;

    public void addStaticResources() {
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
    }
}
