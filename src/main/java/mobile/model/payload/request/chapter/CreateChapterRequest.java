package mobile.model.payload.request.chapter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CreateChapterRequest {
    protected String tenchap;
    protected String content;
    protected String url;
}
