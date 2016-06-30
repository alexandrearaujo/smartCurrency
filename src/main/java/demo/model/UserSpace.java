package demo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserSpace implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	@Column(unique = true, nullable = false)
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Person> persons;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<CreditCard> creditCards;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Account> accounts;

}
