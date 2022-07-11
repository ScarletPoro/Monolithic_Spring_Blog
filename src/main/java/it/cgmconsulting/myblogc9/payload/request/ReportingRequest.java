package it.cgmconsulting.myblogc9.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class ReportingRequest {
	
	private long commentId;
	
	@NotBlank @Size(min=1, max=50)
	private String reason;

}
