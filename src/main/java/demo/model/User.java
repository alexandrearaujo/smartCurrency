package demo.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NaturalId;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@NaturalId
	private String username;
	
	private String password;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Person person;
	
	@OneToOne(cascade = CascadeType.ALL)
	private UserSpace userSpace;

}
