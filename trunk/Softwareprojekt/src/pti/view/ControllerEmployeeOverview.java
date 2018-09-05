package pti.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.swing.Timer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pti.model.domain.Employee;
import pti.model.domain.Order;
import pti.model.service.EmployeeService;
import pti.model.service.OrderService;

/**
 * This is the controller class of the LayoutProductOverview witch extends
 * Controller.
 */
public class ControllerEmployeeOverview extends Controller {

	private Employee currentEmployee;

	private ObservableList<Employee> employeeData;
	@FXML
	private TableView<Employee> employeeTableView = new TableView<>();
	@FXML
	private TableColumn<Employee, String> employeeIDColumn = new TableColumn<>();
	@FXML
	private TableColumn<Employee, String> employeeNameColumn = new TableColumn<>();

	@FXML
	private Label firstNameLabel = new Label();
	@FXML
	private Label lastNameLabel = new Label();
	@FXML
	private Label addressLabel = new Label();
	@FXML
	private Label availableLabel = new Label();

	@FXML
	private GridPane gp = new GridPane();
	@FXML
	private GridPane gp2 = new GridPane();

	@FXML
	private Pane p = new Pane();
	@FXML
	private Pane p2 = new Pane();

	@FXML
	private ImageView transparency = new ImageView();
	@FXML
	private ImageView transparency2 = new ImageView();

	/**
	 * This method will be called when the LayoutProductOverview is started. It load
	 * the productService data out of our database and add them to the tableView.
	 */
	@Override
	void onInit() {

		checkScreenSize();

		EmployeeService es = Launcher.ctx.getBean("employeeService", EmployeeService.class);
		List<Employee> employees = es.findAll();
		Employee[] employeesArray = new Employee[employees.size()];
		employeesArray = employees.toArray(employeesArray);

		employeeData = FXCollections.observableArrayList(employeesArray);
		employeeTableView.setItems(employeeData);
		employeeIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId() + ""));
		employeeNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

		employeeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			refreshDetails(observable, oldValue, newValue);
		});
	}

	private void refreshDetails(ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) {
		currentEmployee = newValue;
		if (currentEmployee == null) {
			firstNameLabel.setText("---");
			lastNameLabel.setText("---");
			addressLabel.setText("---");
			availableLabel.setText("---");
		} else {
			firstNameLabel.setText(currentEmployee.getFirstName());
			lastNameLabel.setText(currentEmployee.getLastName());
			addressLabel.setText(currentEmployee.getAddress());
			if (currentEmployee.isAvailable()) {
				availableLabel.setText("Verfügbar!");
			} else {
				availableLabel.setText("Abwesend bis: " + currentEmployee.getNotAvailableUntil().toString());
			}
		}
	}

	/**
	 * This method will add another product to the database after filling in the
	 * data to the dialogStage.
	 */
	@FXML
	private void handleAdd() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(this.getClass().getResource("LayoutAddEmployee.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Neuer Mitarbeiter");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(anchorPane);

			dialogStage.getIcons().add(new Image("file:src/pti/view/Icon.png"));

			dialogStage.setScene(scene);

			// Set the provider into the controller.
			ControllerAddEmployee controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setEmployeeData(employeeData);
			controller.setEmployeeTableView(employeeTableView);

			// Show the dialog and wait until the user closes it.
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will add the new product to the database.
	 */
	@FXML
	public void handleDelete() {

		EmployeeService es = Launcher.ctx.getBean("employeeService", EmployeeService.class);

		if (!(es.findAll().size() < 1)) {

			Employee currentEmployee = employeeTableView.getSelectionModel().getSelectedItem();

			if (!employeeTableView.getSelectionModel().isEmpty()) {
				employeeData.remove(currentEmployee);
				es.delete(currentEmployee);

				if (employeeTableView.getItems().contains(currentEmployee))
					employeeTableView.getItems().remove(currentEmployee);

				printAlert(AlertType.INFORMATION, "Mitarbeiter gelöscht", "Mitarbeiter wurde entfernt!",
						"Der Mitarbeiter wurde aus der Datenbank entfernt.");

			} else {
				printAlert(AlertType.ERROR, "Keine Auswahl", "Was soll gelöscht werden?",
						"Bitte wähle einen Mitarbeiter aus, den du löschen möchtest.");
			}
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);

			ButtonType buttonTypeYes = new ButtonType("Ja");
			ButtonType buttonTypeNo = new ButtonType("Nein");

			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			alert.initOwner(primaryStage);
			alert.setTitle("Keine Mitarbeiter");
			alert.setHeaderText("Keine Mitarbeiter in der Datenbank gefunden.");
			alert.setContentText("Möchten Sie einen Mitarbeiter registrieren?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeYes) {
				handleAdd();
			} else {
				if (result.get() == buttonTypeNo) {

				}
			}
		}
	}

	/**
	 * This method opens a dialog witch shows a alert
	 * 
	 * @param alertType
	 *            - the alert type can be for example INFORMATION, ERROR, WARNING...
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

		alert.initOwner(primaryStage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(errorMessage);

		alert.showAndWait();
	}

	/**
	 * Checks the screenSize and change the sizes of the objects in the GUI.
	 */
	@FXML
	private void checkScreenSize() {

		Timer timer = new Timer(200, null);
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				int height = (int) primaryStage.getHeight();
				int width = (int) primaryStage.getWidth();

				p.setMinSize(width / 3, height + 100);
				p2.setMinSize(width / 3, height);

				gp.setMinSize(p.getWidth(), p.getHeight());
				gp2.setMinSize(p2.getWidth(), p2.getHeight());

				transparency.setFitWidth(p.getWidth());
				transparency.setFitHeight(p.getHeight());

				transparency2.setFitWidth(p2.getWidth());
				transparency2.setFitHeight(p2.getHeight());

				employeeIDColumn.setMinWidth((25 * employeeTableView.getWidth() / 100) - 5);
				employeeNameColumn.setMinWidth((75 * employeeTableView.getWidth() / 100) - 5);

				timer.stop();
			}

		};
		new Timer(200, taskPerformer).start();
	}

}
