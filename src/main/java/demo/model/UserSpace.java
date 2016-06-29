package demo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserSpace implements Serializable {
	
	@OneToOne
	private User user;
	
	@OneToMany
	private List<Person> persons;
	
	@OneToMany
	private List<CreditCard> creditCards;
	
	@OneToMany
	private List<Account> accounts;

}
