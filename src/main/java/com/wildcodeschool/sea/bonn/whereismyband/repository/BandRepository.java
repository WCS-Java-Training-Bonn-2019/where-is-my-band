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
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String postCode, String city, String instrument, String name);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndBandPositionsInstrumentName(PositionState state, String postCode, String city, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndFavoriteGenresName(PositionState state, String postCode, String city, String name);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCity(PositionState state, String zipcode, String city);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String postCode, String instrument, String name);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentName(PositionState state, String postCode, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndFavoriteGenresName(PositionState state, String postCode, String name);
	public List<Band> findDistinctByBandPositionsStateAndAddressPostCodeStartingWith(PositionState state, String zipcode);
	public List<Band> findDistinctByBandPositionsStateAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String city,String instrument, String name);
	public List<Band> findDistinctByBandPositionsStateAndAddressCityAndBandPositionsInstrumentName(PositionState state, String city, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndAddressCityAndFavoriteGenresName(PositionState state, String city, String name);
	public List<Band> findDistinctByBandPositionsStateAndAddressCity(PositionState state, String city);
	public List<Band> findDistinctByBandPositionsStateAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState state, String instrument, String name);
	public List<Band> findDistinctByBandPositionsStateAndBandPositionsInstrumentName(PositionState state, String instrument);
	public List<Band> findDistinctByBandPositionsStateAndFavoriteGenresName(PositionState state, String name);
	
	

	
}
