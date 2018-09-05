package pti.view;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pti.model.domain.Customer;
import pti.model.domain.Order;
import pti.model.domain.Product;
import pti.model.service.CustomerService;
import pti.model.service.OrderService;
import pti.model.service.ProductService;
import pti.test.OrderFactory;

public class ControllerNewOrder extends Controller {

	private Stage dialogStage;
	private ObservableList<Order> orderData;
	private TableView<Order> orderTableView;

	@FXML
	private ComboBox<Product> productComboBox = new ComboBox<>();
	@FXML
	private ComboBox<Customer> customerComboBox = new ComboBox<>();

	@FXML
	private TextArea descriptionInput = new TextArea();
	@FXML
	private TextField amount;
	@FXML
	private DatePicker datePicker = new DatePicker();
	
	GregorianCalendar now = new GregorianCalendar(); 
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);   

	private String errorMessage;

	@Override
	void onInit() {
		
		CustomerService cs = Launcher.ctx.getBean(CustomerService.class);
		ProductService ps = Launcher.ctx.getBean(ProductService.class);

		// Details View
		List<Customer> customers = cs.findAll();
		customerComboBox.getItems().addAll(customers);

		List<Product> products = ps.findAll();
		productComboBox.getItems().addAll(products);
	}

	/**
	 * Method to set the dialog Stage unresizable and with a icon.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.getIcons().add(new Image("file:src/pti/view/Icon.png"));
		this.dialogStage.setResizable(false);
	}

	@FXML
	public void handleCancel() {
		dialogStage.close();
	}

	@FXML
	private void handleCreate() {

		errorMessage = "";

		if (customerComboBox.getSelectionModel().isEmpty() == true)
			errorMessage += "Bitte wähle einen Kunden aus!\n";

		if (descriptionInput.getText().isEmpty())
			errorMessage += "Bitte gib eine Beschreibung ein!\n";

		if (productComboBox.getSelectionModel().isEmpty() == true)
			errorMessage += "Bitte wähle ein Produkt aus!\n";

		if (amount.getText().isEmpty())
			errorMessage += "Bitte gib die Anzahl des Produktes an!\n";
		
		if (amount.getText().length() > 3)
			errorMessage += "Die maximale Anzahl pro Produkt beträgt 999!\n";

		if (datePicker.getValue() == null)
			errorMessage += "Bitte wähle ein Lieferdatum aus!\n";
		
		if (datePicker.getValue().isBefore(LocalDate.now()))
			errorMessage += "Bitte wähle ein Datum aus, welches in der Zukunft liegt!\n";

		if (!(errorMessage.length() == 0)) {
			// Show the error message.
			printAlert(AlertType.ERROR, "Informationen fehlen", "Bitte gib folgende Informationen an.", errorMessage);
		} else {

			CustomerService cs = Launcher.ctx.getBean("customerService", CustomerService.class);
			Customer customer = cs.findById(customerComboBox.getValue().getId());
			ProductService ps = Launcher.ctx.getBean("productService", ProductService.class);
			Product product = ps.findById(productComboBox.getValue().getId());

			Order order = OrderFactory.createOrder(descriptionInput.getText(), Date.valueOf(datePicker.getValue()),
					product, Integer.parseInt(amount.getText()), Launcher.ctx);

			customer.getOrders().add(order);
			order.setCustomer(customer);

			customer = cs.update(customer);
			
			printAlert(AlertType.INFORMATION, "Auftrag erstellt", "Auftragserstellung war erfolgreich.",
					"Der Auftrag für den Kunden " + customer + " wurde erfolgreich erstellt.");

			OrderService os = Launcher.ctx.getBean("orderService", OrderService.class);
			List<Order> orders = os.findAll();
			Order[] ordersArray = new Order[orders.size()];
			ordersArray = orders.toArray(ordersArray);
			orderData = FXCollections.observableArrayList(ordersArray);
			orderTableView.setItems(orderData);
			orderTableView.refresh();

			dialogStage.close();

		}
	}

	public void setOrderData(ObservableList<Order> orderData) {
		this.orderData = orderData;
	}

	public void setOrderTableView(TableView<Order> orderTableView) {
		this.orderTableView = orderTableView;
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

}
