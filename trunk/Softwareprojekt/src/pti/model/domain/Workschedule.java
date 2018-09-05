package pti.model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WORKSCHEDULE")
public class Workschedule implements Serializable{

	private static final long serialVersionUID = 5073064140025813828L;
	private Long id;
	private String description;
	private Set<ProductionOrder> productionOrders = new HashSet<>();
	private List<Operation> operations = new ArrayList<>();
	private Product product;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@OneToMany(mappedBy = "workschedule", fetch = FetchType.LAZY, 
			   cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ProductionOrder> getProductionOrders() {
		return productionOrders;
	}

	public void setProductionOrders(Set<ProductionOrder> productionOrders) {
		this.productionOrders = productionOrders;
	}

	@OneToMany(mappedBy="workschedule", fetch = FetchType.LAZY,
			   cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "Workschedule : "+description;
	}

}
