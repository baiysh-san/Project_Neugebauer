package pti.view;

import java.io.IOException;

import org.springframework.context.support.GenericXmlApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The Launcher class which extends from Application
 * 
 * This is the main class for the GUI of our project.
 *
 */
public class Launcher extends Application {

	private Stage primaryStage;

	static GenericXmlApplicationContext ctx;

	/**
	 * Starts the Application with the Layout of the Login.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(this.getClass().getResource("LayoutLogin.fxml"));

			AnchorPane anchorPane = (AnchorPane) loader.load();
			ControllerLogin c = loader.getController();
			c.init(primaryStage, null);
			
			primaryStage.setScene(new Scene(anchorPane));
			primaryStage.setTitle("Projektmanager");
			
			primaryStage.centerOnScreen();

			primaryStage.getIcons().add(new Image("file:src/pti/view/Icon.png"));
			
			primaryStage.sizeToScene();

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * It's the main method of the launcher. It load the content of the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ctx = new GenericXmlApplicationContext();
		ctx.load("app-context.xml");
		ctx.refresh();
		launch(args);
	}

	/**
	 * Returns the primary stage.
	 * 
	 * @return primaryStage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

}
