package pl.zar.luxspringRESTJavaFXcommunication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Long> {

//    List<Course> getAllById(Long id);
//    Course getById(Long id);
//    Course getByTitle(String title);
//    void deleteById(long id);
//    //Course

}
