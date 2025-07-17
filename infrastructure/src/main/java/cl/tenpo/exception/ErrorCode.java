package cl.tenpo.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  FILE_ERROR("file.error", "File error"),
  DB_ERROR("db.error", "Database error"),
  ALREADY_PROCESSED("already.processed", "Already processed"),
  ACCOUNT_WITH_BALANCE_IN_PREPAID("prepaid.balance", "User with balance on prepaid"),
  ACCOUNT_WITH_BALANCE_IN_SAVING("saving.balance", "User have balance on savings"),
  SAVING_BALANCE_NOT_FOUND("saving.balance.not-found", "Saving balance not found"),
  ERROR_CONSUMING_REQUEST("client.request.error", "Error while consuming account request"),
  ERROR_CONSUMING_DOF_REQUEST("client.request.error", "Error consumiendo DOF request"),
  ERROR_CONSUMING_DOF_CREATION_REQUEST("client.request.error", "Error al crear la DOF"),
  ERROR_CONSUMING_DOF_DECLINE_REQUEST("client.request.error", "Error al rechazar la DOF"),
  ERROR_CONSUMING_CARD_REQUEST("client.request.error", "Error consumiendo card request"),
  ERROR_SENDING_REQUEST("client.request.error.sending", "Ha ocurrido un error enviando el correo"),
  ERROR_ACCOUNT_BALANCE("account.balance.error", "El balance de la cuenta tiene que ser cero"),
  ERROR_ACCOUNT_BALANCE_REQUEST(
      "account.balance.error.request", "Error consumiendo el balance de la cuenta"),
  ERROR_UNLOCK_APP("app.request.error", "Error al desbloquear la app"),
  ERROR_LOCK_APP("app.request.error", "Error al bloquear la app"),
  ERROR_CONSUMING_REISSUANCE_CARD("client.request.error", "Error al consumir reemisiones de tarjeta");

  private final String code;
  private final String message;
  public static final String PREPAID_CASHOUT_ERROR_CODE = "108904";

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
