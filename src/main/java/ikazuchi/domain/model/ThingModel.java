package ikazuchi.domain.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ThingModel {
  private Long id;
  @NotEmpty
  private String name;
}
