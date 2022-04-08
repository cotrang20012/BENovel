package mobile.Service;

import mobile.model.Entity.Novel;
import mobile.model.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import mobile.model.Entity.Reading;

import java.util.List;


public interface ReadingService {
   void upsertReading(Reading reading);
   List<Reading> getReadings(User user);
   void deleteAllReadingByNovel(Novel novel);

}
