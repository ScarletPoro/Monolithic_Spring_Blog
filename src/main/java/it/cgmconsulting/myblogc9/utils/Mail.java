package it.cgmconsulting.myblogc9.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Mail {
	
	private String mailFrom;
	private String mailTo;
	private String mailSubject;
	private String mailContent;
	private String mailMimeType = "text/plain";

}
