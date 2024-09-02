package com.ravn.challenge.service;

import org.springframework.stereotype.Service;

import com.ravn.challenge.entity.Rating;
import com.ravn.challenge.entity.id.RatingId;
import com.ravn.challenge.repository.IGenericRepo;
import com.ravn.challenge.repository.IRatingRepository;

@Service
public class RatingService extends GenericService<Rating, Long> {
	
	private final IRatingRepository ratingRepository;
	
	public RatingService(IRatingRepository ratingRepository) {
		super(Rating.class);
		this.ratingRepository = ratingRepository;
	}
	
	public boolean existsById(Long id) {
		return this.ratingRepository.existsById(id);
	}

	@Override
	protected IGenericRepo<Rating, Long> getRepo() {
		return this.ratingRepository;
	}

}
