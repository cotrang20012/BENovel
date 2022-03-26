package mobile.Service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import mobile.model.Entity.Chapter;
import mobile.model.Entity.Novel;
import mobile.model.Entity.User;

import java.util.List;

public interface ReadingService {
   void upsertReading(User user, Chapter chapter, Novel novel, String url);
}
