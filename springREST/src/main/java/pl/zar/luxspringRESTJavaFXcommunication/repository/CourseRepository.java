package pl.zar.luxspringRESTJavaFXcommunication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course getById(Long id);


}
