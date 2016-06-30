package demo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class SubCategory implements Serializable {
	

	@Id
	@GeneratedValue
	private Long id;
	
	private String description;
	
	@ManyToOne
	private Category category;
	
	@OneToMany
	private List<Entry> entries;
	
}
