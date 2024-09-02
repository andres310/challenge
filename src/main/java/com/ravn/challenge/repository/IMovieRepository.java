package com.ravn.challenge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.ravn.challenge.entity.Movie;

public interface IMovieRepository extends IGenericRepo<Movie, Long> {

	@Query(value = "SELECT m.* FROM movies m "
			+ " INNER JOIN categories c ON c.id_category = m.id_category"
			+ " WHERE ( m.name LIKE CONCAT('%', ?1, '%')"
			+ " OR m.synopsis LIKE CONCAT('%', ?2, '%')"
			+ " OR c.name LIKE CONCAT('%', ?3, '%') "
			+ " OR m.year LIKE CONCAT('%', ?4, '%') )", nativeQuery = true)
	Page<Movie> findPage(String searchByName,
			String searchBySynopsis,
			String filterByCategory,
			Integer filterByYear,
			Pageable pageable);
}
