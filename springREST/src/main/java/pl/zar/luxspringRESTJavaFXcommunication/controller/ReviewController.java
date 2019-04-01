package pl.zar.luxspringRESTJavaFXcommunication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;
import pl.zar.luxspringRESTJavaFXcommunication.repository.ReviewRepository;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @PostMapping("/review")
    public ResponseEntity addReview(@RequestBody Review reviewDTO) {//sec
        reviewRepository.save(reviewDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/review")
    public ResponseEntity addAllReviews() {
        List<Review> a = reviewRepository.findAll();
        return new ResponseEntity<>(a, HttpStatus.OK);
    }
}
