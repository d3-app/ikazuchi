package ikazuchi.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ikazuchi.database.entity.ThingEntity;
import ikazuchi.database.repository.ThingRepository;
import ikazuchi.domain.model.ThingModel;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ThingService {

  @Autowired
  ThingRepository thingRepository;

  public List<ThingModel> search() {
    return thingRepository.findAll().stream().map(e -> toModel(e)).collect(Collectors.toList());
  }

  public ThingModel save(ThingModel model) {
    return Optional.ofNullable(thingRepository.save(toEntity(model))).map(e -> toModel(e))
        .orElse(new ThingModel());
  }

  public void delete(ThingModel model) {
    thingRepository.delete(toEntity(model));
  }

  public ThingModel toModel(ThingEntity entity) {
    ThingModel model = new ThingModel();
    model.setId(entity.getId());
    model.setName(entity.getName());
    return model;
  }

  public ThingEntity toEntity(ThingModel model) {
    ThingEntity entity = new ThingEntity();
    entity.setId(model.getId());
    entity.setName(model.getName());
    return entity;
  }
}
