package pt.ul.fc.di.css.javafxexample.presentation.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private final StringProperty firstName = new SimpleStringProperty();
    
    public final StringProperty firstNameProperty() {
        return this.firstName;
    }

    public final String getFirstName() {
        return this.firstNameProperty().get();
    }

    public final void setFirstName(final String firstName) {
        this.firstNameProperty().set(firstName);
    }

    private final StringProperty lastName = new SimpleStringProperty();
    
    public final StringProperty lastNameProperty() {
        return this.lastName;
    }

    public final String getLastName() {
        return this.lastNameProperty().get();
    }

    public final void setLastName(final String lastName) {
        this.lastNameProperty().set(lastName);
    }

    private final IntegerProperty phoneNumber = new SimpleIntegerProperty();

    public final IntegerProperty phoneNumberProperty() {
        return this.phoneNumber;
    }

    public final int getPhoneNumber() {
        return this.phoneNumberProperty().get();
    }

    public final void setPhoneNumber(final int phNumber) {
        this.phoneNumberProperty().set(phNumber);
    }
    
    public Person(String firstName, String lastName, int phNumber) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phNumber);
    }
    @Override
    public String toString() {
    	return firstName.getValue()+" "+lastName.getValue();
    }
}
