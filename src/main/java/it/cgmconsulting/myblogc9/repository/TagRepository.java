package it.cgmconsulting.myblogc9.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

	boolean existsByTagName(String tagName);
	
	List<Tag> findByIsVisibleTrue();
	
	@Query(value="SELECT t FROM Tag t "
			+ "LEFT JOIN FETCH t.posts ps "
			+ "WHERE t.id IN (:ids) "
			+ "AND t.isVisible=true")
	Set<Tag> getPostsFromTags(@Param("ids") Set<Long> ids);
	
	@Query(value="SELECT t FROM Tag t "
			+ "LEFT JOIN FETCH t.posts ps "
			+ "WHERE t.tagName= :tagName "
			+ "AND t.isVisible=true")
	Tag getTagByName(@Param("tagName") String tagName);
	
	@Query(value="SELECT pt.post_id FROM post_tags pt "
			+ "INNER JOIN tag t ON t.id=pt.tag_id "
			+ "WHERE t.tag_name= :tagName", nativeQuery=true)
	Set<Long> getPostIdsByTagname(@Param("tagName") String tagName);
	
	@Query(value="SELECT tag_name FROM tag t "
			+ "INNER JOIN post_tags pt ON pt.tag_id=t.id "
			+ "INNER JOIN post p ON p.id=pt.post_id "
			+ "WHERE p.id= :postId", nativeQuery=true)
	List<String> getTagsNameByPost(@Param("postId") long postId);
}
