package demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NaturalId;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Entry implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@NaturalId
	private String ident;
	
	private String description;
	
	private String type;
	
	private String location;
	
	private Integer portion;
	
	private Integer portionQuant;
	
	private LocalDate date;
	
	private BigDecimal value;
	
	@ManyToOne
	private CreditCardBill bill;

}
