package ikazuchi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ikazuchi.database.entity.ThingEntity;

public interface ThingRepository extends JpaRepository<ThingEntity, Long> {

}
