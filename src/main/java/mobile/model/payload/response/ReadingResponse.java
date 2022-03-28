package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReadingResponse {
    protected String tentruyen;
    protected String hinhanh;
    protected int chapnumber;
    protected int sochap;
    protected String url;
}
