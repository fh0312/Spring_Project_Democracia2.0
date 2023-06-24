package pt.ul.fc.di.css.javafxexample.presentation.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.DTOs.ProjetoLeiDTO;
import pt.ul.fc.di.css.javafxexample.presentation.model.DataModel;

public class ApoiarProjController {

	@FXML
	private TextField titulo;

	@FXML
	private TextField validade;

	@FXML
	private TextField tema;

	@FXML
	private TextField texto;

	@FXML
	private TextField num;

	@FXML
	private WebView webView;

	private DataModel model;

	private ProjetoLeiDTO proj;
	
	private boolean apoiou= false;

	public void initModel(DataModel model, ProjetoLeiDTO selectedItem) {
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}
		this.proj = selectedItem;
		this.model = model;
		this.titulo.setText(selectedItem.getName());
		this.validade.setText(selectedItem.getValidade());
		this.tema.setText(selectedItem.getTemaName());
		this.texto.setText(selectedItem.getTexto());
		this.num.setText(selectedItem.getNumApoiantes().toString());
	}

	public void apoiar() {
		if(!apoiou) {
			DesktopController dc = new DesktopController();
			if(dc.apoiarProj(proj.getId())!=null) {
				Long old = Long.parseLong(this.num.getText())+1;
				this.num.setText(old.toString());
				this.apoiou = true;
			}
			else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setTitle("Alerta");
		        alert.setHeaderText("Apoio inváido!");
		        alert.setContentText("Já apoiou este projeto!");
		        alert.showAndWait();
			}
		}
		else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Alerta");
	        alert.setHeaderText("Apoio inváido!");
	        alert.setContentText("Já apoiou este projeto!");
	        alert.showAndWait();
		}
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
		} catch (Exception e) {
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
		} catch (Exception e) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
