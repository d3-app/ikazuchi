package ikazuchi.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ikazuchi.database.entity.AccountEntity;
import ikazuchi.database.entity.AccountRoleEntity;
import ikazuchi.database.repository.AccountRepository;
import ikazuchi.domain.model.AccountModel;
import ikazuchi.domain.model.AccountRoleModel;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService {

  @Autowired
  AccountRepository accountRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  public List<AccountModel> search() {
    return accountRepository.findAll().stream().map(e -> toModel(e)).collect(Collectors.toList());
  }

  public AccountModel save(AccountModel model) {
    return Optional.ofNullable(accountRepository.save(toEntity(model))).map(e -> toModel(e))
        .orElse(new AccountModel());
  }

  public void delete(AccountModel model) {
    accountRepository.delete(toEntity(model));
  }

  public AccountModel toModel(AccountEntity entity) {
    AccountModel model = new AccountModel();
    model.setId(entity.getId());
    model.setName(entity.getName());
    model.setPassword(entity.getPassword());
    // roles
    List<AccountRoleModel> roles =
        Optional.ofNullable(entity.getRoles()).orElse(Collections.emptyList()).stream()
            .map(this::toRoleModel).collect(Collectors.toList());
    model.setRoles(roles);
    // roleAdmin
    boolean roleAdmin = roles.stream().anyMatch(o -> "ROLE_ADMIN".equals(o.getRoleName()));
    model.setRoleAdmin(roleAdmin);
    return model;
  }

  public AccountEntity toEntity(AccountModel model) {
    AccountEntity entity = new AccountEntity();
    entity.setId(model.getId());
    entity.setName(model.getName());
    entity.setPassword(Optional.ofNullable(model.getNewPassword()).map(passwordEncoder::encode)
        .orElse(model.getPassword()));
    // roles
    entity.setRoles(new ArrayList<>());
    // roleAdmin
    if (model.isRoleAdmin()) {
      AccountRoleModel m = new AccountRoleModel();
      m.setAccountId(model.getId());
      m.setRoleName("ROLE_ADMIN");
      AccountRoleModel roleAdmin = model.getRoles().stream()
          .filter(o -> "ROLE_ADMIN".equals(o.getRoleName())).findFirst().orElse(m);
      entity.getRoles().add(toRoleEntity(roleAdmin));
    }
    return entity;
  }

  public AccountRoleModel toRoleModel(AccountRoleEntity entity) {
    AccountRoleModel model = new AccountRoleModel();
    model.setId(entity.getId());
    model.setAccountId(entity.getAccountId());
    model.setRoleName(entity.getRoleName());
    return model;
  }

  public AccountRoleEntity toRoleEntity(AccountRoleModel model) {
    AccountRoleEntity entity = new AccountRoleEntity();
    entity.setId(model.getId());
    entity.setAccountId(model.getAccountId());
    entity.setRoleName(model.getRoleName());
    return entity;
  }
}
