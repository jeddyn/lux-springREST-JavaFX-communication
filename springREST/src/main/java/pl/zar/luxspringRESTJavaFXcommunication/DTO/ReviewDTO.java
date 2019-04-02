package pl.zar.luxspringRESTJavaFXcommunication.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("comment")
    private String comment;
}
