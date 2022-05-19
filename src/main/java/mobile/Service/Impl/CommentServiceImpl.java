package mobile.Service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.Service.CommentService;
import mobile.model.Entity.Comment;
import mobile.repository.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;

    @Override
    public void AddComment(Comment comment) {

        if(comment.getParentId() != null){
            Comment commentParent = commentRepository.findById(comment.getParentId()).get();

            UpgradeComment(commentParent,0);
            comment.setDepth(commentParent.getDepth()-1);
        }
         commentRepository.save(comment);
    }

    public void UpgradeComment(Comment comment,int childDepth){
        if(childDepth == comment.getDepth()){
            comment.setDepth(comment.getDepth()+1);
        }
        comment.setNumChild(comment.getNumChild()+1);
        commentRepository.save(comment);
        if(comment.getParentId() != null){
            Comment commentParent = commentRepository.findById(comment.getParentId()).get();
            UpgradeComment(commentParent,comment.getDepth());
        }
    }

    @Override
    public List<Comment> findByUrl(String url, Pageable pageable) {
        return commentRepository.findAllByUrltruyenAndParentIdIsNull(url,pageable);
    }

    @Override
    public List<Comment> findByIdParent(String id, Pageable pageable) {
        ObjectId objectId = new ObjectId(id);
        return commentRepository.findAllByParentId(objectId, pageable);
    }

    @Override
    public Comment UpdateComment(Comment comment, String newcomment) {
        comment.setContent(newcomment);
        return commentRepository.save(comment);
    }

    @Override
    public Comment findById(String id) {
        ObjectId objectId = new ObjectId(id);
        Optional<Comment> commentOptional = commentRepository.findById(objectId);
        if(commentOptional.isEmpty())
            return null;
        return commentOptional.get();

    }

    @Override
    public void deleteComment(Comment comment) {
        if(comment.getParentId() != null){
            ObjectId objectId = new ObjectId(comment.getParentId().toString());
            deleteChild(comment);
            setNewScoredParent(objectId);
        }
        else{
            deleteChild(comment);
        }

    }

    @Override
    public void DeleteCommentByNovelUrl(String url) {
        commentRepository.deleteAllByUrltruyen(url);
    }

    public void deleteChild(Comment comment){
        List<Comment> comments = commentRepository.findAllByParentId(comment.getId());
        for (Comment temp: comments
             ) {
            if(temp.getDepth() != 0){
                deleteChild(temp);
            }
            else
                commentRepository.delete(temp);

        }
        commentRepository.delete(comment);
    }


    public void setNewScoredParent(ObjectId objectId){
        Comment comment = commentRepository.findById(objectId).get();
        List<Comment> comments = commentRepository.findAllByParentId(objectId);
        int depth = 0;
        int numChild = 0;
        for (Comment temp: comments
             ) {
            numChild = numChild + temp.getNumChild() +1;
            if(temp.getDepth() > depth)
                depth = temp.getDepth();
        }
        comment.setNumChild(numChild);
        comment.setDepth(depth+1);
        commentRepository.save(comment);
    }

}

