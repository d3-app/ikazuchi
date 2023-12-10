package ikazuchi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ikazuchi.database.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

  AccountEntity findByName(String name);
}
