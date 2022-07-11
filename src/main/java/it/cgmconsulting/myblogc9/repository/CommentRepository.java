package it.cgmconsulting.myblogc9.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.Comment;
import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.payload.response.CommentResponse;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.CommentResponse("
			+ "c.id, "
			+ "CASE WHEN (c.censored = true) THEN 'CENSORED' ELSE c.comment END, "			
			+ "c.author.username, "
			+ "c.createdAt"
			+ ") FROM Comment c "
			+ "WHERE c.postId.id = :postId "
			+ "AND c.postId.published =  true "
			+ "ORDER BY c.createdAt DESC")
	List<CommentResponse> getComments(@Param("postId") long postId);
	
	long countByPostId(@Param("postId") Post p);
	
	Optional<Comment> findByIdAndCensoredFalse(long commentId);
	
	

}
