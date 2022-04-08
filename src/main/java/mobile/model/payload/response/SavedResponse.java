package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
public class SavedResponse {
    protected String tentruyen;
    protected String hinhanh;
    protected String url;
    protected String tacgia;
    protected ObjectId id;
}
