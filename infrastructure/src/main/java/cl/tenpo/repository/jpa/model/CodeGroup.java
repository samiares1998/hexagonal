package cl.tenpo.repository.jpa.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "code_group")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id_code_group;

  @Column(name = "group_name", nullable = false)
  private String groupName;

  @OneToMany(mappedBy = "codeGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Code> codes = new ArrayList<>();
}
