package pl.zar.luxspringRESTJavaFXcommunication.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="course")
@Getter
@Setter
@ToString
public class Course {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="title")
	private String title;

	@OneToMany(fetch = FetchType.LAZY,
			   cascade = CascadeType.PERSIST)
	@JoinColumn(name = "course_id")
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Review> review;
	
	public Course() {}

	public void setReview(Review review){
		this.review.add(review);
	}
	//@JsonProperty("review")
	public void setReview(List<Review> review){
		this.review = review;
	}
}
