package com.wildcodeschool.sea.bonn.whereismyband.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;

@Repository
public interface MusicianRepository extends JpaRepository<Musician, Long> {
	
	public List<Musician> findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthday(String firstName, String lastName, LocalDate birthday);
	public List<Musician> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
	public Optional<Musician> findByUsernameIgnoreCase(String username);
	

}
