package com.ravn.challenge.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Movie extends Auditable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long idMovie;
    @Column(length = 55, nullable = false)
	String name;
    @Column(length = 255, nullable = false)
	String synopsis;
    @Column(length = 55)
	String imageUrl;
    @Column(nullable = false)
    String year;
    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false, foreignKey = @ForeignKey(name = "FK_MOVIE_CATEGORY"))
    Category category;
    @OneToMany(mappedBy = "movie")
    List<Rating> rating;
	
	public Movie(Long idMovie) {
        this.idMovie = idMovie;
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(idMovie, movie.idMovie);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idMovie);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "idMovie=" + idMovie +
                ", name='" + name + '\'' +
                ", synopsis='" + synopsis + '\'' +
                '}';
    }
}
