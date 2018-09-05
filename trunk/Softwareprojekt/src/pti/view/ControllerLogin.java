package pti.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pti.model.domain.Admin;
import pti.model.service.AdminService;

/**
 * This controller is the connection to the LayoutLogin.
 */
public class ControllerLogin extends Controller {

	@FXML
	private TextField loginInput;

	@FXML
	private PasswordField passwordInput;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private ImageView gif;

	@FXML
	private ImageView logo;

	@FXML
	private ImageView picture;

	@FXML
	private ImageView background;

	@FXML
	private ImageView transparency;

	@FXML
	private AnchorPane ap;

	@FXML
	private GridPane gp;

	@FXML
	private Pane rightUp;

	@FXML
	private Pane leftDown;

	private String errorMessage;

	private Stage dialogStage;

	public String getLoginName() {
		return loginName;
	}

	private boolean correctLogin = false;

	/**
	 * This method add the login and the password of one user into the textfields.
	 */
	@Override
	void onInit() {
		
		checkScreenSize();

		loginInput.setText("serbus");
		passwordInput.setText("qwerty");
		/* --> Logins for testing --> */
		AdminService as = Launcher.ctx.getBean("adminService", AdminService.class);
		List<Admin> admins = as.findAll();
		for (int i = 0; i < admins.size(); i++) {
			System.out.println(
					"Nutzer " + i + ": " + admins.get(i).getLogin() + "| Password: " + admins.get(i).getPassword());
			/* <-- Logins for testing <-- */
		}

	}

	/**
	 * Handle the login of the Login window. If the login or the password is empty a
	 * alert will be shown. Otherwise it checks of the right password username
	 * combination. If no combination will found the user will get a alert.
	 * 
	 * @throws InterruptedException
	 */
	@FXML
	private void handleLogin() {

		errorMessage = "";

		if (loginInput.getText().isEmpty())
			errorMessage += "Bitte gib einen Anmeldename ein!\n";

		if (passwordInput.getText().isEmpty())
			errorMessage += "Bitte gib ein Password ein!\n";

		if (loginInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {

			// Show the error message.
			printAlert(AlertType.ERROR, "Leere Textfelder", "Bitte korrigiere folgende Textfelder.", errorMessage);

		} else {

			AdminService as = Launcher.ctx.getBean("adminService", AdminService.class);
			List<Admin> admins = as.findAll();
			for (int i = 0; i < admins.size(); i++) {

				if (loginInput.getText().equals(admins.get(i).getLogin())
						&& passwordInput.getText().equals(admins.get(i).getPassword())) {

					correctLogin = true;

					changeVisibleStatus(false, false, false, true, true);

					progressIndicator.setProgress(-1.0f);

					Random rand = new Random();
					int delay = rand.nextInt(3) * 1000 + 2000;

					Timer timer = new Timer(delay, null);
					ActionListener taskPerformer = new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {

									((Timer) evt.getSource()).stop();

									changeVisibleStatus(true, true, true, false, false);

									try {
										FXMLLoader loader = new FXMLLoader();

										loader.setLocation(this.getClass().getResource("LayoutMainMenu.fxml"));

										AnchorPane anchorPane = (AnchorPane) loader.load();
										ControllerMainMenu c = loader.getController();
										loginName = loginInput.getText();
										c.init(primaryStage, loginName);

										primaryStage.setScene(new Scene(anchorPane));

										primaryStage.centerOnScreen();

									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							});
						}
					};
					new Timer(delay, taskPerformer).start();
					timer.stop();

				} else if (correctLogin == false && i >= admins.size() - 1) {
					printAlert(AlertType.ERROR, "Anmeldung fehlgeschlagen", "Kein Nutzer gefunden!",
							"Versuche erneut dich in deinem Account anzumelden.");
				}
			}
		}
	}

	/**
	 * Change the visibility of the following GUI elements
	 * 
	 * @param pictureVisibility
	 *            - false before timer starts, true if timer stopped
	 * @param transparencyVisibility
	 *            - false before timer starts, true if timer stopped
	 * @param gifVisibility
	 *            - true before timer starts, false if timer stopped
	 * @param progressIndicatorVisibility
	 *            - true before timer starts, false if timer stopped
	 */
	private void changeVisibleStatus(boolean pictureVisibility, boolean transparencyVisibility, boolean gpVisibility,
			boolean gifVisibility, boolean progressIndicatorVisibility) {
		picture.setVisible(pictureVisibility);
		transparency.setVisible(transparencyVisibility);
		gp.setVisible(gpVisibility);
		gif.setVisible(gifVisibility);
		progressIndicator.setVisible(progressIndicatorVisibility);
	}

	/**
	 * This method opens a dialog witch shows a alert
	 * 
	 * @param alertType
	 * 			  - the alert type can be for example INFORMATION, ERROR, WARNING...
	 * @param title
	 *            - the title of the alert
	 * @param header
	 *            - the header of the alert
	 * @param errorMessage
	 *            - the error message of the alert
	 */
	private void printAlert(AlertType alertType, String title, String header, String errorMessage) {
		
		Alert alert = new Alert(alertType);

		// Set a icon to the alert window.
		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/pti/view/Icon.png"));

		alert.initOwner(dialogStage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(errorMessage);

		alert.showAndWait();
	}

	/**
	 * Handle the close button of the GUI.
	 */
	@FXML
	protected void handleClose() {
		primaryStage.close();
	}

	/**
	 * Checks the screenSize and change the sizes of the objects in the GUI.
	 * 
	 * @return
	 */
	@FXML
	private void checkScreenSize() {
		Timer timer = new Timer(200, null);
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				int height = (int) primaryStage.getHeight();
				int width = (int) primaryStage.getWidth();

				background.setFitHeight(height);
				background.setFitWidth(width);

				logo.setFitWidth(width / 3);
				logo.setFitHeight(height / 3);

				rightUp.setPrefSize(width / 2, height / 2);
				leftDown.setPrefSize(width / 2, height / 2.25);

				picture.setFitWidth(rightUp.getWidth() - 100);
				picture.setFitHeight(rightUp.getHeight());

				transparency.setFitWidth(leftDown.getWidth() - 100);
				transparency.setFitHeight(leftDown.getHeight()-30);

				gp.setPrefSize(transparency.getFitWidth(), transparency.getFitHeight() - 15);

				timer.stop();

			}
		};
		new Timer(200, taskPerformer).start();
	}
}
