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
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pti.model.domain.Machine;
import pti.model.domain.Operation;
import pti.model.domain.Product;
import pti.model.service.MachineService;
import pti.model.service.OperationService;
import pti.model.service.ProductService;

/**
 * This is the controller class of the LayoutProductOverview witch extends
 * Controller.
 */
public class ControllerProductOverview extends Controller {

	private Product currentProduct;

	private ObservableList<Product> productData;
	@FXML
	private TableView<Product> productTableView = new TableView<>();
	@FXML
	private TableColumn<Product, String> productIDColumn = new TableColumn<>();
	@FXML
	private TableColumn<Product, String> productNameColumn = new TableColumn<>();

	private ObservableList<Operation> operationData;
	@FXML
	private TableView<Operation> operationTableView = new TableView<>();
	@FXML
	private TableColumn<Operation, String> operationIDColumn = new TableColumn<>();
	@FXML
	private TableColumn<Operation, String> operationNameColumn = new TableColumn<>();
	@FXML
	private TableColumn<Operation, String> operationTimeColumn = new TableColumn<>();

	@FXML
	private TextArea description = new TextArea();
	
	@FXML
	private GridPane gp = new GridPane();
	
	@FXML
	private ImageView transparency = new ImageView();
	
	@FXML
	private Label labelProducts = new Label();
	@FXML
	private Label labelDescription = new Label();
	@FXML
	private Label labelWorkingSteps = new Label();
	
	/**
	 * This method will be called when the LayoutProductOverview is started. It load
	 * the productService data out of our database and add them to the tableView.
	 */
	@Override
	void onInit() {
		
		checkScreenSize();
		
		ProductService ps = Launcher.ctx.getBean("productService", ProductService.class);
		List<Product> products = ps.findAll();
		Product[] productsArray = new Product[products.size()];
		productsArray = products.toArray(productsArray);

		productData = FXCollections.observableArrayList(productsArray);
		productTableView.setItems(productData);
		productIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId() + ""));
		productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

		productTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			refreshDetails(observable, oldValue, newValue);
		});
	}

	private void refreshDetails(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
		currentProduct = newValue;
		if (currentProduct == null) {
			description.setText("");
		} else {
			description.setText(currentProduct.getDescription());
			OperationService os = Launcher.ctx.getBean("operationService", OperationService.class);
			List<Operation> operations = os.findByProduct(currentProduct);
			Operation[] operationsArray = new Operation[operations.size()];
			operationsArray = operations.toArray(operationsArray);

			operationData = FXCollections.observableArrayList(operationsArray);
			operationTableView.setItems(operationData);
			operationIDColumn
					.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId() + ""));
			operationNameColumn
					.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
			operationTimeColumn.setCellValueFactory(
					cellData -> new SimpleStringProperty(cellData.getValue().getDuration() + " Minuten"));
		}
	}

	/**
	 * This method will add another product to the database after filling in the
	 * data to the dialogStage.*
	 */
	@FXML
	private void handleAdd() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("LayoutAddProduct.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Neues Produkt");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(anchorPane);

			dialogStage.getIcons().add(new Image("file:src/pti/view/Icon.png"));
			dialogStage.setScene(scene);

			// Set the provider into the controller.
			ControllerAddProduct controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setProductData(productData);
			controller.setProductTableView(productTableView);
			controller.init(primaryStage, loginName);

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
		
		ProductService ps = Launcher.ctx.getBean("productService", ProductService.class);

		if (!(ps.findAll().size() < 1)) {

			Product currentProduct = productTableView.getSelectionModel().getSelectedItem();

			if (!productTableView.getSelectionModel().isEmpty()) {
				productData.remove(currentProduct);
				ps.delete(currentProduct);

				if (productTableView.getItems().contains(currentProduct))
					productTableView.getItems().remove(currentProduct);

				printAlert(AlertType.INFORMATION, "Produkt gelöscht", "Produkt wurde entfernt!", "Das Produkt wurde aus der Datenbank entfernt.");

			} else {
				printAlert(AlertType.ERROR, "Keine Auswahl", "Was soll gelöscht werden?",
						"Bitte wähle ein Produkt aus, das du löschen möchtest.");
			}
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);

			ButtonType buttonTypeYes = new ButtonType("Ja");
			ButtonType buttonTypeNo = new ButtonType("Nein");

			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			alert.initOwner(primaryStage);
			alert.setTitle("Keine Produkte");
			alert.setHeaderText("Keine Produkte in der Datenbank gefunden.");
			alert.setContentText("Möchten Sie ein neues Produkt erstellen?");

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

				gp.setPrefSize(width-90, height-300);
				
				transparency.setFitWidth(width-90);
				transparency.setFitHeight(height-275);
				
				productIDColumn.setMinWidth((25*productTableView.getWidth()/100)-5);
				
				productNameColumn.setMinWidth((75*productTableView.getWidth()/100)-5);
				
				operationIDColumn.setMinWidth((20*operationTableView.getWidth()/100)-5);
				
				operationNameColumn.setMinWidth((40*operationTableView.getWidth()/100)-5);
				
				operationTimeColumn.setMinWidth((40*operationTableView.getWidth()/100)-5);
				
				labelProducts.setMinWidth(productTableView.getWidth());
				
				labelWorkingSteps.setMinWidth(operationTableView.getWidth());
				timer.stop();
			}

		};
		new Timer(200, taskPerformer).start();
	}

}
