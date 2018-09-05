package pti.model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "MACHINE")
public class Machine implements Serializable{

	private static final long serialVersionUID = 8289748545235381062L;
	private Long id;
	private String name;
	private String description;
	private Set<Operation> operations = new HashSet<Operation>();
	private boolean operative;
	private LocalDateTime notOperativeUntil;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(mappedBy = "machine", fetch = FetchType.LAZY,
			   cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Operation> getOperations() {
		return operations;
	}
	
	public void setOperations(Set<Operation> operations) {
		this.operations = operations;
	}

    @Column(name = "OPERATIVE")
	public boolean isOperative() {
        return operative;
    }

    public void setOperative(boolean operative) {
        this.operative = operative;
    }

    @Column(name = "NOT_OPERATIVE_UNTIL")
    @Convert(converter = LocalDateTimeConverter.class)
    public LocalDateTime getNotOperativeUntil() {
        return notOperativeUntil;
    }

    public void setNotOperativeUntil(LocalDateTime notOperativeUntil) {
        this.notOperativeUntil = notOperativeUntil;
    }

    @Override
	public String toString(){
		return "Mashine : " + name + ", " + description + ", " + operative + ", " + notOperativeUntil;
	}
}
