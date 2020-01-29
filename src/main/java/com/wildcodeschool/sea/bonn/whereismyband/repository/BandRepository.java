package com.wildcodeschool.sea.bonn.whereismyband.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;

@Repository
public interface BandRepository extends JpaRepository<Band, Long>{
	
	public List<Band> findByName(String name);

}
