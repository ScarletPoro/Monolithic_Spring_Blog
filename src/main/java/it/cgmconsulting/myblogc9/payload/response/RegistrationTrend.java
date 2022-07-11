package it.cgmconsulting.myblogc9.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegistrationTrend {
	
	private long registrationNumber;
	private String registrationDate;

}
