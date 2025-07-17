package cl.tenpo.repository.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "code")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "mambu_id_code", nullable = false)
  private int mambuIdCode;

  @Column(name = "mambu_code_name", nullable = false)
  private String mambuCodeName;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_code_group", nullable = true)
  private CodeGroup codeGroup;
}
