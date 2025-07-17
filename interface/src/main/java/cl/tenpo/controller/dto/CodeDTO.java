package cl.tenpo.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.micrometer.common.lang.Nullable;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
public class CodeDTO {
  @Nullable
  private UUID id;
  private int mambuIdCode;
  private String mambuCodeName;
  @Nullable
  private String groupName;
  private UUID groupId;
}
