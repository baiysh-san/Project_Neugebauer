package pti.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import pti.model.domain.Employee;
import pti.model.domain.EmployeeProductionOrderOperation;
import pti.model.domain.Machine;
import pti.model.domain.Order;
import pti.model.service.EmployeeService;
import pti.model.service.MachineService;
import pti.model.service.OrderService;
import pti.test.JobShop;
import pti.view.GanttChart.ExtraData;

/**
 * This class is the controller of the gantt diagramm of the employee.
 */
public class ControllerEmployeeGantt extends Controller {

	private GanttChart<Number, String> chart;
	private List<Machine> machineList;
	private List<Employee> employeeList;
	private List<Order> orderList;
	private OrderService os;

	@FXML
	private AnchorPane ap;
	@FXML
	private DatePicker dateInput;

	@FXML
	private MenuBar mb = new MenuBar();

	@FXML
	private GridPane gp = new GridPane();

	@FXML
	private Pane p = new Pane();

	@FXML
	private ImageView transparency = new ImageView();

	@FXML
	/**
	 * if the initialize method will called they add text to the legend.
	 */
	@Override
	void onInit() {
		dateInput.setValue(LocalDate.now());
		dateInput.setOnAction(e -> {
			runAlgorithm();
		});
		runAlgorithm();

		checkScreenSize();
		
	}

	@FXML
	private void runAlgorithm() {
		MachineService ms = Launcher.ctx.getBean("machineService", MachineService.class);
		machineList = ms.findAll();

		EmployeeService es = Launcher.ctx.getBean("employeeService", EmployeeService.class);
		employeeList = es.findAll();

		os = Launcher.ctx.getBean("orderService", OrderService.class);
		orderList = os.findAll();

		int workingDayBeginTime = 8;
		int workingDayDuration = 12;
		int breakDurationMinutes = 30;
		int maxHoursWithoutBreak = 4;
		int maxAllowedHoursForWorker = 8;

		JobShop jobShop = new JobShop(workingDayBeginTime, workingDayDuration, breakDurationMinutes,
				maxHoursWithoutBreak, maxAllowedHoursForWorker, machineList, employeeList, orderList, os);
		jobShop.run();

		initializeGanttChart();
	}

	@SuppressWarnings("unchecked")
	private void initializeGanttChart() {
		ap.getChildren().remove(chart);

		List<String> employeeNamesList = new ArrayList<String>();
		for (Employee e : employeeList) {
			String name = e.getLastName() + ", " + e.getFirstName();
			employeeNamesList.add(name);
		}

		String[] employees = new String[employeeNamesList.size()];
		employees = employeeNamesList.toArray(employees);

		final NumberAxis xAxis = new NumberAxis();
		final CategoryAxis yAxis = new CategoryAxis();

		chart = new GanttChart<Number, String>(xAxis, yAxis);

		xAxis.setTickLabelFill(Color.WHITE);
		xAxis.setMinorTickCount(4);

		yAxis.setTickLabelFill(Color.WHITE);
		yAxis.setTickLabelGap(10);

		chart.setLegendVisible(false);
		chart.setBlockHeight(20);
		chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

		String employee;
		String status;

		for (int i = 0; i < employeeList.size(); i++) {
			Employee e = employeeList.get(i);
			employee = employeeNamesList.get(i);
			Random r = new Random();
			double d = r.nextDouble();
			if (d > 0.3d) {
				status = "status-green";
			} else if (d > 0.1d && d < 0.3d) {
				status = "status-blue";
			} else {
				status = "status-red";
			}
			int deltaTime = 0;
			for (EmployeeProductionOrderOperation epoo : e.getEmployeeProductionOrderOperations()) {
				System.out.println(epoo.getDuration());
				int duration = epoo.getDuration()
						+ (int) ChronoUnit.DAYS.between(LocalDate.now(), dateInput.getValue()) * 100;

				if (deltaTime + duration < 720) {
					XYChart.Series<Number, String> series = new XYChart.Series<Number, String>();
					series.getData().add(
							new XYChart.Data<Number, String>(deltaTime, employee, new ExtraData(duration, status)));
					chart.getData().add(series);
					deltaTime += duration;
				}
			}

		}
		chart.setPrefWidth(890);
		ap.getChildren().add(chart);
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

				ap.setMinSize(width, height);

				chart.setPrefSize(ap.getWidth() - 300, ap.getHeight());

				transparency.setFitWidth(p.getWidth());

				mb.setMinSize(ap.getWidth(), 35);

				if (width > 1500 && height > 800) {
					chart.getXAxis().setStyle("-fx-tick-label-font-size:22px;");
					chart.getYAxis().setStyle("-fx-tick-label-font-size:22px;");
					chart.setBlockHeight(35);
				} else {
					chart.getXAxis().setStyle("-fx-tick-label-font-size:17px;");
					chart.getYAxis().setStyle("-fx-tick-label-font-size:17px;");
					chart.setBlockHeight(20);
				}

				timer.stop();
			}

		};
		new Timer(200, taskPerformer).start();
	}
}