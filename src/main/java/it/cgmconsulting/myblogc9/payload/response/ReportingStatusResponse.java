package it.cgmconsulting.myblogc9.payload.response;

import it.cgmconsulting.myblogc9.entity.ReportingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReportingStatusResponse {
	
	private long commentId;
	private ReportingStatus status;

}
