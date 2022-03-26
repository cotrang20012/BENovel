package mobile.model.payload.request.comment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateCommentRequest {
    protected String commentId;
    protected String content;
}
