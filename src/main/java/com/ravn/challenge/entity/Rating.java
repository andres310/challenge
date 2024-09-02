package com.ravn.challenge.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.ravn.challenge.entity.id.RatingId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ratings")
//@IdClass(RatingId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Rating extends Auditable implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRating;
    @ManyToOne
    @JoinColumn(name = "id_movie", referencedColumnName = "idMovie", updatable = false, insertable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "idUser", updatable = false, insertable = false)
    private User user;
    @DecimalMin(value = "0", message = "Minimum rating is 0.0 out of 5.0 stars.")
    @DecimalMax(value = "5", message = "Maximun rating is 5.0 out of 5.0 stars.")
    private BigDecimal stars;
    
	public Rating(Movie movie, User user,
			@DecimalMin(value = "0", message = "Minimum rating is 0.0 out of 5.0 stars.") @DecimalMax(value = "5", message = "Maximun rating is 5.0 out of 5.0 stars.") BigDecimal stars) {
		super();
		this.movie = movie;
		this.user = user;
		this.stars = stars;
	}
    
    
    
	/*public Rating(Long idMovie, Long idUser,
			@DecimalMin(value = "0", message = "Minimum rating is 0.0 out of 5.0 stars.") @DecimalMax(value = "5", message = "Maximun rating is 5.0 out of 5.0 stars.") BigDecimal stars) {
		super();
		this.idMovie = idMovie;
		this.idUser = idUser;
		this.stars = stars;
	}*/
    
    

}
