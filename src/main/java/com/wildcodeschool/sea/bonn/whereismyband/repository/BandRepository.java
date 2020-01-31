package com.wildcodeschool.sea.bonn.whereismyband.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;

@Repository
public interface BandRepository extends JpaRepository<Band, Long>{
	

	public Optional<Band> findByName(String name);
	public List<Band> findDistinctByBandPositionsState(PositionState state);


}
