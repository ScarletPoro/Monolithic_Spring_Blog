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
public class Comment extends DateAudit {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false)
	private String comment;
	
	private boolean censored;
	
	@ManyToOne
	@JoinColumn(name="author", nullable=false)
	private User author;
	
	@ManyToOne
	@JoinColumn(name="post_id", nullable=false)
	private Post postId;

	public Comment(String comment, User author, Post postId) {
		super();
		this.comment = comment;
		this.author = author;
		this.postId = postId;
		this.censored = false;
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
		Comment other = (Comment) obj;
		return id == other.id;
	}

}
