package com.wildcodeschool.sea.bonn.whereismyband.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;

@Repository
public interface MusicianRepository extends JpaRepository<Musician, Long> {
	
	public List <Musician> findByFirstNameAndLastName(String firstName, String lastName);

}
