package pti.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pti.model.domain.Machine;
import pti.model.service.MachineService;

/**
 * This controller is for the communication to the LayoutAddProduct.
 */
public class ControllerAddMachine extends Controller {

	private Stage dialogStage;
	private ObservableList<Machine> machineData;
	private TableView<Machine> machineTableView;

	@FXML
	private TextField nameField;
	@FXML
	private TextField descriptionField;

	void onInit() {

	}

	/**
	 * Method to set the dialog Stage unresizable and with a icon.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
	}

	/**
	 * This method will add the new product to the database.
	 */
	@FXML
	public void handleCreate() {
		if (isInputValid()) {
			Machine machine = new Machine();
			machine.setName(nameField.getText());
			machine.setDescription(descriptionField.getText());
			machine.setOperative(true);

			MachineService ps = Launcher.ctx.getBean("machineService", MachineService.class);
			ps.insert(machine);

			List<Machine> machines = ps.findAll();
			Machine[] machinesArray = new Machine[machines.size()];
			machinesArray = machines.toArray(machinesArray);
			machineData = FXCollections.observableArrayList(machinesArray);
			machineTableView.setItems(machineData);
			machineTableView.refresh();

			// Shows the information message.
			printAlert(AlertType.INFORMATION, "Maschine hinzugefügt", "Maschine wurde registriert!",
					"Die Maschine wurde in der Datenbank registriert.");

			dialogStage.close();
		}
	}

	@FXML
	public void handleCancel() {
		dialogStage.close();
	}

	/**
	 * This method checks all inputFields.
	 * 
	 * @return true - if the fields are NOT empty
	 * @return false - if the fields are empty
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (nameField.getText() == null || nameField.getText().length() == 0) {
			errorMessage += "Der Name fehlt!\n";
		}
		if (descriptionField.getText() == null || descriptionField.getText().length() == 0) {
			errorMessage += "Die Beschreibung fehlt!\n";
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			printAlert(AlertType.ERROR, "Fehlermeldung", "Bitte prüfe deine Eingaben:", errorMessage);

			return false;
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

		alert.initOwner(dialogStage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(errorMessage);

		alert.showAndWait();
	}

	public void setMachineData(ObservableList<Machine> machineData) {
		this.machineData = machineData;
	}

	public void setMachineTableView(TableView<Machine> machineTableView) {
		this.machineTableView = machineTableView;
	}

}
