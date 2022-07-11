package it.cgmconsulting.myblogc9.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
//@Table(name="nuova_tabella_tag")
@Getter @Setter @NoArgsConstructor @ToString
public class Tag {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false, unique=true, length=15)
	private String tagName; // tag_name
	
	private boolean isVisible;
	
	@ManyToMany
	@JoinTable(name="post_tags",
		joinColumns = {@JoinColumn(name="tag_id", referencedColumnName="id")},
		inverseJoinColumns = {@JoinColumn(name="post_id", referencedColumnName="id")})
	private Set<Post> posts = new HashSet<Post>();

	public Tag(String tagName) {
		super();
		this.tagName = tagName;
		this.isVisible = true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		return id == other.id;
	}

}
