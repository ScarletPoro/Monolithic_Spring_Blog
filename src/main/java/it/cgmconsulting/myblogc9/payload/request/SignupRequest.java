package it.cgmconsulting.myblogc9.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class SignupRequest {
	
	@NotBlank @Size(min=3, max=10)
	@Pattern(regexp = "^[a-zA-Z0-9äöüÄÖÜ]*$")
	private String username;
	
	@NotBlank @Size(min=6, max=12)
	private String password; 
	
	@NotBlank @Email @Size(max=150)
	private String email;

}
