package it.cgmconsulting.myblogc9.entity.dateAudit;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public class DateAudit {
	
	@CreationTimestamp
	@Column(updatable=false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, updatedAt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateAudit other = (DateAudit) obj;
		return Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt);
	}

}
