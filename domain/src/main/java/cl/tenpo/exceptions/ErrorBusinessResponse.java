package cl.tenpo.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorBusinessResponse {
  private String code;
  private String message;
  private String[] reasons;
}
