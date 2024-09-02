package com.ravn.challenge.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ravn.challenge.entity.Category;
import com.ravn.challenge.entity.Movie;

public class MovieSpecification {

	public static Specification<Movie> hasNameOrSynopsis(String searchTerm) {
        return (root, query, builder) -> builder.or(
            builder.like(builder.lower(root.get("name")), "%" + searchTerm.toLowerCase() + "%"),
            builder.like(builder.lower(root.get("synopsis")), "%" + searchTerm.toLowerCase() + "%")
        );
    }

    public static Specification<Movie> hasCategory(Category category) {
        return (root, query, builder) -> builder.equal(root.get("category"), category);
    }

    public static Specification<Movie> hasYearOfRelease(String year) {
        return (root, query, builder) -> builder.equal(root.get("year"), year);
    }
}
