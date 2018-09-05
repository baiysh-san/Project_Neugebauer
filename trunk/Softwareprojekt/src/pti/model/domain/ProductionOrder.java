package pti.model.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "PRODUCTION_ORDER")
public class ProductionOrder implements Serializable{

	private static final long serialVersionUID = 2870394707941155649L;
	private Long id;
	private Date dateOfDelivery;
	private Workschedule workschedule;
	private List<ProductionOrderOperation> productionOrderOperations = new ArrayList<ProductionOrderOperation>();
	private Order order;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DATE_OF_DELIVERY")
	public Date getDateOfDelivery() {
		return dateOfDelivery;
	}
	
	public void setDateOfDelivery(Date dateOfDelivery) {
		this.dateOfDelivery = dateOfDelivery;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORKSCHEDULE_ID")
	public Workschedule getWorkschedule() {
		return workschedule;
	}
	
	public void setWorkschedule(Workschedule workschedule) {
		this.workschedule = workschedule;
	}
	
	@OneToMany(mappedBy = "productionOrder", fetch = FetchType.LAZY, 
			   cascade = CascadeType.PERSIST, orphanRemoval = true)
	public List<ProductionOrderOperation> getProductionOrderOperations() {
		return productionOrderOperations;
	}

	public void setProductionOrderOperations(List<ProductionOrderOperation> productionOrderOperations) {
		this.productionOrderOperations = productionOrderOperations;
	}
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "ORDER_ID")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "ProductionOrder : "+dateOfDelivery;
	}
}
