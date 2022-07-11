package it.cgmconsulting.myblogc9.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.cgmconsulting.myblogc9.entity.Comment;
import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.request.CommentRequest;
import it.cgmconsulting.myblogc9.service.CommentService;
import it.cgmconsulting.myblogc9.service.PostService;
import it.cgmconsulting.myblogc9.service.UserService;

@RestController
@RequestMapping("comment")
public class CommentController {
	
	@Autowired PostService postService;
	@Autowired UserService userService;
	@Autowired CommentService commentService;
	
	@PostMapping("create/{postId}")
	@PreAuthorize("hasRole('READER')")
	public ResponseEntity<?> create(@PathVariable long postId, @RequestBody @Valid CommentRequest request){
		
		Optional<Post> p = postService.findByIdAndPublishedTrue(postId);
		if(!p.isPresent())
			return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);
		
		User u = userService.getAuthenticatedUser();
		
		Comment c = new Comment(request.getCommentText(), u, p.get());
		commentService.save(c);
		
		return new ResponseEntity<String>("New comment added to post '"+p.get().getTitle()+"'", HttpStatus.OK);
	}

}
