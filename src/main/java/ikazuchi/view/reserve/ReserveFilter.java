package ikazuchi.view.reserve;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import ikazuchi.domain.model.ReserveModel;

public class ReserveFilter {

  private final GridListDataView<ReserveModel> dataView;

  private Map<String, String> map = new HashMap<>();
  private String value;

  public ReserveFilter(GridListDataView<ReserveModel> dataView) {
    this.dataView = dataView;
    this.dataView.addFilter(this::filter);
  }

  public void put(String key, String value) {
    this.value = value;
    map.put(key, value);
    dataView.refreshAll();
  }

  public boolean filter(ReserveModel reserve) {
    System.out.println(value);
    return map.entrySet().stream().map(e -> {
      if ("Thing Name".equals(e.getKey())) {
        return matches(reserve.getThingName(), e.getValue());
      }
      if ("Account Name".equals(e.getKey())) {
        return matches(reserve.getAccountName(), e.getValue());
      }
      return false;
    }).collect(Collectors.reducing(true, Boolean::logicalAnd));
  }

  private boolean matches(String value, String searchTerm) {
    return searchTerm == null || searchTerm.isEmpty()
        || Optional.ofNullable(value).orElse("").toLowerCase().contains(searchTerm.toLowerCase());
  }
}
