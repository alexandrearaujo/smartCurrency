package demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class AccountMonthly implements Serializable {
	
	private static final long serialVersionUID = -6980790888660404340L;

	@Id
	@GeneratedValue
	private Long id;
	
	private Integer month;
	
	private Integer year;
	
	private BigDecimal amount;
	
	@ManyToOne
	private Account account;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Entry> entries;

}
