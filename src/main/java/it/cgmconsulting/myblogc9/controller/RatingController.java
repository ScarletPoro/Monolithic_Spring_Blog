package it.cgmconsulting.myblogc9.controller;

import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.entity.Rating;
import it.cgmconsulting.myblogc9.entity.RatingId;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.service.PostService;
import it.cgmconsulting.myblogc9.service.RatingService;
import it.cgmconsulting.myblogc9.service.UserService;

@RestController
@RequestMapping("rate")
@Validated
public class RatingController {
	
	@Autowired PostService postService;
	@Autowired UserService userService;
	@Autowired RatingService ratingService;
	
	@PreAuthorize("hasRole('READER')")
	@PostMapping("add-rate/{postId}/{rate}")
	public ResponseEntity<?> addRate(@PathVariable long postId, @PathVariable @Min(1) @Max(5) byte rate){
		
		Optional<Post> p = postService.findById(postId);
		if(!p.isPresent())
			return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);
		
		User u = userService.getAuthenticatedUser();
		
		Rating r = new Rating(new RatingId(u, p.get()), rate);
		ratingService.save(r);
		
		return new ResponseEntity<Rating>(r, HttpStatus.OK);
		
	}

}
