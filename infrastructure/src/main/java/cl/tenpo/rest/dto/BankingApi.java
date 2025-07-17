package cl.tenpo.rest.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "backoffice.thirdparty.banking-priv")
public class BankingApi {
  private String baseUrl;
  private String getTransaction;
  private String getReissueCard;
  private String getHoldTransaction;
  private String transactionHoldApiKeyName;
  private String transactionHoldApiKey;
}
