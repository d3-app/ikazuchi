package ikazuchi.view.thing;

import java.util.Optional;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import ikazuchi.domain.model.ThingModel;
import ikazuchi.domain.service.ThingService;
import ikazuchi.view.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;


@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
@PermitAll
@RolesAllowed(value = "ROLE_ADMIN")
@Route(layout = MainLayout.class)
@PageTitle("thing | ikazuchi")
public class ThingView extends VerticalLayout {

  ThingService thingService;
  Button addButton = new Button("Add thing");
  Grid<ThingModel> grid = new Grid<>(ThingModel.class);
  ThingForm form = new ThingForm();

  public ThingView(ThingService thingService) {
    this.thingService = thingService;

    addClassName("thing-view");
    setSizeFull();

    // layout
    add(getToolbar(), getContent());

    configureButton();
    configureGrid();
    configureForm();

    updateList();
    close();
  }

  private Component getToolbar() {
    var toolbar = new HorizontalLayout(addButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private HorizontalLayout getContent() {
    var content = new HorizontalLayout(grid, form);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, form);
    content.addClassNames("content");
    content.setSizeFull();
    return content;
  }

  private void configureButton() {
    addButton.addClickListener(e -> add());
  }

  private void configureGrid() {
    grid.addClassNames("thing-grid");
    grid.setSizeFull();
    grid.setColumns("id", "name");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));

    grid.asSingleSelect().addValueChangeListener(e -> edit(e.getValue()));
  }

  private void configureForm() {
    form.setWidth("25em");
    form.addSaveListener(this::save);
    form.addDeleteListener(this::delete);
    form.addCloseListener(e -> close());
  }


  // action

  private void add() {
    grid.asSingleSelect().clear();
    edit(new ThingModel());
  }

  public void edit(ThingModel thingModel) {
    Optional.ofNullable(thingModel).ifPresentOrElse(o -> {
      form.setThingModel(o);
      form.setVisible(true);
      addClassName("editing");
    }, () -> close());
  }

  private void save(ThingForm.SaveEvent event) {
    thingService.save(event.getThingModel());
    updateList();
    close();
  }

  private void delete(ThingForm.DeleteEvent event) {
    thingService.delete(event.getThingModel());
    updateList();
    close();
  }

  private void close() {
    form.setThingModel(null);
    form.setVisible(false);
    removeClassName("editing");
  }

  private void updateList() {
    grid.setItems(thingService.search());
  }
}
