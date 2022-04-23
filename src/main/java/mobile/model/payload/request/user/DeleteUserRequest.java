package mobile.model.payload.request.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteUserRequest {
    protected String username;
}
