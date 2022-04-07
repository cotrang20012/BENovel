package mobile.model.payload.request.novel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CreateNovelRequest {
    protected String tentruyen;
    protected String theloai;
    protected String tacgia;
    protected String url;
    protected String hinhanh;
    protected String noidung;
}
