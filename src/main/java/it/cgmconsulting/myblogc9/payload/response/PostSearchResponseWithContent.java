package it.cgmconsulting.myblogc9.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostSearchResponseWithContent {
	
	private long id;
	private String title;
	private String content;
	
}
