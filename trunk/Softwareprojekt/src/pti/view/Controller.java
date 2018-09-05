package pti.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The abstract class Controller to handle Logout and Close of all other controllers.
 */
public abstract class Controller {

	protected Stage primaryStage;
	protected String loginName;

	@FXML
	protected Label loginNameLabel = new Label();

	/**
	 * Initialize the primaryStage with the given loginName.
	 * @param primaryStage
	 * @param loginName
	 */
	public void init(Stage primaryStage, String loginName) {
		this.primaryStage = primaryStage;
		this.loginName = loginName;
		loginNameLabel.setText(loginName);
		onInit();
	}

	abstract void onInit();

	/**
	 * Handle the logout of the Login.
	 */
	@FXML
	protected void handleLogout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("LayoutLogin.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			ControllerLogin c = loader.getController();
			c.init(primaryStage, null);
			primaryStage.setScene(new Scene(anchorPane));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle to close the primaryStage.
	 */
	@FXML
	protected void handleClose() {
		primaryStage.close();
	}

}
