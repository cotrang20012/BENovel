package mobile.model.payload.request.saved;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SavedRequest {
    @NonNull
    protected String url;

}

