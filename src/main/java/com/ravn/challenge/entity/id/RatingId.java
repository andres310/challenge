package com.ravn.challenge.entity.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Embeddable
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
public class RatingId implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long idMovie;
    @EqualsAndHashCode.Include
    private Long idUser;
}
