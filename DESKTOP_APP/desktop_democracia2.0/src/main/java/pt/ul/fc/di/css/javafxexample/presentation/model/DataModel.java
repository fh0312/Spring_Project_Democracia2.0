package pt.ul.fc.di.css.javafxexample.presentation.model;

import java.io.File;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.di.css.javafxexample.DTOs.ProjetoLeiDTO;
import pt.ul.fc.di.css.javafxexample.presentation.control.DesktopController;

public class DataModel {

	/*
	 * in this way personList also reports mutations of the elements in it by using
	 * the given extractor. Observable objects returned by extractor (applied to
	 * each list element) are listened for changes and transformed into "update"
	 * change of ListChangeListener. since the phone is not visible, changes in the
	 * phone do not need to be propagated
	 */
	private final DesktopController dc = new DesktopController();
	
	private final ObservableList<Person> personList = FXCollections
			.observableArrayList(person -> new Observable[] { person.firstNameProperty(), person.lastNameProperty() });

	public ObservableList<Person> getCustomerList() {
		return personList;
	}
	
	public ObservableList<ProjetoLeiDTO> getProjsList() {
		return FXCollections.observableList((dc.getListaProjetos()));
	}

	private final ObjectProperty<Person> currentCustomer = new SimpleObjectProperty<>(null);

	public ObjectProperty<Person> currentCustomerProperty() {
		return currentCustomer;
	}

	public final Person getCurrentCustomer() {
		return currentCustomerProperty().get();
	}

	public final void setCurrentCustomer(Person person) {
		currentCustomerProperty().set(person);
	}

	public void loadData(File file) {		
		personList.setAll(new Person("Jose", "Silva", 934445678), new Person("Isabel", "Ramos", 912765432),
				new Person("Eloi", "Matos", 965436576), new Person("Ema", "Antunes", 217122121),
				new Person("Paulo", "Guerra", 217500504));
	}

	public void saveData(File file) {
	}

	public ObservableList<ProjetoLeiDTO> getVotingList() {
		return FXCollections.observableList((dc.getEmVotacao()));
	}
}