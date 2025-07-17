package cl.tenpo.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class CodeEntity {
  private UUID id;
  private int mambuIdCode;
  private String mambuCodeName;
  private String groupName;
  private UUID groupId;
}
