package pt.ul.fc.di.css.javafxexample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.presentation.control.LoginController;
import pt.ul.fc.di.css.javafxexample.presentation.model.DataModel;

public class DemocraciaDesktop extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		String prefix = "/pt/ul/fc/di/css/javafxexample/presentation/view/";

		BorderPane root = new BorderPane();
		FXMLLoader loginLoader = new FXMLLoader(getClass().getResource(prefix + "login.fxml"));
		root.setCenter(loginLoader.load());
		LoginController loginController = loginLoader.getController();

		 

		DataModel model = new DataModel();
		loginController.initModel(model);

		Scene scene = new Scene(root, 800, 600);
		primaryStage.setTitle("Democracia2.0");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
