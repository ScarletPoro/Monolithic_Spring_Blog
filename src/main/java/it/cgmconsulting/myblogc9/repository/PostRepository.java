package it.cgmconsulting.myblogc9.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.Post;
import it.cgmconsulting.myblogc9.payload.response.PostDetailResponse;
import it.cgmconsulting.myblogc9.payload.response.PostResponse;
import it.cgmconsulting.myblogc9.payload.response.PostSearchResponseWithContent;
import it.cgmconsulting.myblogc9.payload.response.PostXlsResponse;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	
	Optional<Post> findByIdAndPublishedTrue(long postId);
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.PostDetailResponse("
			+ "p.id, "
			+ "p.title, "
			+ "p.content, "
			+ "p.image, "
			+ "p.author.username, "
			+ "p.updatedAt) "
			+ "FROM Post p "
			+ "WHERE p.published = true "
			+ "AND p.id = :postId")
	PostDetailResponse getPostDetail(@Param("postId") long id);
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.PostResponse("
			+ "p.id, "
			+ "p.title, "
			+ "p.overview, "
			+ "p.image, "
			+ "(SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) FROM Rating r WHERE r.ratingId.post.id = p.id), "
			+ "(SELECT COUNT(c) FROM Comment c WHERE c.postId.id = p.id )"
			+ ") "
			+ "FROM Post p "
			+ "WHERE p.published = true "
			+ "ORDER BY p.updatedAt DESC")
	List<PostResponse> getPosts();
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.PostResponse("
			+ "p.id, "
			+ "p.title, "
			+ "p.overview, "
			+ "p.image, "
			+ "(SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) FROM Rating r WHERE r.ratingId.post.id = p.id), "
			+ "(SELECT COUNT(c) FROM Comment c WHERE c.postId.id = p.id )"
			+ ") "
			+ "FROM Post p "
			+ "WHERE p.published = true "
			+ "AND p.id IN (:postIds) "
			+ "ORDER BY p.title")
	List<PostResponse> getPostsByTagname(@Param("postIds") Set<Long> postIds);
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.PostResponse("
			+ "p.id, "
			+ "p.title, "
			+ "p.overview, "
			+ "p.image "
			//+ "(SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) FROM Rating r WHERE r.ratingId.post.id = p.id) AS average, "
			//+ "(SELECT COUNT(c.id) FROM Comment c WHERE c.postId.id = p.id) AS commentsNr"
			+ ") "
			+ "FROM Post p "
			+ "WHERE p.published = true")
	Page<PostResponse> getPostsPaged(Pageable pageable);
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.PostSearchResponseWithContent("
			+ "p.id, "
			+ "p.title, "
			+ "p.content) "
			+ "FROM Post p "
			+ "WHERE p.published = true "
			+ "AND (title LIKE :keyword OR content LIKE :keyword)")
	List<PostSearchResponseWithContent> findPostsByKeyword(@Param("keyword") String keyword);
	
	/*
	@Query(value="SELECT p.* FROM post p "
			+ " WHERE p.is_published=true "
			+ " AND   REGEXP_LIKE(p.title, BINARY :wordToFind ) OR REGEXP_LIKE(p.content, BINARY :wordToFind )",nativeQuery = true)
	List<Post> getPostsVisibleBySearchCaseSensitiveTrue(String wordToFind);
	
	@Query(value="SELECT p.* FROM post p "
			+ " WHERE p.is_published=true "
			+ " AND   REGEXP_LIKE(p.title, :wordToFind ) OR REGEXP_LIKE(p.content,:wordToFind )",nativeQuery = true)
	List<Post> getPostsVisibleBySearchCaseSensitiveFalse(String wordToFind);
	*/
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.PostXlsResponse("
			+ "p.id, "
			+ "p.title, "
			+ "(SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) FROM Rating r WHERE r.ratingId.post.id = p.id), "
			+ "p.published, "
			+ "p.author.username) "
			+ "FROM Post p "
			+ "ORDER BY p.title")
	List<PostXlsResponse> getPostReport();
	
}
