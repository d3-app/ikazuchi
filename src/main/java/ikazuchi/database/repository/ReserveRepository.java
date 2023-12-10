package ikazuchi.database.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ikazuchi.database.entity.ReserveEntity;

public interface ReserveRepository extends JpaRepository<ReserveEntity, Long> {

  List<ReserveEntity> findByReserveDate(LocalDate reserveDate);
}
