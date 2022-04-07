package mobile.model.payload.request.novel;

import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class UpdateNovelRequest {
    protected String tentruyen;
    protected String id;
    protected String theloai;
    protected String tacgia;
    protected String url;
    protected String hinhanh;
    protected String noidung;
}
