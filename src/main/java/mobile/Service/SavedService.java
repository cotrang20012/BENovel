package mobile.Service;

import mobile.model.Entity.Saved;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface SavedService {
    List<Saved> getSavedByUserId(ObjectId id);
    Saved createSaved(Saved saved);
    Saved deleteSaved(ObjectId userId, ObjectId novelId);
    Saved getSaved(ObjectId userId, ObjectId novelId);
}
