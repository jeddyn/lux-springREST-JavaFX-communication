package pl.zar.luxspringRESTJavaFXcommunication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.CourseDTO;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.ReviewDTO;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;
import pl.zar.luxspringRESTJavaFXcommunication.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @GetMapping("/review")
    public ResponseEntity addAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        log.info("Pobranie wszystkich opinii: {}" + reviews.toString());
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity getSingleReview(@PathVariable("id") long id) {
        log.info("Pobranie opinii z Id: {}", id);
        return new ResponseEntity<>(reviewService.getSingleReview(id), HttpStatus.OK);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity deleteReview(@PathVariable("id") long id) {
        log.info("Usuwanięcie opinii z Id: {}" + id);
        reviewService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/review")
    public ResponseEntity addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("Dodanie opinii: {}" + reviewDTO);
        reviewService.addReview(reviewDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/review")
    public ReviewDTO updateReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        log.info("Edytowanie review {}" + reviewDTO);
        Review review = reviewService.updateReview(reviewDTO);
        reviewDTO.setId(review.getId());
        return reviewDTO;
    }
}
