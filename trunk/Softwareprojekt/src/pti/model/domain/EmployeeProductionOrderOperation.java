package pti.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_production_order_operation")
public class EmployeeProductionOrderOperation implements Serializable {
    private Long id;
    private Employee employee;
    private ProductionOrderOperation productionOrderOperation;
    private EmployeeProductionOrderOperation before;
    private EmployeeProductionOrderOperation after;
    private int duration;
    private LocalDateTime beginn;
	private LocalDateTime end;
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_operation_id")
    public ProductionOrderOperation getProductionOrderOperation() {
        return productionOrderOperation;
    }

    public void setProductionOrderOperation(ProductionOrderOperation productionOrderOperation) {
        this.productionOrderOperation = productionOrderOperation;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "before_employee_production_order_operation_id")
    public EmployeeProductionOrderOperation getBefore() {
        return before;
    }

    public void setBefore(EmployeeProductionOrderOperation before) {
        this.before = before;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "after_employee_production_order_operation_id")
    public EmployeeProductionOrderOperation getAfter() {
        return after;
    }

    public void setAfter(EmployeeProductionOrderOperation after) {
        this.after = after;
    }

    @Column(name = "duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    @Column(name = "BEGINN")
	@Convert(converter = LocalDateTimeConverter.class)
	public LocalDateTime getBeginn() {
		return beginn;
	}

	public void setBeginn(LocalDateTime beginn) {
		this.beginn = beginn;
	}
	
	@Column(name = "END")
	@Convert(converter = LocalDateTimeConverter.class)
	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "EmployeeProductionOrderOperation [id=" + id + ", employee=" + employee + ", productionOrderOperation="
				+ productionOrderOperation + ", before=" + before + ", after=" + after + ", duration=" + duration
				+ ", beginn=" + beginn + ", end=" + end + "]";
	}
    
	
    
}
