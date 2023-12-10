package ikazuchi.view.account;

import java.util.Optional;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import ikazuchi.domain.model.AccountModel;
import ikazuchi.domain.service.AccountService;
import ikazuchi.view.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
@PermitAll
@RolesAllowed(value = "ROLE_ADMIN")
@Route(layout = MainLayout.class)
@PageTitle("account | ikazuchi")
public class AccountView extends VerticalLayout {

  AccountService accountService;

  Button addButton = new Button("Add account");
  Grid<AccountModel> grid = new Grid<>(AccountModel.class);
  AccountForm form = new AccountForm();

  public AccountView(AccountService accountService) {
    this.accountService = accountService;
    addClassName("account-view");
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
    grid.addComponentColumn(m -> {
      if (m.isRoleAdmin()) {
        Checkbox checkbox = new Checkbox(true);
        checkbox.setReadOnly(true);
        return checkbox;
      }
      return new HorizontalLayout();
    }).setHeader("Role Admin");

    grid.getColumns().forEach(col -> col.setAutoWidth(true));

    grid.asSingleSelect().addValueChangeListener(e -> edit(e.getValue()));
  }

  private void configureForm() {
    form.setWidth("25em");
    form.addListener(AccountForm.SaveEvent.class, this::save);
    form.addListener(AccountForm.DeleteEvent.class, this::delete);
    form.addListener(AccountForm.CloseEvent.class, e -> close());
  }

  // action
  private void add() {
    grid.asSingleSelect().clear();
    edit(new AccountModel());
  }

  public void edit(AccountModel accountModel) {
    Optional.ofNullable(accountModel).ifPresentOrElse(o -> {
      form.setAccountModel(o);
      form.setVisible(true);
      addClassName("editing");
    }, () -> close());
  }

  private void save(AccountForm.SaveEvent event) {
    accountService.save(event.getAccountModel());
    updateList();
    close();
  }

  private void delete(AccountForm.DeleteEvent event) {
    accountService.delete(event.getAccountModel());
    updateList();
    close();
  }

  private void close() {
    form.setAccountModel(null);
    form.setVisible(false);
    removeClassName("editing");
  }

  private void updateList() {
    grid.setItems(accountService.search());
  }
}
