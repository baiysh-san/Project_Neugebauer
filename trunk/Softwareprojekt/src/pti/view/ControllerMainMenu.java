package pti.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pti.model.domain.Customer;
import pti.model.domain.Order;
import pti.model.domain.Product;
import pti.model.domain.ProductionOrder;
import pti.model.service.CustomerService;
import pti.model.service.OrderService;
import pti.model.service.ProductService;
import pti.model.service.ProductionOrderService;
import pti.test.OrderFactory;

/**
 * This is the controller of the main Menu of the GUI.
 */
public class ControllerMainMenu extends Controller {

	private Order currentOrder;
	// Order Data
	private ObservableList<Order> orderData;
	@FXML
	private TableView<Order> orderTableView = new TableView<>();
	@FXML
	private TableColumn<Order, String> orderIDColumn = new TableColumn<>();

	@FXML
	ImageView background = new ImageView();

	@FXML
	TabPane tp = new TabPane();

	@FXML
	AnchorPane ap = new AnchorPane();

	@FXML
	AnchorPane auftraege = new AnchorPane();

	@FXML
	GridPane orderGp = new GridPane();

	@FXML
	ImageView transparencyOrder = new ImageView();

	@FXML
	ImageView transparencyOrderData = new ImageView();

	@FXML
	MenuBar menuBar = new MenuBar();

	@FXML
	Pane pane = new Pane();

	@FXML
	Pane pane2 = new Pane();

	// Label
	@FXML
	private Label orderLabel = new Label();
	@FXML
	private Label customerLabel = new Label();
	@FXML
	private Label descriptionLabel = new Label();
	@FXML
	private Label productLabel = new Label();
	@FXML
	private Label productionOrderLabel = new Label();

	@FXML
	private AnchorPane productTab = new AnchorPane();
	@FXML
	private AnchorPane employeeTab = new AnchorPane();
	@FXML
	private AnchorPane machineTab = new AnchorPane();
	@FXML
	private AnchorPane ganttEmployeeTab = new AnchorPane();
	@FXML
	private AnchorPane ganttMachineTab = new AnchorPane();

