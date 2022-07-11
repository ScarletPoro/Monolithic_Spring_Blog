package it.cgmconsulting.myblogc9.payload.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
	
	private String username;
	private String email;
	private LocalDateTime createdAt;

}
