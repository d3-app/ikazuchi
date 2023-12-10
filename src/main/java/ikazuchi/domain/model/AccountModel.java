package ikazuchi.domain.model;

import java.util.Collections;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccountModel {
  private Long id;
  @NotEmpty
  private String name;
  private String password;
  private String newPassword;
  private List<AccountRoleModel> roles = Collections.emptyList();
  private boolean roleAdmin = false;
}
