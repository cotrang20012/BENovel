package mobile.repository;

import mobile.model.Entity.Chapter;
import mobile.model.Entity.Novel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
public interface ChapterRepository  extends MongoRepository<Chapter, ObjectId> {
    List<Chapter> findByDautruyenId(ObjectId id);
    Optional<Chapter> findByDautruyenIdAndChapnumber(ObjectId id,int number);
    List<Chapter> findAllByDautruyenId(ObjectId id, Pageable pageable);
    int countAllByDautruyenId(ObjectId id);
    void deleteAllByDautruyenId(Novel novel);

    @Query(value="{ 'dautruyenId' : ?0}", fields="{ 'tenchap' : 1, 'chapnumber' : 1 }")
    List<Object> getTenchapAndChapNumberByDautruyenId(ObjectId id, Pageable pageable);
    @Query(value = "{}",sort="{updateAt:-1}")
    List<Chapter> findNewUpdate(Pageable pageable);
}