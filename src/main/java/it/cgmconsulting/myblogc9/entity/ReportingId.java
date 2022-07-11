package it.cgmconsulting.myblogc9.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReportingId implements Serializable{

	@ManyToOne
	@JoinColumn(name="comment_id")
	private Comment comment;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@Override
	public int hashCode() {
		return Objects.hash(comment, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportingId other = (ReportingId) obj;
		return Objects.equals(comment, other.comment) && Objects.equals(user, other.user);
	}
	
	private static final long serialVersionUID = 1L;

}
