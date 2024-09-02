package com.ravn.challenge.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ravn.challenge.entity.Category;
import com.ravn.challenge.entity.Movie;
import com.ravn.challenge.exception.StorageException;
import com.ravn.challenge.repository.IGenericRepo;
import com.ravn.challenge.repository.IMovieRepository;
import com.ravn.challenge.specification.MovieSpecification;

@Service
public class MovieService extends GenericService<Movie, Long> {

	final IMovieRepository movieRepository;
	final IStorageService storageService;
	
	public MovieService(IMovieRepository movieRepository, IStorageService storageService) {
		super(Movie.class);
		this.movieRepository = movieRepository;
		this.storageService = storageService;
	}
	
	@Cacheable("movies")
	public List<Movie> getAll() {
        return this.movieRepository.findAll();
    }
	
	@Cacheable("movies")
	public Page<Movie> getMovies(String searchTerm, Category category, String yearOfRelease, Pageable pageable) {
        Specification<Movie> specs = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            specs = specs.and(MovieSpecification.hasNameOrSynopsis(searchTerm));
        }

        if (category != null) {
            specs = specs.and(MovieSpecification.hasCategory(category));
        }

        if (yearOfRelease != null && !yearOfRelease.isEmpty()) {
            specs = specs.and(MovieSpecification.hasYearOfRelease(yearOfRelease));
        }

        return movieRepository.findAll(specs, pageable);
    }
	
	@Cacheable("images")
	public Resource loadImage(Long id) {
		String imgPath = this.findById(id).getImageUrl();
		String name = imgPath.substring(imgPath.lastIndexOf("/") + 1);
		
		return storageService.loadAsResource(name);
	}

    @Transactional
    public Movie save(Movie movie) {
        return this.movieRepository.save(movie);
    }
    
    @Transactional
    public void saveImage(MultipartFile image, Long id) throws NoSuchElementException, StorageException {
    	Movie movie = this.movieRepository.findById(id)
    			.orElseThrow(() -> new NoSuchElementException(String.format("Movie with id %s not found", id)));
    	
    	// TODO: Refactor
    	String filename = String.format("%s-%s", movie.getIdMovie(), LocalDateTime.now().atZone(ZoneId.of("America/El_Salvador")).toEpochSecond());
    	String extension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
    	
    	this.storageService.store(image, filename);
    	
    	movie.setImageUrl(extension);
    	
    	this.movieRepository.save(movie);
    }

    @Transactional
    public void delete(Long id) {
        this.movieRepository.deleteById(id);
    }

	@Override
	protected IGenericRepo<Movie, Long> getRepo() {
		return this.movieRepository;
	}
}
