package cl.tenpo.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Setter
@Getter
public class TenpoException extends RuntimeException {

    private HttpStatus code;
    private String[] reasons;
    private String errorCode;

    public TenpoException(String message) {
        super(message);
    }

    public TenpoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TenpoException(HttpStatus code, String errorCode) {
        this.errorCode = errorCode;
        this.code = code;
    }

    public TenpoException(HttpStatus code, ErrorCode errorCode) {
        this(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.code = code;
    }
    public TenpoException(HttpStatus code, String errorCode, String message) {
        this(message);
        this.code = code;
        this.errorCode = errorCode;
    }

    public TenpoException(Exception cause, HttpStatus code, String errorCode) {
        super(cause);
        this.code = code;
        this.errorCode = errorCode;
    }

    public TenpoException(Exception cause, HttpStatus code, String errorCode, String message) {
        this(message, cause);
        this.code = code;
        this.errorCode = errorCode;
    }

    public TenpoException(HttpStatus code, String message, String... reasons) {
        this(message);
        this.code = code;
        this.reasons = reasons;
    }

    public TenpoException(HttpStatus code, ErrorCode errorCode,String ... reasons) {
        this(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.code = code;
        this.reasons=reasons;
    }
    public TenpoException(int code, String message) {
        this(message);
        this.code = HttpStatus.valueOf(code);
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(this.getMessage());
        response.setCode(this.errorCode);
        if (this.reasons != null && this.reasons.length > 0) {
            response.setReasons(this.reasons);
        }
        return response;
    }

    public ResponseEntity<ErrorResponse> getResponse() {
        return new ResponseEntity<>(this.getErrorResponse(), this.code);
    }

    public TenpoException setReason(String ... reasons) {
        this.reasons = reasons;
        return this;
    }

}
