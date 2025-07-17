package cl.tenpo.exceptions;

import lombok.Getter;

@Getter
public class TenpoBusinessException extends RuntimeException {
    private final String errorCode;
    private final String[] reasons;

    public TenpoBusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.reasons = new String[0];
    }

    public TenpoBusinessException(String errorCode, String message, String... reasons) {
        super(message);
        this.errorCode = errorCode;
        this.reasons = reasons;
    }
    public TenpoBusinessException(String errorCode,String... reasons) {
        this.errorCode = errorCode;
        this.reasons = reasons;
    }
}
