package demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Account implements Serializable {
	
	private static final long serialVersionUID = 7343111853218639155L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String label;
	
	private BigDecimal balance;
	
	@ManyToOne
	@Column(unique = true, nullable = false)
	private UserSpace userSpace;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<AccountMonthly> monthlyEntries;
	
}
