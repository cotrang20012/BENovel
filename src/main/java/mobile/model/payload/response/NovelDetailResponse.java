package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobile.model.Entity.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
@Getter
@Setter
@NoArgsConstructor
public class NovelDetailResponse {
    protected int danhgia;
    protected String hinhanh;
    protected int luotdoc;
    protected String nguoidangtruyen;
    protected String noidung;
    protected int soluongdanhgia;
    protected String tacgia;
    protected String tentruyen;
    protected String theloai;
    protected String trangthai;
    protected String status;
    protected String url;
    protected int sochap;
}
