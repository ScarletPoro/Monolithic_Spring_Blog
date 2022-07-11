package it.cgmconsulting.myblogc9.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import it.cgmconsulting.myblogc9.entity.dateAudit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Reporting extends DateAudit{
	
	@EmbeddedId
	private ReportingId reportingId;

	@Column(nullable=false, length=18)
	@Enumerated(EnumType.STRING)
	private ReportingStatus status = ReportingStatus.OPEN;

	@ManyToOne
	@JoinColumn(name="reason_id", nullable=false)
	private ReportingReason reasonId;
	
	public Reporting(ReportingId reportingId, ReportingReason reasonId) {
		super();
		this.reportingId = reportingId;
		this.reasonId = reasonId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(reportingId);
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
		Reporting other = (Reporting) obj;
		return Objects.equals(reportingId, other.reportingId);
	}

	

}
