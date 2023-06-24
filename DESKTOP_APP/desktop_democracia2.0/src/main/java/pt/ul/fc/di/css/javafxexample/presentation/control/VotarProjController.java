package pt.ul.fc.di.css.javafxexample.presentation.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.DTOs.ProjetoLeiDTO;
import pt.ul.fc.di.css.javafxexample.presentation.model.DataModel;

public class VotarProjController {

	@FXML
	private TextField titulo;

	@FXML
	private TextField validade;

	@FXML
	private TextField tema;

	@FXML
	private TextField texto;

	@FXML
	private TextField omissao;

	@FXML
	private CheckBox favor;

	@FXML
	private CheckBox contra;

	private DataModel model;

	private ProjetoLeiDTO proj;

	private boolean votou = false;

	public void initModel(DataModel model, ProjetoLeiDTO selectedItem) {
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}		
		this.model = model;
		this.proj = selectedItem;
		this.model = model;
		this.titulo.setText(selectedItem.getName());
		this.validade.setText(selectedItem.getValidade());
		this.tema.setText(selectedItem.getTemaName());
		this.texto.setText(selectedItem.getTexto());		
		this.omissao.setText(new DesktopController().getOmissao(selectedItem.getId()));
	}

	public void votar() {
		if (!votou) {
			if (this.favor.isSelected() || this.contra.isSelected()) {
				String voto = "";
				if (this.favor.isSelected() && this.contra.isSelected()) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Alerta");
					alert.setHeaderText("Voto inválido!");
					alert.setContentText("Porfavor apenas selecione um tipo de voto!");
					alert.showAndWait();
				} else {
					if (this.favor.isSelected()) {
						voto = "favor";
					} else if (this.contra.isSelected()) {
						voto = "contra";
					}
					DesktopController dc = new DesktopController();
					
					if (dc.votarProjeto(this.proj.getId(), voto) != null) {
						this.votou = true;
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Voto processado");
						alert.setHeaderText("Obrigado por ter votado!");
						alert.setContentText("O seu voto: " + voto + " foi processado com sucesso!");
						alert.showAndWait();
					}
					else {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Alerta");
						alert.setHeaderText("Voto inváido!");
						alert.setContentText("Já votou neste projeto!");
						alert.showAndWait();
					}
				}
			
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Alerta");
			alert.setHeaderText("Voto inváido!");
			alert.setContentText("Porfavor selecione uma opção de voto!");
			alert.showAndWait();
		}

	}else {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Alerta");
		alert.setHeaderText("Voto inváido!");
		alert.setContentText("Já votou neste projeto!");
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
