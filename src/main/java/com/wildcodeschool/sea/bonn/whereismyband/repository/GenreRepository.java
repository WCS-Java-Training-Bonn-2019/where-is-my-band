/**
 * 
 */
package com.wildcodeschool.sea.bonn.whereismyband.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;

/**
 * JPA-repository for entity 'Genre'
 * @author Michael Obst
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

	Optional<Genre> findByName(String name);
}
