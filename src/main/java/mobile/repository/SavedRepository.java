package mobile.repository;

import mobile.model.Entity.Saved;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
public interface SavedRepository extends MongoRepository<Saved, ObjectId> {
    @Query("{'user.$id':?0}")
    List<Saved> findByUserId(ObjectId id);

    @Query(value="{$and:[{'user.$id':?0},{'novel.$id':?1}]}")
    Saved findByParam(ObjectId userId,ObjectId novelId);

    @Query(value="{$and:[{'user.$id':?0},{'novel.$id':?1}]}",delete = true)
    Saved deleteByParam(ObjectId userId,ObjectId novelId);
}
