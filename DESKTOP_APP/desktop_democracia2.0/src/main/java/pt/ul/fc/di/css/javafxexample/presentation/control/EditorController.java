package pt.ul.fc.di.css.javafxexample.presentation.control;

import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import pt.ul.fc.di.css.javafxexample.presentation.model.DataModel;

public class EditorController {

    @FXML
    private TextField firstNameField ;
    @FXML
    private TextField lastNameField ;
    @FXML
    private TextField phoneField ;

    private DataModel model ;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
        model.currentCustomerProperty().addListener((obs, oldCustomer, newCustomer) -> {
            if (oldCustomer != null) {
                firstNameField.textProperty().unbindBidirectional(oldCustomer.firstNameProperty());
                lastNameField.textProperty().unbindBidirectional(oldCustomer.lastNameProperty());
                phoneField.textProperty().unbindBidirectional(oldCustomer.phoneNumberProperty());
            }
            if (newCustomer == null) {
                firstNameField.setText("");
                lastNameField.setText("");
                phoneField.setText("");
            } else {
                firstNameField.textProperty().bindBidirectional(newCustomer.firstNameProperty());
                lastNameField.textProperty().bindBidirectional(newCustomer.lastNameProperty());
                phoneField.textProperty().bindBidirectional(newCustomer.phoneNumberProperty(),
                		new NumberStringConverter(new Locale ("pt", "PT")));
            }
        });
    }
}