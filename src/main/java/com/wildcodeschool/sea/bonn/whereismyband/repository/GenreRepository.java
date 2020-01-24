/**
 * 
 */
package com.wildcodeschool.sea.bonn.whereismyband.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;

/**
 * JPA-repository for entity 'Genre'
 * @author Michael Obst
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {

}
