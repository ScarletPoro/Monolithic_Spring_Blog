package it.cgmconsulting.myblogc9.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class CommentRequest {
	
	@NotBlank @Size(max=255)
	private String commentText;

}
