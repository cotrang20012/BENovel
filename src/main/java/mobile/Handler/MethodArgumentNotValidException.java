package mobile.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MethodArgumentNotValidException extends BindException
{
    public MethodArgumentNotValidException(BindingResult exception) {
        super(exception);
    }
}