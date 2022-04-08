package mobile.repository;

import mobile.model.Entity.Chapter;
import mobile.model.Entity.Novel;
import mobile.model.Entity.Reading;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
public interface ReadingRepository  extends MongoRepository<Reading, ObjectId> {
    void deleteAllByNovel(Novel novel);
    @Query("{'user.$id':?0}")
    List<Reading> findByUserId(ObjectId id);
   // @Query("{$and : [?#{{'user.$id':[0]}},?#{{'novel.$id':[1]}}]}")
    @Query("{$and : [{'user.$id':?0},{'novel.$id':?1}]}")
    Optional<Reading> findWithParam(ObjectId userId, ObjectId novelId);
    @Query("{}")
    List<Reading> find(Pageable pageable);

}