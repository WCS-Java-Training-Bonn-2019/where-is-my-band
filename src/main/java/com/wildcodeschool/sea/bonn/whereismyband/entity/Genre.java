package com.wildcodeschool.sea.bonn.whereismyband.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class for 'Genre'
 * @author Michael Obst
 */
@Entity
public class Genre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

//  JPA magic instead of a coded constructor
//	public Genre(Long id, String name) {
//		super();
//		this.id = id;
//		this.name = name;
//	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @returns The String representation of a Genre
	 */
	@Override
	public String toString() {
		return "Genre [id=" + id + ", name=" + name + "]";
	}
	
}
