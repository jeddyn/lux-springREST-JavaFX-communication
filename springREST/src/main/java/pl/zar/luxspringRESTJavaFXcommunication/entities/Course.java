package pl.zar.luxspringRESTJavaFXcommunication.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;



@Getter
@Setter
@ToString
@Entity
@Table(name="course")
public class Course {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	@JsonProperty("id")
	private Long id;

	@Column(name="title")
	@JsonProperty("title")
	private String title;

	@OneToMany(fetch = FetchType.LAZY,
			   cascade = CascadeType.PERSIST)
	@JoinColumn(name = "course_id")
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonProperty("review")
	private List<Review> review;
	
	public Course() {}

	public void setReview2(Review review){
		this.review.add(review);
	}
	//@JsonProperty("review")
	public void setReview(List<Review> review){
		this.review = review;
	}
}
