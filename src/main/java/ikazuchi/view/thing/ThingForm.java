package ikazuchi.view.thing;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ikazuchi.domain.model.ThingModel;

@SuppressWarnings("serial")
public class ThingForm extends FormLayout {
  TextField name = new TextField("Thing name");

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");

  Binder<ThingModel> binder = new BeanValidationBinder<>(ThingModel.class);

  public ThingForm() {
    addClassName("thing-form");
    binder.bindInstanceFields(this);
    add(name, new HorizontalLayout(save, delete, close));

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

  public void setThingModel(ThingModel thingModel) {
    binder.setBean(thingModel);
  }
  
  public static abstract class ThingFormEvent extends ComponentEvent<ThingForm> {
    private ThingModel thingModel;

    protected ThingFormEvent(ThingForm source, ThingModel thingModel) {
      super(source, false);
      this.thingModel = thingModel;
    }

    public ThingModel getThingModel() {
      return thingModel;
    }
  }

  public static class SaveEvent extends ThingFormEvent {
    SaveEvent(ThingForm source, ThingModel thingModel) {
      super(source, thingModel);
    }
  }

  public static class DeleteEvent extends ThingFormEvent {
    DeleteEvent(ThingForm source, ThingModel thingModel) {
      super(source, thingModel);
    }
  }

  public static class CloseEvent extends ThingFormEvent {
    CloseEvent(ThingForm source) {
      super(source, null);
    }
  }

  public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
    return addListener(DeleteEvent.class, listener);
  }

  public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
    return addListener(SaveEvent.class, listener);
  }

  public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
    return addListener(CloseEvent.class, listener);
  }
}
