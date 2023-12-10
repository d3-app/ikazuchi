package ikazuchi.domain.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReserveModel {
  private Long id;
  private LocalDate reserveDate;
  private Long thingId;
  private String thingName;
  private Long accountId;
  private String accountName;
}
