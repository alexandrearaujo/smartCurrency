package demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CreditCard implements Serializable {
	
	private static final long serialVersionUID = -4779418085181047025L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String number;
	
	private String label;
	
	private LocalDate dueDate;
	
	private BigDecimal creditLimit;
	
	@OneToMany
	private List<CreditCardBill> bills;
	
}
