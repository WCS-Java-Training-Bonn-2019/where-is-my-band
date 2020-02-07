package com.wildcodeschool.sea.bonn.whereismyband.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long>{
	
	Optional<Instrument> findByName(String name);

}
