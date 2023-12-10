package ikazuchi.security;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ikazuchi.database.entity.AccountEntity;
import ikazuchi.database.entity.AccountRoleEntity;

@SuppressWarnings("serial")
public class CustomUser extends User {

  private Long accountId;

  public CustomUser(AccountEntity account, List<AccountRoleEntity> roles) {
    super(account.getName(), account.getPassword(),
        Optional.ofNullable(roles).orElse(Collections.emptyList()).stream()
            .map(AccountRoleEntity::getRoleName).map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet()));
    this.accountId = account.getId();
  }

  public Long getAccountId() {
    return accountId;
  }
}
