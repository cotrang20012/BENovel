package mobile.model.payload.request.chapter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class DeleteChapterRequest {
    protected String url;
    protected int chapnumber;
}
