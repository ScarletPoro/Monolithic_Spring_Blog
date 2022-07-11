package it.cgmconsulting.myblogc9.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class PostRequest {
	
	@NotBlank @Size(min=1, max=30)
	private String title;
	
	@NotBlank @Size(min=1, max=15000)
	private String content;
	
	@NotBlank @Size(min=1, max=250)
	private String overview;

}
