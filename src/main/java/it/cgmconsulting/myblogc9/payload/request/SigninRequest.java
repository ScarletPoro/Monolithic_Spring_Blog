package it.cgmconsulting.myblogc9.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class SigninRequest {
	
	@NotBlank
	private String usernameOrEmail;
	
	@NotBlank @Size(min=6, max=12)
	private String password;

}
