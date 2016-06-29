package demo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Person implements Serializable {
	
	private static final long serialVersionUID = 306594414386792487L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToOne
	private User user;
	
	@OneToMany
	private List<Entry> entries;
	
	@ManyToOne
	private UserSpace userSpace;

}
