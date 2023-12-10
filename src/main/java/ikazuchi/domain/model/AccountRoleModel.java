package ikazuchi.domain.model;

import lombok.Data;

@Data
public class AccountRoleModel {
  private Long id;
  private Long accountId;
  private String roleName;
}
