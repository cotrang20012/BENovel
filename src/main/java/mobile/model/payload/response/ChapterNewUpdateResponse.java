package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ChapterNewUpdateResponse {
    protected String theloai;
    protected String tentruyen;
    protected String tenchap;
    protected String tacgia;
    protected String nguoidangtruyen;
    protected Date updateAt;
    protected String url;
    protected int chapnumber;
}
