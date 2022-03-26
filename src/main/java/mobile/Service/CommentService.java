package mobile.Service;

import mobile.model.Entity.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    void AddComment(Comment comment);
    List<Comment> findByUrl(String url, Pageable pageable);
    List<Comment> findByIdParent(String id, Pageable pageable);
    Comment UpdateComment(Comment comment, String newcomment);
    Comment findById(String id);
    void deleteComment(Comment comment);
}
