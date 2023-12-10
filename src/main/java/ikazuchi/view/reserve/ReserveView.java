package ikazuchi.view.reserve;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.SpringComponent;
import ikazuchi.domain.model.ReserveModel;
import ikazuchi.domain.service.ReserveService;
import ikazuchi.security.CustomUser;
import ikazuchi.security.SecurityService;
import ikazuchi.view.MainLayout;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(layout = MainLayout.class)
@RouteAlias(value = "/", layout = MainLayout.class)
@PageTitle("reserve | ikazuchi")
public class ReserveView extends VerticalLayout {

  private final ReserveService reserveService;
  private final SecurityService securityService;

  HorizontalLayout toolbar = new HorizontalLayout();
  DatePicker reserveDate = new DatePicker("Select a date:");
  Checkbox vacant = new Checkbox("Vacant");
  Grid<ReserveModel> grid = new Grid<>(ReserveModel.class);

  public ReserveView(ReserveService reserveService, SecurityService securityService) {
    this.reserveService = reserveService;
    this.securityService = securityService;

    addClassName("reserve-view");
    setSizeFull();

    add(reserveDate);

    configureToolbar();
    add(toolbar);

    configureGrid();
    add(grid);

    load(reserveDate.getValue());
  }

  void configureToolbar() {
    toolbar.addClassName("reserve-toolbar");
    toolbar.setAlignItems(Alignment.BASELINE);
    toolbar.add(reserveDate, vacant);

    DatePickerI18n i18n = new DatePickerI18n();
    i18n.setDateFormat("yyyy-MM-dd");
    reserveDate.setI18n(i18n);
    reserveDate.setValue(LocalDate.now());
    reserveDate.addValueChangeListener(e -> load(e.getValue()));
    vacant.addValueChangeListener(e -> load(reserveDate.getValue()));
  }

  void configureGrid() {
    grid.addClassName("reserve-grid");
    grid.setSizeFull();
    grid.setColumns("reserveDate", "thingId", "thingName", "accountId", "accountName");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
    grid.addComponentColumn(reserve -> {
      if (reserve.getAccountId() == null) {
        // 予約
        Button button = new Button(VaadinIcon.BOOKMARK_O.create(), e -> reserve(reserve));
        return new HorizontalLayout(button);
      } else if (securityService.hasRole("ROLE_ADMIN")
          || securityService.getCustomUser().getAccountId().equals(reserve.getAccountId())) {
        // キャンセル
        Button button = new Button(VaadinIcon.TRASH.create(), e -> cancel(reserve));
        button.setTooltipText("キャンセル");
        return new HorizontalLayout(button);
      }
      return new HorizontalLayout();
    }).setHeader("Action");


    grid.getHeaderRows().forEach(row -> {
      row.getCells().forEach(cell -> {
        String label = cell.getText();
        if ("Thing Name".equals(label) || "Account Name".equals(label)) {
          TextField textField = new TextField();
          textField.setClearButtonVisible(true);
          textField.setPlaceholder(label);
          textField.addValueChangeListener(e -> {
            map.put(label, e.getValue());
            grid.getListDataView().refreshAll();
          });
          cell.setComponent(new VerticalLayout(textField));
        }
      });
    });
  }

  void load(LocalDate reserveDate) {
    Optional.ofNullable(reserveDate).ifPresent(d -> {
      grid.setItems(reserveService.search(d, Boolean.TRUE.equals(vacant.getValue())));
      grid.getListDataView().removeFilters();
      grid.getListDataView().addFilter(this::filter);
    });
  }

  void cancel(ReserveModel reserve) {
    reserveService.cancel(reserve);
    load(reserveDate.getValue());
  }

  void reserve(ReserveModel reserve) {
    CustomUser user = securityService.getCustomUser();
    reserve.setReserveDate(reserveDate.getValue());
    reserve.setAccountId(user.getAccountId());
    reserveService.reserve(reserve);
    load(reserveDate.getValue());
  }

  Map<String, String> map = new HashMap<>();

  boolean filter(ReserveModel reserve) {
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

  boolean matches(String value, String searchTerm) {
    return searchTerm == null || searchTerm.isEmpty()
        || Optional.ofNullable(value).orElse("").toLowerCase().contains(searchTerm.toLowerCase());
  }
}
