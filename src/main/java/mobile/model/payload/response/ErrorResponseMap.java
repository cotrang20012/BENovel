package mobile.model.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class ErrorResponseMap {
    public ErrorResponseMap(String message, Map<String,String> details, int status) {
        super();
        this.message = message;
        this.details = details;
        this.status = status;
    }
    private Boolean success;
    private int status;
    private String message;
    private Map<String,String> details;
}
