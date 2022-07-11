package it.cgmconsulting.myblogc9.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.entity.Comment;
import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.payload.response.CommentResponse;
import it.cgmconsulting.myblogc9.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired CommentRepository commentRepository;
	
	public void save(Comment c) {
		commentRepository.save(c);
	}
	
	public List<CommentResponse> getComments(long postId){
		return commentRepository.getComments(postId);
	}
	
	public long countByPostId(Post p) {
		return commentRepository.countByPostId(p);
	}
	
	public Optional<Comment> findByIdAndCensoredFalse(long id){
		return commentRepository.findByIdAndCensoredFalse(id);
	}
	
	public Optional<Comment> findById(long id){
		return commentRepository.findById(id);
	}

}
