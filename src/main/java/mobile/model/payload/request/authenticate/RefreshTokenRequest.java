package mobile.model.payload.request.authenticate;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshTokenRequest {
    @NotEmpty
    String refreshToken;
}
