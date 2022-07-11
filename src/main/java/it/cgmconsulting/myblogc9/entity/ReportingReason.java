package it.cgmconsulting.myblogc9.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Check;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Check(constraints="severity > 0")
public class ReportingReason {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;

	@Column(nullable = false, length = 50)
	private String reason;

	private int severity;

	@Column(nullable = false)
	private LocalDate startValidity;

	private LocalDate endValidity;

	public ReportingReason(String reason, int severity, LocalDate startValidity, LocalDate endValidity) {
		super();
		this.reason = reason;
		this.severity = severity;
		this.startValidity = startValidity;
		this.endValidity = endValidity;
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
		ReportingReason other = (ReportingReason) obj;
		return id == other.id;
	}
	
}
