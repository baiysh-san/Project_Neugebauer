package pti.view;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pti.model.domain.Operation;
import pti.model.domain.Product;
import pti.model.domain.Workschedule;
import pti.model.service.MachineService;
import pti.model.service.ProductService;

/**
 * This controller is for the communication to the LayoutAddProduct.
 */
public class ControllerAddProduct extends Controller {

	private Stage dialogStage;
	private ObservableList<Product> productData;
	private TableView<Product> productTableView;

	@FXML
	private TextField nameField;

	@FXML
	private CheckBox cb1;
	@FXML
	private CheckBox cb2;
	@FXML
	private CheckBox cb3;
	@FXML
	private CheckBox cb4;
	@FXML
	private CheckBox cb5;

	@FXML
	private TextField duration1;
	@FXML
	private TextField duration2;
	@FXML
	private TextField duration3;
	@FXML
	private TextField duration4;
	@FXML
	private TextField duration5;

	@FXML
	private TextArea descriptionField;

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

			Product product = new Product();
			product.setName(nameField.getText());
			product.setDescription(descriptionField.getText());

			Workschedule workschedule = new Workschedule();
			workschedule.setProduct(product);
			product.setWorkschedule(workschedule);

			MachineService ms = Launcher.ctx.getBean("machineService", MachineService.class);
			System.out.println(ms.findAll());

			if (cb1.isSelected()) {
				Operation o1 = new Operation();
				o1.setName("Sägen");
				o1.setDuration(Integer.parseInt(duration1.getText()));
				o1.setMachine(ms.findById(1L));
				workschedule.getOperations().add(o1);
				o1.setWorkschedule(workschedule);
			}
			if (cb2.isSelected()) {
				Operation o2 = new Operation();
				o2.setName("Bohren");
				o2.setDuration(Integer.parseInt(duration2.getText()));
				o2.setMachine(ms.findById(2L));
				workschedule.getOperations().add(o2);
				o2.setWorkschedule(workschedule);
			}
			if (cb3.isSelected()) {
				Operation o3 = new Operation();
				o3.setName("Hobeln");
				o3.setDuration(Integer.parseInt(duration3.getText()));
				o3.setMachine(ms.findById(3L));
				workschedule.getOperations().add(o3);
				o3.setWorkschedule(workschedule);
			}
			if (cb4.isSelected()) {
				Operation o4 = new Operation();
				o4.setName("Politur");
				o4.setDuration(Integer.parseInt(duration4.getText()));
				o4.setMachine(ms.findById(4L));
				workschedule.getOperations().add(o4);
				o4.setWorkschedule(workschedule);
			}
			if (cb5.isSelected()) {
				Operation o5 = new Operation();
				o5.setName("Verpacken");
				o5.setDuration(Integer.parseInt(duration5.getText()));
				o5.setMachine(ms.findById(5L));
				workschedule.getOperations().add(o5);
				o5.setWorkschedule(workschedule);
			}

			ProductService ps = Launcher.ctx.getBean("productService", ProductService.class);
			ps.insert(product);

			// refreshes the table-view
			List<Product> products = ps.findAll();
			Product[] productsArray = new Product[products.size()];
			productsArray = products.toArray(productsArray);
			productData = FXCollections.observableArrayList(productsArray);
			productTableView.setItems(productData);
			productTableView.refresh();

			// Shows the information message.
			printAlert(AlertType.INFORMATION, "Produkt erstellt", "Produkt wurde registriert!",
					"Das Produkt wurde erstellt und in der Datenbank registriert.");

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

		if (nameField.getText().isEmpty())
			errorMessage += "Produktname fehlt.\n";

		if (descriptionField.getText().isEmpty())
			errorMessage += "Die Beschreibung des Produktes fehlt.\n";

		if (cb1.isSelected() && duration1.getText().isEmpty())
			errorMessage += "Gib an, wie lange das Sägen dieses Produktes dauert.\n";

		if (cb2.isSelected() && duration2.getText().isEmpty())
			errorMessage += "Gib an, wie lange das Bohren dieses Produktes dauert.\n";

		if (cb3.isSelected() && duration3.getText().isEmpty())
			errorMessage += "Gib an, wie lange das Hobeln dieses Produktes dauert.\n";

		if (cb4.isSelected() && duration4.getText().isEmpty())
			errorMessage += "Gib an, wie lange das Politur dieses Produktes dauert.\n";

		if (cb5.isSelected() && duration5.getText().isEmpty())
			errorMessage += "Gib an, wie lange das Verpacken dieses Produktes dauert.\n";

		if (!(cb1.isSelected() || cb2.isSelected() || cb3.isSelected() || cb4.isSelected() || cb5.isSelected()))
			errorMessage += "Dem Produkt fehlt mindestens ein Arbeitsschritt.\n";
		
		if (cb1.isSelected() && duration1.getText().length() > 3)
			errorMessage += "Die maximale Dauer des Sägens beträgt 999 Minuten.\n";
		
		if (cb2.isSelected() && duration2.getText().length() > 3)
			errorMessage += "Die maximale Dauer des Bohrens beträgt 999 Minuten.\n";
		
		if (cb3.isSelected() && duration3.getText().length() > 3)
			errorMessage += "Die maximale Dauer des Hobeln beträgt 999 Minuten.\n";
		
		if (cb4.isSelected() && duration4.getText().length() > 3)
			errorMessage += "Die maximale Dauer des Politur beträgt 999 Minuten.\n";
		
		if (cb5.isSelected() && duration5.getText().length() > 3)
			errorMessage += "Die maximale Dauer des Verpacken beträgt 999 Minuten.\n";

		if (!(errorMessage.length() == 0)) {
			// Show the error message.
			printAlert(AlertType.ERROR, "Fehlende Angaben", "Bitte prüfe deine Eingaben:", errorMessage);
			return false;
		} else
			return true;
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

	public void setProductData(ObservableList<Product> productData) {
		this.productData = productData;
	}

	public void setProductTableView(TableView<Product> productTableView) {
		this.productTableView = productTableView;
	}

}
