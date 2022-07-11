package it.cgmconsulting.myblogc9.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.entity.Tag;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.response.PostResponse;
import it.cgmconsulting.myblogc9.service.CommentService;
import it.cgmconsulting.myblogc9.service.PostService;
import it.cgmconsulting.myblogc9.service.RatingService;
import it.cgmconsulting.myblogc9.service.TagService;
import it.cgmconsulting.myblogc9.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("tag")
@Validated
@Slf4j
public class TagController {
	
	@Autowired TagService tagService;
	@Autowired PostService postService;
	@Autowired UserService userService;
	@Autowired RatingService ratingService;
	@Autowired CommentService commentService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("create/{tagName}") //localhost:808x/tag/create/ANTIPASTI
	public ResponseEntity<String> create(@PathVariable @NotBlank @Size(min=3, max=15) String tagName){
		return new ResponseEntity<String>(tagService.save(tagName), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("update") // localhost:8085/tag/update?id=4&newTagName=seCONdi
	public ResponseEntity<String> update(@RequestParam @Min(1) long id, @RequestParam @NotBlank @Size(min=3, max=15) String newTagName){
		return new ResponseEntity<String>(tagService.update(id, newTagName), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("switch-visibility/{id}")
	public ResponseEntity<String> switchVisibility(@PathVariable @Min(1) long id){
		return new ResponseEntity<String>(tagService.switchVisibility(id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("get-all")
	public ResponseEntity<List<Tag>> getAll(){
		return new ResponseEntity<List<Tag>>(tagService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("public/get-all-visible")
	public ResponseEntity<?> getAllVisible(){
		List<Tag> list = tagService.getAllVisible();
		if(list.isEmpty())
			return new ResponseEntity<String>("Nessun tag trovato", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<Tag>>(list, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('WRITER') or hasRole('ADMIN')")
	@PutMapping("add-tags-to-post/{postId}")
	public ResponseEntity<?> addTagsToPost(@PathVariable long postId, 
			@RequestParam boolean remove, 
			@RequestParam Set<Long> ids){
		
		Optional<Post> p = postService.findById(postId);
		if(!p.isPresent())
			return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);
		
		User u = userService.getAuthenticatedUser();
		if(!u.equals(p.get().getAuthor()))
			return new ResponseEntity<String>("You are not the owner of this post", HttpStatus.FORBIDDEN);
		
		Set<Tag> tags = tagService.getPostsFromTags(ids);
		
		String msg = null;
		for(Tag t : tags) {
			if(!remove) {
				t.getPosts().add(p.get());
				msg = "added to";
			} else {
				t.getPosts().remove(p.get());
				msg = "removed from";
			}
				
			tagService.save(t);
		}
		
		return new ResponseEntity<String>("tags "+msg+" "+p.get().getTitle(), HttpStatus.OK);
	}
	
	@GetMapping("public/get-posts-by-tag-1/{tagName}")
	public ResponseEntity<?> getPostByTag1(@PathVariable String tagName){
		// Una query + 1 ciclo java
		Instant start = Instant.now();
		Tag t = tagService.getTagByName(tagName);
		if(t == null)
			return new ResponseEntity<String>("No tag found", HttpStatus.NOT_FOUND);
		
		if(t.getPosts().isEmpty())
			return new ResponseEntity<String>("No posts found with tag "+tagName, HttpStatus.NOT_FOUND);
		
		List<PostResponse> ps = t.getPosts().stream()
				.filter(p -> p.isPublished())
				.map(p ->
			new PostResponse(
					p.getId(),
					p.getTitle(),
					p.getOverview(),
					p.getImage(),
					ratingService.getAvgPost(p.getId()),
					commentService.countByPostId(p)
					)
		).collect(Collectors.toList());
		
		Instant end = Instant.now();
		log.info("Tempo di esecuzione 1 : "+Duration.between(start, end).toMillis());
		
		return new ResponseEntity<List<PostResponse>>(ps, HttpStatus.OK);
		
	}
	
	@GetMapping("public/get-posts-by-tag-2/{tagName}")
	public ResponseEntity<?> getPostByTag2(@PathVariable String tagName){
		// 2 query : risulta più veloce della logica adottata nel precedente metodo
		// impiega quasi 1/3 del tempo
		Instant start = Instant.now();
		Set<Long> postIds = tagService.getPostIdsByTagname(tagName);
		if(postIds.isEmpty())
			return new ResponseEntity<String>("No posts found with tag: "+tagName, HttpStatus.NOT_FOUND);
		
		List<PostResponse> ps = postService.getPostsByTagname(postIds);
		
		Instant end = Instant.now();
		log.info("Tempo di esecuzione 2 : "+Duration.between(start, end).toMillis());
		
		return new ResponseEntity<List<PostResponse>>(ps, HttpStatus.OK);
		
	}
	
	
	
}
