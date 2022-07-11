package it.cgmconsulting.myblogc9.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.entity.Rating;
import it.cgmconsulting.myblogc9.entity.RatingId;
import it.cgmconsulting.myblogc9.repository.RatingRepository;

@Service
public class RatingService {
	
	@Autowired RatingRepository ratingRepository;
	
	public Optional<Rating> findById(RatingId ratingId) {
		return ratingRepository.findById(ratingId);
	}
	
	public void save(Rating r) {
		ratingRepository.save(r);
	}
	
	public double getAvgPost(long postId) {
		return ratingRepository.getAvgPost(postId);
	}

}
