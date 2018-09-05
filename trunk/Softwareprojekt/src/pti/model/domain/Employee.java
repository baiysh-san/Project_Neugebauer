package pti.model.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYEE")
public class Employee implements Serializable{

	private static final long serialVersionUID = -4790483870070753601L;
	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private boolean available;
	private Date notAvailableUntil;
	private Set<EmployeeProductionOrderOperation> employeeProductionOrderOperations
            = new HashSet<>();

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "AVAILABLE")
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Column(name = "NOT_AVAILABLE_UNTIL")
    public Date getNotAvailableUntil() {
        return notAvailableUntil;
    }

    public void setNotAvailableUntil(Date notAvailableUntil) {
        this.notAvailableUntil = notAvailableUntil;
    }

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<EmployeeProductionOrderOperation> getEmployeeProductionOrderOperations() {
        return employeeProductionOrderOperations;
    }

    public void setEmployeeProductionOrderOperations(Set<EmployeeProductionOrderOperation> employeeProductionOrderOperations) {
        this.employeeProductionOrderOperations = employeeProductionOrderOperations;
    }

    @Override
	public String toString() {
		return "Employee : " + firstName + ", " + lastName + ", " + address + ", "
                + available + ", " + notAvailableUntil;
	}
	
}