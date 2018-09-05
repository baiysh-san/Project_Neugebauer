package pti.model.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCTION_ORDER_OPERATION")
public class ProductionOrderOperation {
	private Long id;
	private LocalDateTime beginn;
	private LocalDateTime end;
	private ProductionOrder productionOrder;
	private Operation operation;
	private List<EmployeeProductionOrderOperation> employeeProductionOrderOperations = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCTION_ORDER_ID")
	public ProductionOrder getProductionOrder() {
		return productionOrder;
	}
	
	public void setProductionOrder(ProductionOrder productionOrder) {
		this.productionOrder = productionOrder;
	}
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATION_ID")
	public Operation getOperation() {
		return operation;
	}
	
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

    @OneToMany(mappedBy = "productionOrderOperation", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    public List<EmployeeProductionOrderOperation> getEmployeeProductionOrderOperations() {
        return employeeProductionOrderOperations;
    }

    public void setEmployeeProductionOrderOperations(List<EmployeeProductionOrderOperation> employeeProductionOrderOperations) {
        this.employeeProductionOrderOperations = employeeProductionOrderOperations;
    }

	@Override
	public String toString() {
		return "ProductionOrderOperation : beginn - "+beginn+", end - "+end;
	}
}
