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
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityIgnoreCaseAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String postCode, String city, String instrument, String name);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityIgnoreCaseAndBandPositionsInstrumentName(PositionState state, String postCode, String city, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityIgnoreCaseAndFavoriteGenresName(PositionState state, String postCode, String city, String genre);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String postCode, String instrument, String genre);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentName(PositionState state, String postCode, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndFavoriteGenresName(PositionState state, String postCode, String genre);
	public List<Band> findDistinctByBandPositionsStateAndAddressCityIgnoreCaseAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String city,String instrument, String genre);
	public List<Band> findDistinctByBandPositionsStateAndAddressCityIgnoreCaseAndBandPositionsInstrumentName(PositionState state, String city, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndAddressCityIgnoreCaseAndFavoriteGenresName(PositionState state, String city, String genre);
	public List<Band> findDistinctByBandPositionsStateAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String instrument, String genre);
	public List<Band> findDistinctByBandPositionsStateAndBandPositionsInstrumentName(PositionState state, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndFavoriteGenresName(PositionState state, String genre);	
		
}
