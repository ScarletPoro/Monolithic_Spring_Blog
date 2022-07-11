package it.cgmconsulting.myblogc9.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class ReportingReasonRequest {
	
	@NotBlank @Size(min=1, max=50)
	private String reason;
	
	@Min(1)
	private int severity;
	
	@NotNull
	private LocalDate startValidity;

	private LocalDate endValidity;
}
