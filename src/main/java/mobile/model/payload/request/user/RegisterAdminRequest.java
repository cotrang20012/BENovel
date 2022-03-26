package mobile.model.payload.request.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterAdminRequest {
    @NotEmpty(message = "Thiếu username")
    @Size(min = 6, message = "username tối thiểu 6 kí tự")
    private String username;

    @NotEmpty(message = "Thiếu Email")
    @Email(regexp = ".+@.+\\..+",message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Thiếu password")
    @Size(min = 8, message = "Password phải từ 8 kí tự trở lên")
    private String password;

    @NotEmpty(message = "Thiếu roles")
    private String roles;


}
