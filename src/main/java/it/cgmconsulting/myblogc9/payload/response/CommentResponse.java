package it.cgmconsulting.myblogc9.payload.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentResponse {
	
	private long id;
	private String comment;
	private String author; // username di User
	private LocalDateTime createdAt;

}
