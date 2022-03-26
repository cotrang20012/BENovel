package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SuccessResponse {
    private Boolean success;
    private int status;
    private String message;
    private Map<String,Object> data;

    public SuccessResponse(){
        this.data = new HashMap<>();
    }
}
