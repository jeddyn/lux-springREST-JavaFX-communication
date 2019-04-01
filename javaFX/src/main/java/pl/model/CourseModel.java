package pl.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CourseModel {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("review")
    private List<ReviewModel> review;

    //@JsonProperty("comment")
    public void setComment(String comment){
        if(this.review == null){
            review = new ArrayList<>();
        }
        review.add(new ReviewModel(comment));
    }

}
