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
import pti.model.domain.Employee;
import pti.model.service.EmployeeService;

/**
 * This controller is for the communication to the LayoutAddProduct.
 */
public class ControllerAddEmployee extends Controller {

	private Stage dialogStage;
	private ObservableList<Employee> employeeData;
	private TableView<Employee> employeeTableView;

	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField addressField;

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
			Employee employee = new Employee();
			employee.setFirstName(firstNameField.getText());
			employee.setLastName(lastNameField.getText());
			employee.setAddress(addressField.getText());
			employee.setAvailable(true);

			EmployeeService es = Launcher.ctx.getBean("employeeService", EmployeeService.class);
			es.insert(employee);

			List<Employee> employees = es.findAll();
			Employee[] employeesArray = new Employee[employees.size()];
			employeesArray = employees.toArray(employeesArray);
			employeeData = FXCollections.observableArrayList(employeesArray);
			employeeTableView.setItems(employeeData);
			employeeTableView.refresh();

			// Shows the information message.
			printAlert(AlertType.INFORMATION, "Mitarbeiter hinzugefügt", "Mitarbeiter wurde registriert!",
					"Der Mitarbeiter wurde in der Datenbank registriert.");

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

		if (firstNameField.getText() == null || firstNameField.getText().length() == 0)
			errorMessage += "Der Vorname fehlt!\n";

		if (lastNameField.getText() == null || lastNameField.getText().length() == 0)
			errorMessage += "Der Nachname fehlt!\n";

		if (addressField.getText() == null || addressField.getText().length() == 0)
			errorMessage += "Die Adresse fehlt!\n";

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

	public void setEmployeeData(ObservableList<Employee> employeeData) {
		this.employeeData = employeeData;
	}

	public void setEmployeeTableView(TableView<Employee> employeeTableView) {
		this.employeeTableView = employeeTableView;
	}
}
