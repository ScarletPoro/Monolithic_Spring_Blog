package it.cgmconsulting.myblogc9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.Rating;
import it.cgmconsulting.myblogc9.entity.RatingId;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId>{
	/*
	@Query(value="SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) "
			+ "FROM Rating r WHERE r.ratingId.post.id = :postId")
	double getAvgPost(@Param("postId") long postId);
	*/
	@Query(value="SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) "
			+ "FROM rating r WHERE r.post_id = :postId", nativeQuery=true)
	double getAvgPost(@Param("postId") long postId);
	
	

}
