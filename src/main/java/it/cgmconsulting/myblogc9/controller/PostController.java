package it.cgmconsulting.myblogc9.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.request.PostRequest;
import it.cgmconsulting.myblogc9.payload.response.CommentResponse;
import it.cgmconsulting.myblogc9.payload.response.PostDetailResponse;
import it.cgmconsulting.myblogc9.payload.response.PostResponse;
import it.cgmconsulting.myblogc9.payload.response.PostSearchResponse;
import it.cgmconsulting.myblogc9.payload.response.PostSearchResponseWithContent;
import it.cgmconsulting.myblogc9.service.CommentService;
import it.cgmconsulting.myblogc9.service.ImageService;
import it.cgmconsulting.myblogc9.service.PostService;
import it.cgmconsulting.myblogc9.service.TagService;
import it.cgmconsulting.myblogc9.service.UserService;

@RestController
@RequestMapping("post")

public class PostController {
	
	@Autowired UserService userService;
	@Autowired PostService postService;
	@Autowired TagService tagService;
	@Autowired CommentService commentService;
	@Autowired ImageService imageService;
	
	@Value("${image.post.size}")
	private long pSize;
	
	@Value("${image.post.height}")
	private int pHeight;
	
	@Value("${image.post.width}")
	private int pWidth;
	
	@Value("${image.post.ext}")
	private String[] extensions;
	
	@Value("${image.post.path}")
	private String pPath;
	
