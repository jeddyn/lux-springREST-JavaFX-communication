package pl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewModel {


    @JsonProperty("id")
    @JsonIgnoreProperties
    private Long id;

    @JsonProperty("comment")
    private String comment;

    public ReviewModel(String comment){
        this.comment = comment;
    }
}
