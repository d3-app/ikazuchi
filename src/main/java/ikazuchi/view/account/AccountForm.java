package ikazuchi.view.account;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ikazuchi.domain.model.AccountModel;

@SuppressWarnings("serial")
public class AccountForm extends FormLayout {

  TextField name = new TextField("Account name");
  PasswordField newPassword = new PasswordField("Account new password");
  Checkbox roleAdmin = new Checkbox("Role Admin");

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");

  Binder<AccountModel> binder = new BeanValidationBinder<>(AccountModel.class);

  public AccountForm() {
    addClassName("account-form");
    binder.bindInstanceFields(this);
    add(name, newPassword, roleAdmin, new HorizontalLayout(save, delete, close));

    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(e -> {
      if (binder.isValid()) {
        fireEvent(new SaveEvent(this, binder.getBean()));
      }
    });
    delete.addClickListener(e -> {
      fireEvent(new DeleteEvent(this, binder.getBean()));
    });
    close.addClickListener(e -> {
      fireEvent(new CloseEvent(this));
    });

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
  }

  public void setAccountModel(AccountModel accountModel) {
    binder.setBean(accountModel);
  }

  public static abstract class AccountFormEvent extends ComponentEvent<AccountForm> {
    private AccountModel accountModel;

    protected AccountFormEvent(AccountForm source, AccountModel accountModel) {
      super(source, false);
      this.accountModel = accountModel;
    }

    public AccountModel getAccountModel() {
      return accountModel;
    }
  }

  public static class SaveEvent extends AccountFormEvent {
    SaveEvent(AccountForm source, AccountModel accountModel) {
      super(source, accountModel);
    }
  }

  public static class DeleteEvent extends AccountFormEvent {
    DeleteEvent(AccountForm source, AccountModel accountModel) {
      super(source, accountModel);
    }
  }

  public static class CloseEvent extends AccountFormEvent {
    CloseEvent(AccountForm source) {
      super(source, null);
    }
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return super.addListener(eventType, listener);
  }
}
