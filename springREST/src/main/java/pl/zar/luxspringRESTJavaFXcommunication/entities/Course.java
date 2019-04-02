package pl.zar.luxspringRESTJavaFXcommunication.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
			   cascade = CascadeType.ALL)
	@JoinColumn(name = "course_id")
	@JsonProperty("review")
	private List<Review> review;

}
