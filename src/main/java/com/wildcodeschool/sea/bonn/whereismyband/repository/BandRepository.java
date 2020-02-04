package com.wildcodeschool.sea.bonn.whereismyband.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;

@Repository
public interface BandRepository extends JpaRepository<Band, Long>{
	

	public Optional<Band> findByName(String name);
	public List<Band> findDistinctByBandPositionsState(PositionState state);
	//public List<Band> findByPostCode(Address postCode);
	public List<Band> findByAddressPostCodeAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(Integer postCode, String city, String instrument, String name);
	public List<Band> findByAddressPostCodeAndAddressCityAndBandPositionsInstrumentName(Integer postCode, String city, String instrument);
	public List<Band> findByAddressPostCodeAndAddressCityAndFavoriteGenresName(Integer postCode, String city, String name);
	public List<Band> findByAddressPostCodeAndAddressCity(Integer zipcode, String city);
	public List<Band> findByAddressPostCodeAndBandPositionsInstrumentNameAndFavoriteGenresName( Integer postCode, String instrument, String name);
	public List<Band> findByAddressPostCodeAndBandPositionsInstrumentName(Integer postCode, String instrument);
	public List<Band> findByAddressPostCodeAndFavoriteGenresName(Integer postCode, String name);
	public List<Band> findByAddressPostCode(Integer zipcode);
	public List<Band> findByAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(String city,	String instrument, String name);
	public List<Band> findByAddressCityAndBandPositionsInstrumentName(String city, String instrument);
	public List<Band> findByAddressCityAndFavoriteGenresName(String city, String name);
	public List<Band> findByAddressCity(String city);
	public List<Band> findByBandPositionsInstrumentNameAndFavoriteGenresName(String instrument, String name);
	public List<Band> findByBandPositionsInstrumentName(String instrument);
	public List<Band> findByFavoriteGenresName(String name);
	
//	public List<Band> findByAddressPostCodeContainingAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(Integer postCode, String city, String instrument, String name);	
//	public List<Band> findByAddressPostCodeContainingAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(Integer postCode, String city, String instrument, String name);
//	public List<Band> findByAddressPostCodeContainingAndAddressCityAndBandPositionsInstrumentName(Integer postCode, String city, String instrument);
//	public List<Band> findByAddressPostCodeContainingAndAddressCityAndFavoriteGenresName(Integer postCode, String city, String name);
//	public List<Band> findByAddressPostCodeContainingAndAddressCity(Integer zipcode, String city);
//	public List<Band> findByAddressPostCodeContainingAndBandPositionsInstrumentNameAndFavoriteGenresName( Integer postCode, String instrument, String name);
//	public List<Band> findByAddressPostCodeContainingAndBandPositionsInstrumentName(Integer postCode, String instrument);
//	public List<Band> findByAddressPostCodeContainingAndFavoriteGenresName(Integer postCode, String name);
//	public List<Band> findByAddressPostCodeContaining(Integer zipcode);
//	public List<Band> findByAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(String city,	String instrument, String name);
//	public List<Band> findByAddressCityAndBandPositionsInstrumentName(String city, String instrument);
//	public List<Band> findByAddressCityAndFavoriteGenresName(String city, String name);
//	public List<Band> findByAddressCity(String city);
//	public List<Band> findByBandPositionsInstrumentNameAndFavoriteGenresName(String instrument, String name);
//	public List<Band> findByBandPositionsInstrumentName(String instrument);
//	public List<Band> findByFavoriteGenresName(String name);



}
