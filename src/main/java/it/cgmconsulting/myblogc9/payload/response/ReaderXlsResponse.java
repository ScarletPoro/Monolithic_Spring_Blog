package it.cgmconsulting.myblogc9.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class ReaderXlsResponse {
	
	private long id;
	private String username;
	private long commentsWritten;
	private long bans;
	private boolean enabled;

}
