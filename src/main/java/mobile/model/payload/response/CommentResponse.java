package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobile.model.Entity.Comment;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    protected String id;
    protected String parentId;
    protected List<CommentResponse> listChild;
    protected String urltruyen;
    protected String tenhienthi;
    protected String username;
    protected String image;
    protected int numChild;
    protected int depth;
    protected String content;
    protected Date date;
}
