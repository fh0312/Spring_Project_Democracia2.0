package pt.ul.fc.di.css.javafxexample.presentation.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.DTOs.ProjetoLeiDTO;
import pt.ul.fc.di.css.javafxexample.presentation.model.DataModel;

public class ProjsController {
	@FXML
	private ListView<ProjetoLeiDTO> listView;

	private DataModel model;
	
	

	public void initModel(DataModel model) {
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}

		this.model = model;
		listView.setItems(model.getProjsList());

		listView.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
			@Override
			protected void updateItem(ProjetoLeiDTO item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null && !empty) {
					setText(item.getName());
				} else {
					setText("");
				}
			}
		});

		listView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) { // Double click
				ProjetoLeiDTO selectedItem = listView.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					
					String prefix = "/pt/ul/fc/di/css/javafxexample/presentation/view/";

					FXMLLoader loader = new FXMLLoader(getClass().getResource(prefix + "votarProj.fxml"));
					if (selectedItem.getVotacaoId() == null) {
						loader = new FXMLLoader(getClass().getResource(prefix + "apoiarProj.fxml"));
						try {
							Parent root = loader.load();
							ApoiarProjController controller = loader.getController();
							DataModel model2 = new DataModel();
							controller.initModel(model2,selectedItem);
							Scene scene = new Scene(root);
							Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							stage.setScene(scene);
							stage.show();

						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						try {
							Parent root = loader.load();
							VotarProjController controller = loader.getController();
							DataModel model2 = new DataModel();
							controller.initModel(model2,selectedItem);
							Scene scene = new Scene(root);
							Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							stage.setScene(scene);
							stage.show();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		});
	}
	
	public void goToAllProjs(ActionEvent event) {
		String prefix = "/pt/ul/fc/di/css/javafxexample/presentation/view/";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(prefix + "allProjs.fxml"));
		try {
			Parent root = loader.load();
			ProjsController controller = loader.getController();
			DataModel model2 = new DataModel();
			controller.initModel(model2);
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void goToVotingProjs(ActionEvent event) {
		String prefix = "/pt/ul/fc/di/css/javafxexample/presentation/view/";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(prefix + "ProjsEmVotacao.fxml"));
		try {
			Parent root = loader.load();
			VotingProjsController controller = loader.getController();
			DataModel model2 = new DataModel();
			controller.initModel(model2);
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logout(ActionEvent event) {
		String prefix = "/pt/ul/fc/di/css/javafxexample/presentation/view/";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(prefix + "login.fxml"));
		try {
			Parent root = loader.load();
			LoginController controller = loader.getController();
			DataModel model2 = new DataModel();
			controller.initModel(model2);
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}