	/**
	 * This method get the oderService data as a list. After them the oder data will
	 * be add into the table view.
	 */
	@Override
	void onInit() {

		checkScreenSize();

		initTabs();

		CustomerService cs = Launcher.ctx.getBean("customerService", CustomerService.class);
		Customer customer = cs.findById(1L);
		ProductService ps = Launcher.ctx.getBean("productService", ProductService.class);
		Product product = ps.findById(1L);

		Order order = OrderFactory.createOrder("Testbestellung", Date.valueOf("2018-08-20"), product, 3, Launcher.ctx);

		customer.getOrders().add(order);
		order.setCustomer(customer);

		customer = cs.update(customer);

		OrderService os = Launcher.ctx.getBean("orderService", OrderService.class);
		List<Order> orders = os.findAll();
		Order[] ordersArray = new Order[orders.size()];
		ordersArray = orders.toArray(ordersArray);

		orderData = FXCollections.observableArrayList(ordersArray);
		orderTableView.setItems(orderData);
		orderIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId() + ""));

		orderTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			refreshDetails(observable, oldValue, newValue);
		});
	}

	@FXML
	private void initTabs() {
		try {
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(this.getClass().getResource("LayoutProductTab.fxml"));
			AnchorPane apProduct = (AnchorPane) loader1.load();
			productTab.getChildren().addAll(apProduct.getChildren());
			ControllerProductOverview c1 = loader1.getController();
			c1.init(primaryStage, loginName);

			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(this.getClass().getResource("LayoutEmployeeTab.fxml"));
			AnchorPane apEmployee = (AnchorPane) loader2.load();
			employeeTab.getChildren().addAll(apEmployee.getChildren());
			ControllerEmployeeOverview c2 = loader2.getController();
			c2.init(primaryStage, loginName);

			FXMLLoader loader3 = new FXMLLoader();
			loader3.setLocation(this.getClass().getResource("LayoutMachineTab.fxml"));
			AnchorPane apMachine = (AnchorPane) loader3.load();
			machineTab.getChildren().addAll(apMachine.getChildren());
			ControllerMachineOverview c3 = loader3.getController();
			c3.init(primaryStage, loginName);

			FXMLLoader loader4 = new FXMLLoader();
			loader4.setLocation(this.getClass().getResource("LayoutEmployeeGantt.fxml"));
			AnchorPane employeeGanttTabPane = (AnchorPane) loader4.load();
			ganttEmployeeTab.getChildren().addAll(employeeGanttTabPane.getChildren());
			ControllerEmployeeGantt c4 = loader4.getController();
			c4.init(primaryStage, loginName);

			FXMLLoader loader5 = new FXMLLoader();
			loader5.setLocation(this.getClass().getResource("LayoutMachineGantt.fxml"));
			AnchorPane machineGanttTabPane = (AnchorPane) loader5.load();
			ganttMachineTab.getChildren().addAll(machineGanttTabPane.getChildren());
			ControllerMachineGantt c5 = loader5.getController();
			c5.init(primaryStage, loginName);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method refreshs the details of the table. Changes the views for details,
	 * product and productionOrder.
	 * 
	 * @param observable
	 *            - to observe the data
	 * @param oldValue
	 *            - the old value of the data
	 * @param newValue
	 *            - the new value of the data
	 */
	private void refreshDetails(ObservableValue<? extends Order> observable, Order oldValue, Order newValue) {
		currentOrder = newValue;

		CustomerService cs = Launcher.ctx.getBean(CustomerService.class);
		ProductService ps = Launcher.ctx.getBean(ProductService.class);
		ProductionOrderService pos = Launcher.ctx.getBean(ProductionOrderService.class);

		// Details View
		Customer customer = cs.findByOrder(newValue);
		Product product = ps.findByOrder(newValue);
		ProductionOrder productionOrder = pos.findByOrder(newValue);

		orderLabel.setText(newValue.getId() + "");
		customerLabel.setText(customer.getLastName() + ", " + customer.getFirstName());
		descriptionLabel.setText(newValue.getDescription());

		productLabel.setText(product.getName());
		productionOrderLabel.setText(productionOrder.getDateOfDelivery().toString());

	}

	@FXML
	private void handleNewOrder() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("LayoutNewOrder.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Neuer Auftrag");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(anchorPane);

			dialogStage.setScene(scene);

			// Set the provider into the controller.
			ControllerNewOrder controller = loader.getController();
			controller.init(primaryStage, loginName);
			controller.setDialogStage(dialogStage);
			controller.setOrderData(orderData);
			controller.setOrderTableView(orderTableView);

			// Show the dialog and wait until the user closes it.
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method deletes the current Order.
	 */
	@FXML
	private void handleDeleteOrder() {

		OrderService os = Launcher.ctx.getBean("orderService", OrderService.class);

		if (!(os.findAll().size() < 1)) {

			Order currentOrder = orderTableView.getSelectionModel().getSelectedItem();

			if (!orderTableView.getSelectionModel().isEmpty()) {
				orderData.remove(currentOrder);
				os.delete(currentOrder);

				if (orderTableView.getItems().contains(currentOrder))
					orderTableView.getItems().remove(currentOrder);
				
				printAlert(AlertType.INFORMATION, "Auftrag gelöscht", "Auftrag wurde erfolgreich gelöscht!",
						"Der ausgewählte Auftrag wurde erfolgreich aus der Datenbank entfernt.");
				
			} else {
				printAlert(AlertType.ERROR, "Keine Auswahl", "Was soll gelöscht werden?",
						"Bitte wähle einen Auftrag aus, den du löschen möchtest.");
			}
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);

			ButtonType buttonTypeYes = new ButtonType("Ja");
			ButtonType buttonTypeNo = new ButtonType("Nein");

			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			alert.initOwner(primaryStage);
			alert.setTitle("Keine Aufträge");
			alert.setHeaderText("Keine Aufträge in der Datenbank gefunden.");
			alert.setContentText("Möchten Sie einen neuen Auftrag anlegen?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeYes) {
				handleNewOrder();
			} else {
				if (result.get() == buttonTypeNo) {

				}
			}
		}
	}

	/**
	 * This method shows the machine gantt diagramm by opening a new scene.
	 */
	@FXML
	private void handleMachineGantt() {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(this.getClass().getResource("LayoutMachineGantt.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			ControllerMachineGantt c = loader.getController();
			c.init(primaryStage, loginName);

			primaryStage.setScene(new Scene(anchorPane));

			primaryStage.centerOnScreen();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method shows the employee gantt diagramm by opening a new scene.
	 */
	@FXML
	private void handleEmployeeGantt() {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(this.getClass().getResource("LayoutEmployeeGantt.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			ControllerEmployeeGantt c = loader.getController();
			c.init(primaryStage, loginName);

			primaryStage.setScene(new Scene(anchorPane));

			primaryStage.centerOnScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void handleClose() {
		primaryStage.close();
	}

	@FXML
	protected void handleLogout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("LayoutLogin.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			ControllerLogin c = loader.getController();
			c.init(primaryStage, "");

			primaryStage.setScene(new Scene(anchorPane));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		printAlert(AlertType.INFORMATION, "Abmeldung", "Abmeldung war erfolgreich.", "Sie wurden erfolgreich aus Ihrem Account abgemeldet.");
		
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

				menuBar.setMinWidth(width);

				orderTableView.setPrefSize(pane.getWidth(), pane.getHeight());

				orderIDColumn.setMinWidth(pane.getWidth());

				transparencyOrder.setFitWidth(pane.getWidth());
				transparencyOrder.setFitHeight(pane.getHeight());

				orderGp.setPrefSize(pane2.getWidth(), pane2.getHeight());

				transparencyOrderData.setFitWidth(pane2.getWidth());
				transparencyOrderData.setFitHeight(pane2.getHeight());

				timer.stop();
			}
		};
		new Timer(200, taskPerformer).start();
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

}
