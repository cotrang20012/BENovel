package mobile.repository;

import mobile.model.Entity.Novel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;
@EnableMongoRepositories
public interface NovelRepository  extends MongoRepository<Novel, ObjectId> {
    Optional<Novel> findByTentruyen(String name);
    Optional<Novel> findBy_id(ObjectId id);
    Novel findByUrl(String url);
    List<Novel> findAllBy_idNotNull(Pageable pageable);
    List<Novel> findAllByStatus(String status,Pageable pageable);
    List<Novel> findAllByTentruyenContainsAllIgnoreCase(String value,Pageable pageable);
    List<Novel> findAllByTheloaiContainsAndTentruyenContainsAllIgnoreCase(String type,String value,Pageable pageable);
    List<Novel> findAllByTacgiaContainsAllIgnoreCase(String value,Pageable pageable);
    List<Novel> findAllByTheloaiContainsAllIgnoreCase(String theloai,Pageable pageable);
    List<Novel> findByTentruyenLike(String name);

    @Query("{'nguoidangtruyen.$id':?0}")
    List<Novel> findWithUserId(ObjectId id,Pageable pageable);
}
