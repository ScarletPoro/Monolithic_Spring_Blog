package it.cgmconsulting.myblogc9.payload.response;

import java.time.LocalDateTime;

import it.cgmconsulting.myblogc9.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ReportingResponse {

	private User author;
	private LocalDateTime updatedAt;
	private int severity;
	private String reason;

}