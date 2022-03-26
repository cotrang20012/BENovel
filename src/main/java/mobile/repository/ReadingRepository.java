package mobile.repository;

import mobile.model.Entity.Chapter;
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
    List<Reading> findByUserId(ObjectId id);
    Optional<Reading> findByUserIdAndDautruyenId (ObjectId userId, ObjectId dautruyenId);
}