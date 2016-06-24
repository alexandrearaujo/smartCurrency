package demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class CreditCardBill implements Serializable {
	
	private static final long serialVersionUID = -7138168796166683476L;

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private CreditCard creditCard;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Entry> entries;
	
	private LocalDate closingDate;
	
	private BigDecimal amount;

}
