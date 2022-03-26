package mobile.repository;

import mobile.model.Entity.Chapter;
import mobile.model.Entity.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
public interface CommentRepository extends MongoRepository<Comment, ObjectId> {

    List<Comment> findAllByUrltruyenAndParentIdIsNull(String urlTruyen, Pageable pageable);
    List<Comment> findAllByParentId(ObjectId parentId, Pageable pageable);
    List<Comment> findAllByParentId(ObjectId parentId);
    Boolean existsByParentId(ObjectId parentId);
    Optional<Comment> findById(ObjectId objectId);
}
