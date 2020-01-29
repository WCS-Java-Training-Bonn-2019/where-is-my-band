package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class for 'Genre'
 * @author Michael Obst
 */
@Entity
@Getter
@Setter
@ToString
public class Genre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@ManyToMany(mappedBy = "favoriteGenres")
	private List<Band> bands = new ArrayList<>();
		
}
