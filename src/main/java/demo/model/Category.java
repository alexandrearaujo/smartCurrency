package demo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<SubCategory> subCategories;
	
	@OneToMany
	private List<Entry> entries;

}
