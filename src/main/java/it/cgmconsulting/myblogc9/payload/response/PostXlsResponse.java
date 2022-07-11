package it.cgmconsulting.myblogc9.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class PostXlsResponse {
	
	private long id;
	private String title;
	private double average;
	private boolean published;
	private String author;

}
