package it.cgmconsulting.myblogc9.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.payload.response.PostDetailResponse;
import it.cgmconsulting.myblogc9.payload.response.PostResponse;
import it.cgmconsulting.myblogc9.payload.response.PostSearchResponse;
import it.cgmconsulting.myblogc9.payload.response.PostSearchResponseWithContent;
import it.cgmconsulting.myblogc9.repository.PostRepository;

@Service
public class PostService {
	
	
	
	@Autowired PostRepository postRepository;
	
	public void save(Post p) {
		postRepository.save(p);
	}
	
	public Optional<Post> findById(long id){
		return postRepository.findById(id);
	}
	
	public Optional<Post> findByIdAndPublishedTrue(long postId){
		return postRepository.findByIdAndPublishedTrue(postId);
	}
	
	public PostDetailResponse getPostDetail(long id) {
		return postRepository.getPostDetail(id);
	}
	
	public List<PostResponse> getPosts(){
		return postRepository.getPosts();
	}
	
	public List<PostResponse> getPostsByTagname(Set<Long> postIds){
		return postRepository.getPostsByTagname(postIds);
	}
	
	public List<PostResponse> getPostsPaged(int pagNumber, int pageSize, String direction, String sortBy){
		Pageable pageable = PageRequest.of(pagNumber, pageSize, Sort.by(Direction.valueOf(direction.toUpperCase()), sortBy));
		Page<PostResponse> pageResult = postRepository.getPostsPaged(pageable);
		if(pageResult.hasContent())
			return pageResult.getContent();
		else
			return new ArrayList<PostResponse>();		
	}
	
	public List<PostSearchResponseWithContent> findPostsByKeyword(String keyword){
		return postRepository.findPostsByKeyword(keyword);
	}
	
	public List<PostSearchResponse> searchPostsByKeyword(String keyword, boolean isExactMatch, boolean isCaseSensitive, List<PostSearchResponseWithContent> posts) {
		
		Pattern p;
		String pattern = "\\b"+keyword+"\\b";
		List<PostSearchResponse> filteredPosts = new ArrayList<PostSearchResponse>();
		if(!isExactMatch)
			if(isCaseSensitive) p = Pattern.compile(keyword);
			else p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);

		else //exact match
			if(isCaseSensitive) p = Pattern.compile(pattern);
			else p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);

		// map Post into PostSimpleResponse using statement 'for'
		for(PostSearchResponseWithContent post : posts) {
			if(p.matcher(post.getTitle().concat(" ").concat(post.getContent())).find())
				filteredPosts.add(new PostSearchResponse(post.getId(), post.getTitle()));
		}
		
		// or map Post into PostSimpleResponse using stream()
		filteredPosts = posts.stream()
			.filter(post -> p.matcher(post.getTitle().concat(" ").concat(post.getContent())).find())
			.map(post -> new PostSearchResponse(post.getId(), post.getTitle()))
			.collect(Collectors.toList());
				
		return filteredPosts;
	}

}
