package ikazuchi.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ikazuchi.database.entity.AccountEntity;
import ikazuchi.database.entity.ReserveEntity;
import ikazuchi.database.entity.ThingEntity;
import ikazuchi.database.repository.AccountRepository;
import ikazuchi.database.repository.ReserveRepository;
import ikazuchi.database.repository.ThingRepository;
import ikazuchi.domain.model.ReserveModel;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReserveService {

  @Autowired
  ReserveRepository reserveRepository;
  @Autowired
  ThingRepository thingRepository;
  @Autowired
  AccountRepository accountRepository;

  public List<ReserveModel> search(LocalDate reserveDate, boolean isVacant) {

    Map<Long, ReserveEntity> reserveMap = reserveRepository.findByReserveDate(reserveDate).stream()
        .collect(Collectors.toMap(ReserveEntity::getThingId, e -> e));

    List<ThingEntity> things = thingRepository.findAll();

    Map<Long, AccountEntity> accountMap = accountRepository.findAll().stream()
        .collect(Collectors.toMap(AccountEntity::getId, e -> e));

    return things.stream().map(thing -> {
      ReserveModel model = new ReserveModel();
      model.setThingId(thing.getId());
      model.setThingName(thing.getName());
      Optional.ofNullable(reserveMap.get(model.getThingId())).ifPresent(reserve -> {
        model.setId(reserve.getId());
        model.setReserveDate(reserve.getReserveDate());
        model.setAccountId(reserve.getAccountId());
      });
      Optional.ofNullable(accountMap.get(model.getAccountId())).ifPresent(account -> {
        // model.setAccountId(account.getId());
        model.setAccountName(account.getName());
      });
      return model;
    }).filter(reserve -> {
      if (isVacant) {
        return !Optional.ofNullable(reserve.getAccountId()).isPresent();
      }
      return true;
    }).collect(Collectors.toList());
  }

  public void reserve(ReserveModel model) {
    reserveRepository.save(toEntity(model));
  }

  public void cancel(ReserveModel model) {
    reserveRepository.delete(toEntity(model));
  }

  public ReserveModel toModel(ReserveEntity reserve, ThingEntity thing, AccountEntity account) {
    ReserveModel model = new ReserveModel();
    Optional.ofNullable(reserve).ifPresent(e -> {
      model.setId(e.getId());
      model.setReserveDate(e.getReserveDate());
      model.setThingId(e.getThingId());
      model.setAccountId(e.getAccountId());
    });
    Optional.ofNullable(thing).ifPresent(e -> {
      model.setThingId(e.getId());
      model.setThingName(e.getName());
    });
    Optional.ofNullable(account).ifPresent(e -> {
      model.setAccountId(e.getId());
      model.setAccountName(e.getName());
    });
    return model;
  }

  public ReserveEntity toEntity(ReserveModel model) {
    ReserveEntity entity = new ReserveEntity();
    entity.setId(model.getId());
    entity.setReserveDate(model.getReserveDate());
    entity.setThingId(model.getThingId());
    entity.setAccountId(model.getAccountId());
    return entity;
  }
}
