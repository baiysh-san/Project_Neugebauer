package pti.model.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OPERATION")
public class Operation implements Serializable {

	private static final long serialVersionUID = 7071047428217283447L;
	private Long id;
	private String name;
	private int duration;
	private Workschedule workschedule;
	private Machine machine;
	private Set<ProductionOrderOperation> productionOrderOperations = new HashSet<>();
	private Operation beforeOperation;
	private Operation afterOperation;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DURATION")
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	@ManyToOne( fetch = FetchType.LAZY) 
	@JoinColumn(name = "WORKSCHEDULE_ID")
	public Workschedule getWorkschedule() {
		return workschedule;
	}
	
	public void setWorkschedule(Workschedule workschedule) {
		this.workschedule = workschedule;
	}

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "machine_id")
	public Machine getMachine() {
		return machine;
	}
	
	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	@OneToMany(mappedBy="operation", fetch = FetchType.LAZY,
			   cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ProductionOrderOperation> getProductionOrderOperations() {
		return productionOrderOperations;
	}

	public void setProductionOrderOperations(Set<ProductionOrderOperation> productionOrderOperations) {
		this.productionOrderOperations = productionOrderOperations;
	}
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "before_operation_id")
	public Operation getBeforeOperation() {
		return beforeOperation;
	}

	public void setBeforeOperation(Operation beforeOperation) {
		this.beforeOperation = beforeOperation;
	}
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "after_operation_id")
	public Operation getAfterOperation() {
		return afterOperation;
	}

	public void setAfterOperation(Operation afterOperation) {
		this.afterOperation = afterOperation;
	}

	@Override
	public String toString() {
		return "Operation [id=" + id + ", name=" + name + ", duration=" + duration + "]";
	}
	
}
