package it.cgmconsulting.myblogc9.payload.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class PostDetailResponse {
	
	private long postId;
	private String title;
	private String content;
	private String image;
	private String author; // username di User
	private LocalDateTime publicationDate;
	private List<String> tags;
	private List<CommentResponse> comments;
	
	public PostDetailResponse(long postId, String title, String content, String image, String author,
			LocalDateTime publicationDate) {
		super();
		this.postId = postId;
		this.title = title;
		this.content = content;
		this.image = image;
		this.author = author;
		this.publicationDate = publicationDate;
	}
	
	

}
