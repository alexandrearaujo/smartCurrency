package demo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CurrentAccount extends Account implements Serializable {
	
	@ManyToOne
	private Bank bank;
	
	private String account;
	
	private String agency;

}
