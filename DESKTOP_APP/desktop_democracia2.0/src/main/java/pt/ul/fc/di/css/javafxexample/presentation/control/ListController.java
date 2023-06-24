package pt.ul.fc.di.css.javafxexample.presentation.control;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import pt.ul.fc.di.css.javafxexample.presentation.model.DataModel;
import pt.ul.fc.di.css.javafxexample.presentation.model.Person;

public class ListController {

	@FXML
	private ListView<Person> listView ;

	private DataModel model;

	public void initModel(DataModel model) {
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}

		this.model = model ;
		listView.setItems(model.getCustomerList());

		listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
		model.setCurrentCustomer(newSelection));
	}

}
