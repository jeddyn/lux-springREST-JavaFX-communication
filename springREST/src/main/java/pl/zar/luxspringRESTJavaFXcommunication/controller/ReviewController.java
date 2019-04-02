package pl.zar.luxspringRESTJavaFXcommunication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.ReviewDTO;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;
import pl.zar.luxspringRESTJavaFXcommunication.service.ReviewService;

import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @GetMapping("/review")
    public ResponseEntity addAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity getSingleReview(@PathVariable("id") long id) {
        log.info("Pobieranie Opinii z Id: {}", id);
        return new ResponseEntity<>(reviewService.getSingleReview(id), HttpStatus.OK);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity deleteReview(@PathVariable("id") long id) {
        reviewService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/review")
    public ResponseEntity addReview(@Valid @RequestBody ReviewDTO reviewDTO) {//sec
        reviewService.addReview(reviewDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/review")
    public ReviewDTO updateReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        Review review = reviewService.updateReview(reviewDTO);
        reviewDTO.setId(review.getId());
        return reviewDTO;
    }
}
