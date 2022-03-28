package mobile.model.payload.request.reading;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReadingRequest {
    protected int chapnumber;
    protected String url;

}