	@PreAuthorize("hasRole('WRITER')")
	@PostMapping("/create")
	public ResponseEntity<String> create(@Valid @RequestBody PostRequest request){
		
		Post p = new Post(
				request.getTitle(),
				request.getContent(),
				request.getOverview(),
				userService.getAuthenticatedUser()
				);
		
		postService.save(p);
		return new ResponseEntity<String>("New post created: "+p.getTitle(), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/publish/{postId}")
	public ResponseEntity<String> publish(@PathVariable long postId){
		
		Optional<Post> p = postService.findById(postId);
		if(!p.isPresent())
			return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);
		
		p.get().setPublished(true);
		postService.save(p.get());
		return new ResponseEntity<String>("Post '"+p.get().getTitle()+"' has been published", HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('WRITER')")
	@PutMapping("update/{postId}")
	public ResponseEntity<String> update(@PathVariable long postId, @Valid @RequestBody PostRequest request){
		// aggiornare il post (title, content, overview)
		// chi può aggionare il post è solo colui che l'ha scritto
		// s-pubblicarlo- > published: false
		
		Optional<Post> p = postService.findById(postId);
		if(!p.isPresent())
			return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);
		
		User u = userService.getAuthenticatedUser();
		
		if(u.getId() == p.get().getAuthor().getId()) {
		//if(u.equals(p.get().getAuthor()) { // anche questo funziona
			p.get().setTitle(request.getTitle());
			p.get().setContent(request.getContent());
			p.get().setOverview(request.getOverview());
			p.get().setPublished(false);
			postService.save(p.get());
			return new ResponseEntity<String>("Post '"+p.get().getId()+"' has been modified", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("You are not the owner of this post!", HttpStatus.FORBIDDEN);
			
		}
	}
	
	@GetMapping("public/post-detail/{id}")
	public ResponseEntity<?> getPostDetail(@PathVariable long id){
		PostDetailResponse p = postService.getPostDetail(id);
		if(p == null)
			return new ResponseEntity<String>("No post found", HttpStatus.NOT_FOUND);
		// recupero i tagName associati al post in questione
		List<String> ts = tagService.getTagsNameByPost(id);
		p.setTags(ts);
		List<CommentResponse> comments = commentService.getComments(id);
		p.setComments(comments);
		
		return new ResponseEntity<PostDetailResponse>(p, HttpStatus.OK);
	}
	
	@GetMapping("public/posts")
	public ResponseEntity<?> getPosts(){
		List<PostResponse> ps = postService.getPosts();
		if(ps.isEmpty())
			return new ResponseEntity<String>("No post found or published", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<PostResponse>>(ps, HttpStatus.OK);
	}
	
	@GetMapping("public/paged-posts")
	public ResponseEntity<?> getPagedPosts(
			@RequestParam(defaultValue="0") int pageNumber, // numero della pagina
			@RequestParam(defaultValue="4") int pageSize,   // numero di elementi per pagina
			@RequestParam(defaultValue="ASC") String direction, // direzione (ASC oppure DESC)
			@RequestParam(defaultValue="title") String sortBy	// campo sul quale fare l'ordinamento	
			){
		
		List<PostResponse> ps = postService.getPostsPaged(pageNumber, pageSize, direction, sortBy);
		if(ps.isEmpty())
			return new ResponseEntity<String>("No post found or published", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<PostResponse>>(ps, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('WRITER')")
	@PutMapping("update-image/{id}")
	public ResponseEntity<?> updateImage(@PathVariable long id,
			@RequestParam MultipartFile file){
		
		Optional<Post> p = postService.findById(id);
		if(!p.isPresent())
			return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);
		
		User u = userService.getAuthenticatedUser();
		if(u.getId() != p.get().getAuthor().getId())
			return new ResponseEntity<String>("You are not the author of this post!", HttpStatus.FORBIDDEN);
		
		if(file.isEmpty())
			return new ResponseEntity<String>("Empty file !", HttpStatus.BAD_REQUEST);
		
		if(!imageService.checkExtension(file, extensions))
			return new ResponseEntity<String>("File type not allowed", HttpStatus.BAD_REQUEST);
		
		if(!imageService.checkDimensions(imageService.fromFileToBufferedImage(file), pHeight, pWidth))
			return new ResponseEntity<String>("Image tooo large !", HttpStatus.BAD_REQUEST);
		
		if(!imageService.checkSize(file, pSize))
			return new ResponseEntity<String>("File size greater then "+pSize/1024+" kb", HttpStatus.BAD_REQUEST);	
		
		String newFileName = id+"."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
		if(!imageService.loadImage(file, id, newFileName))
			return new ResponseEntity<String>("Something went wrong writing image on storage", HttpStatus.INTERNAL_SERVER_ERROR);	
		
		p.get().setImage(newFileName);
		postService.save(p.get());
		
		return new ResponseEntity<String>("Image added to post", HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('WRITER')")
	@PutMapping("remove-image/{id}")
	public ResponseEntity<?> updateImage(@PathVariable long id){
		
		Optional<Post> p = postService.findById(id);
		if(!p.isPresent())
			return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);
		
		User u = userService.getAuthenticatedUser();
		if(u.getId() != p.get().getAuthor().getId())
			return new ResponseEntity<String>("You are not the author of this post!", HttpStatus.FORBIDDEN);
		
		if(!imageService.removeImage(p.get().getImage()))
			return new ResponseEntity<String>("Something went wrong deleting image on storage", HttpStatus.INTERNAL_SERVER_ERROR);	
		
		p.get().setImage(null);
		postService.save(p.get());
		
		return new ResponseEntity<String>("Image removed from post", HttpStatus.OK);	
	}
	
	// Trovare i post che contengano la parola chiave o nel titolo o nel contenuto
	// Parametri: keyword, isExactMatch, isCaseSensitive
	@GetMapping("public/find-posts-by-keyword")
	public ResponseEntity<?> findPostsByKeyword(
			@RequestParam String keyword, 
			@RequestParam boolean isExactMatch, 
			@RequestParam boolean isCaseSensitive){ 
		
		// isExactMatch -> Esempi
		// Keyword : 'pasta'
		// isExactMatch true: "la pasta va cotta", "condire la pasta."
		// isExactMatch false: "impastare farina e..." 
		
		// lista iniziale dei post da esaminare:
		// SELECT * FROM post WHERE title LIKE '%keyword%' OR content LIKE '%keyword%';
	
		List<PostSearchResponseWithContent> list = postService.findPostsByKeyword(keyword);
		if(list.isEmpty())
			return new ResponseEntity<String>("No posts found about "+keyword, HttpStatus.NOT_FOUND);			
		else
			return new ResponseEntity<List<PostSearchResponse>>(postService.searchPostsByKeyword(keyword, isExactMatch, isCaseSensitive, list), HttpStatus.OK);
		
	}
	
}
