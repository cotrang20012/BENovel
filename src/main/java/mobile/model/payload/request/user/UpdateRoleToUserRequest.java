package mobile.model.payload.request.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateRoleToUserRequest {
    @NotBlank
    private String username;


    private List<String> roles;
}
