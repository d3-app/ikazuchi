package ikazuchi.security;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ikazuchi.database.entity.AccountEntity;
import ikazuchi.database.entity.AccountRoleEntity;
import ikazuchi.database.repository.AccountRepository;
import ikazuchi.database.repository.AccountRoleRepository;

@Service
public class CustomUserService implements UserDetailsService {

  @Autowired
  AccountRepository accountRepository;
  @Autowired
  AccountRoleRepository accountRoleRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AccountEntity account = accountRepository.findByName(username);
    Optional.ofNullable(account)
        .orElseThrow(() -> new UsernameNotFoundException("not found : " + username));
    List<AccountRoleEntity> roles = accountRoleRepository.findByAccountId(account.getId());
    return new CustomUser(account, roles);
  }
}
