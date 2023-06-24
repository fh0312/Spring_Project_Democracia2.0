package pt.ul.fc.di.css.javafxexample.presentation.control;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.presentation.model.DataModel;

public class LoginController {
	
	@FXML
	private TextField cc ;
	
	@FXML
	private Button logButton;
	
	private DataModel model;
	

	public void initModel(DataModel model) {
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}
		this.model = model ;
	}
	
	public void login(ActionEvent event) {
		String prefix = "/pt/ul/fc/di/css/javafxexample/presentation/view/";
		
		String ccText = cc.getText();
		if(!ccText.isBlank()) {
			DesktopController dc = new DesktopController();
			if(dc.login(ccText)!=null) {
				System.out.println("Login bem sucedido");				
				try {
			        FXMLLoader loader = new FXMLLoader(getClass().getResource(prefix + "allProjs.fxml"));
			        Parent root = loader.load();
			        ProjsController controller = loader.getController();

			        DataModel model = new DataModel();
			        controller.initModel(model);

			        Scene scene = new Scene(root);
			        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			        stage.setScene(scene);
			        stage.show();
			        
			        
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
		}
		
	}

}
