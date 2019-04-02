package pl.zar.luxspringRESTJavaFXcommunication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    Review findById(long id);

}
