package ikazuchi.database.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ikazuchi.database.entity.AccountRoleEntity;

public interface AccountRoleRepository extends JpaRepository<AccountRoleEntity, Long> {

  List<AccountRoleEntity> findByAccountId(Long accountId);
}
