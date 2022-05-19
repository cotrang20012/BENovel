package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NovelResponse {
    protected int danhgia;
    protected String hinhanh;
    protected int luotdoc;
    protected String noidung;
    protected int soluongdanhgia;
    protected String tacgia;
    protected String tentruyen;
    protected String theloai;
    protected String trangthai;
    protected String status;
    protected String url;
}
