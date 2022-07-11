package it.cgmconsulting.myblogc9.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.Check;

import it.cgmconsulting.myblogc9.entity.dateAudit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Check(constraints="rate > 0 AND rate < 6")
public class Rating extends DateAudit{
	
	// Rating r = new Rating(new RatingId(user, post), rate);
	
	@EmbeddedId
	private RatingId ratingId;
	
	@Column(nullable=false)
	private byte rate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(ratingId);
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
		Rating other = (Rating) obj;
		return Objects.equals(ratingId, other.ratingId);
	}

}
