package it.cgmconsulting.myblogc9.payload.response;

import it.cgmconsulting.myblogc9.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PostResponse {
	
	private long id;
	private String title;
	private String overview;
	private String image;
	private String link = Constants.POST_LINK;
	private double average;
	private long commentsNr;
	
	public PostResponse(long id, String title, String overview, String image, double average, long commentsNr) {
		super();
		this.id = id;
		this.title = title;
		this.overview = overview;
		this.image = image;
		this.link = link+id;
		this.average = average;
		this.commentsNr = commentsNr;
	}
	
	public PostResponse(long id, String title, String overview, String image, double average) {
		super();
		this.id = id;
		this.title = title;
		this.overview = overview;
		this.image = image;
		this.link = link+id;
		this.average = average;
		
	}
	
	public PostResponse(long id, String title, String overview, String image) {
		super();
		this.id = id;
		this.title = title;
		this.overview = overview;
		this.image = image;
		this.link = link+id;	
	}
	
	public PostResponse(long id, String title, String overview, String image, long commentsNr) {
		super();
		this.id = id;
		this.title = title;
		this.overview = overview;
		this.image = image;
		this.link = link+id;
		this.commentsNr = commentsNr;
	}
}
