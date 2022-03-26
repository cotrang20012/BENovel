package mobile.model.payload.request.user;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InfoUserRequest {
    protected  String tenhienthi;
    protected Date birthdate;
    protected  String image;

}
