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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pti.model.domain.Employee;
import pti.model.domain.Machine;
import pti.model.service.EmployeeService;
import pti.model.service.MachineService;

/**
 * This is the controller class of the LayoutProductOverview witch extends
 * Controller.
 */
public class ControllerMachineOverview extends Controller {

	private Machine currentMachine;

	private ObservableList<Machine> machineData;
	@FXML
	private TableView<Machine> machineTableView = new TableView<>();
	@FXML
	private TableColumn<Machine, String> machineIDColumn = new TableColumn<>();
	@FXML
	private TableColumn<Machine, String> machineNameColumn = new TableColumn<>();

	@FXML
	private Label nameLabel = new Label();
	@FXML
	private TextArea descriptionTextArea = new TextArea();
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

	@FXML
	private Label labelMachines = new Label();

	/**
	 * This method will be called when the LayoutProductOverview is started. It load
	 * the productService data out of our database and add them to the tableView.
	 */
	@Override
	void onInit() {

		checkScreenSize();

		MachineService es = Launcher.ctx.getBean("machineService", MachineService.class);
		List<Machine> machines = es.findAll();
		Machine[] machinesArray = new Machine[machines.size()];
		machinesArray = machines.toArray(machinesArray);

		machineData = FXCollections.observableArrayList(machinesArray);
		machineTableView.setItems(machineData);
		machineIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId() + ""));
		machineNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

		machineTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			refreshDetails(observable, oldValue, newValue);
		});
	}

	private void refreshDetails(ObservableValue<? extends Machine> observable, Machine oldValue, Machine newValue) {
		currentMachine = newValue;
		if (currentMachine == null) {
			nameLabel.setText("---");
			descriptionTextArea.setText("---");
			availableLabel.setText("---");
		} else {
			nameLabel.setText(currentMachine.getName());
			descriptionTextArea.setText(currentMachine.getDescription());
			if (currentMachine.isOperative()) {
				availableLabel.setText("Arbeitet!");
			} else {
				availableLabel.setText("Frei bis: " + currentMachine.getNotOperativeUntil().toString());
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

			loader.setLocation(this.getClass().getResource("LayoutAddMachine.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Neue Maschine");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(anchorPane);

			dialogStage.getIcons().add(new Image("file:src/pti/view/Icon.png"));

			dialogStage.setScene(scene);

			// Set the provider into the controller.
			ControllerAddMachine controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMachineData(machineData);
			controller.setMachineTableView(machineTableView);

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

		MachineService ms = Launcher.ctx.getBean("machineService", MachineService.class);

		if (!(ms.findAll().size() < 1)) {

			Machine currentMachine = machineTableView.getSelectionModel().getSelectedItem();

			if (!machineTableView.getSelectionModel().isEmpty()) {
				machineData.remove(currentMachine);
				ms.delete(currentMachine);

				if (machineTableView.getItems().contains(currentMachine))
					machineTableView.getItems().remove(currentMachine);

				printAlert(AlertType.INFORMATION,"Maschine gelöscht", "Maschine wurde entfernt!", "Die Maschine wurde aus der Datenbank entfernt.");

			} else {
				printAlert(AlertType.ERROR, "Keine Auswahl", "Was soll gelöscht werden?",
						"Bitte wähle eine Maschine aus, die du löschen möchtest.");
			}
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);

			ButtonType buttonTypeYes = new ButtonType("Ja");
			ButtonType buttonTypeNo = new ButtonType("Nein");

			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			alert.initOwner(primaryStage);
			alert.setTitle("Keine Maschinen");
			alert.setHeaderText("Keine Maschinen in der Datenbank gefunden.");
			alert.setContentText("Möchten Sie eine Maschine hinzufügen?");

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

				p.setMinSize(width / 3, height - 450);
				p2.setMinSize(width / 3, height);

				gp.setMinSize(p.getWidth(), p.getHeight());
				gp2.setPrefSize(p2.getWidth(), p2.getHeight());

				transparency.setFitWidth(p.getWidth());
				transparency.setFitHeight(p.getHeight());

				transparency2.setFitWidth(p2.getWidth());
				transparency2.setFitHeight(p2.getHeight());

				machineIDColumn.setMinWidth((25 * machineTableView.getWidth() / 100) - 5);
				machineNameColumn.setMinWidth((75 * machineTableView.getWidth() / 100) - 5);

				labelMachines.setMinWidth(machineTableView.getWidth());

				timer.stop();
			}

		};
		new Timer(200, taskPerformer).start();
	}

}
