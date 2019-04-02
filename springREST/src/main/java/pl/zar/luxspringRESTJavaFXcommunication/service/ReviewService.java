package pl.zar.luxspringRESTJavaFXcommunication.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.zar.luxspringRESTJavaFXcommunication.DTO.ReviewDTO;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;
import pl.zar.luxspringRESTJavaFXcommunication.repository.ReviewRepository;

import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setComment(reviewDTO.getComment());
        review.setId(reviewDTO.getId());
        return reviewRepository.save(review);

    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }


    public Review getSingleReview(long id) {
        return reviewRepository.findById(id);
    }

    public void deleteById(long id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setComment(reviewDTO.getComment());
        return reviewRepository.save(review);
    }


}
