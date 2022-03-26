package mobile.model.payload.response;

import lombok.*;


import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ErrorResponse {
    public ErrorResponse(String message, List<String> details,int status) {
        super();
        this.message = message;
        this.details = details;
        this.status = status;
        this.success = false;
    }
    private Boolean success;
    private int status;
    private String message;
    private List<String> details;
}
