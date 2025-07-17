package cl.tenpo.exception;

import cl.tenpo.observability.trace.service.TelemetryService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestExceptionHandler {

  private final TelemetryService telemetryService;

  @ExceptionHandler({TemplateException.class, Exception.class})
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public ResponseErrorDto handleEventException(Exception ex) {
    return new ResponseErrorDto(ex.getLocalizedMessage());
  }

  private MessageSource messageSource;

  @ExceptionHandler(TenpoException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleTenpoException(
      TenpoException ex, HttpServletRequest request) {

    String endpoint = request.getRequestURI();

    String traceId = getTraceIdFromNowDate();
    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setCode(this.getErrorCode(ex));
    errorResponse.setMessage(this.resolveMessage(ex));

    if (ex.getReasons() != null && ex.getReasons().length > 0) {
      errorResponse.setReasons(ex.getReasons());
    }

    String message =
        String.format(
            "%s [%s :: %s]", traceId, errorResponse.getCode(), errorResponse.getMessage());
    log.error(message);

    return new ResponseEntity<>(errorResponse, ex.getCode());
  }


  private String getErrorCode(TenpoException exception) {
    return StringUtils.isBlank(exception.getErrorCode()) ? "SEMS-000" : exception.getErrorCode();
  }

  private String resolveMessage(TenpoException exception) {
    String message = exception.getMessage();
    String key = this.getErrorCode(exception);

    if (StringUtils.isBlank(message)) {
      String[] reasons = exception.getReasons();
      if (this.messageSource != null) {
        try {
          message = this.messageSource.getMessage(key, reasons, Locale.ENGLISH);
        } catch (NoSuchMessageException var6) {
          log.error(
              "[resolveMessage] Unrecognized key {} to resolve message, returning key as message",
              key);
        }
      }
    }

    return message;
  }

  private static String getTraceIdFromNowDate() {
    Format formatter = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
    return formatter.format(new Date());
  }

  @ExceptionHandler(cl.tenpo.exceptions.TenpoBusinessException.class)
  public ResponseEntity<cl.tenpo.exceptions.ErrorBusinessResponse> handleBusinessException(cl.tenpo.exceptions.TenpoBusinessException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    cl.tenpo.exceptions.ErrorBusinessResponse errorBusinessResponse = new cl.tenpo.exceptions.ErrorBusinessResponse();
    errorBusinessResponse.setCode(ex.getErrorCode());
    errorBusinessResponse.setMessage(ex.getMessage());

    return ResponseEntity
            .status(status)
            .body(errorBusinessResponse);
  }
}
