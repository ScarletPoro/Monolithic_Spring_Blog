package it.cgmconsulting.myblogc9.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import it.cgmconsulting.myblogc9.entity.dateAudit.DateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Post extends DateAudit{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false, length=30)
	private String title;
	
	@Column(nullable=false, length=15000)
	private String content;
	
	@Column(nullable=false, length=250)
	private String overview;
	
	private boolean published;
	
	private String image;
	
	@ManyToOne
	@JoinColumn(name="author", nullable=false)
	private User author;

	public Post(String title, String content, String overview, User author) {
		super();
		this.title = title;
		this.content = content;
		this.overview = overview;
		this.author = author;
		this.published = false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		return id == other.id;
	}

}
