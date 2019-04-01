package pl.zar.luxspringRESTJavaFXcommunication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Review;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

//    List<Review> getAllById(Long id);
//    Review getById(long id);
//    void deleteById(long id);
}
