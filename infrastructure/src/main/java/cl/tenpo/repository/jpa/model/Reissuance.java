package cl.tenpo.repository.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reissuance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reissuance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "holder_id")
  private UUID holderId;

  @Column(name = "account_id")
  private UUID accountId;

  @Column(name = "old_card_id")
  private UUID oldCardId;

  @Column(name = "new_card_id")
  private UUID newCardId;

  @Column(name = "expiration_date")
  private Integer expirationDate;

  @Column(name = "motive")
  private String motive;

  @Column(name = "status")
  private String status;

  @Column(name = "card_type")
  private String cardType;

  @Column(name = "process_type")
  private String processType;

  @Column(name = "reset_user")
  private String resetUser;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "reset_user_email")
  private String resetUserEmail;

  @Column(name = "old_masked_pan")
  private String oldMaskedPan;

  @Column(name = "new_masked_pan")
  private String newMaskedPan;
}
