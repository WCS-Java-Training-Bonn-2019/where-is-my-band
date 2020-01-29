package com.wildcodeschool.sea.bonn.whereismyband.bootstrap;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;

@Component
@Transactional
public class devBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private AddressRepository addressRepository;
	private BandRepository bandRepository;
	private GenreRepository genreRepository;
	private InstrumentRepository instrumentRepository;
	private BandpositionRepository bandpositionRepository;

	@Autowired
	public devBootstrap(AddressRepository addressRepository, BandRepository bandRepository,
			GenreRepository genreRepository, InstrumentRepository instrumentRepository,
			BandpositionRepository bandpositionRepository) {
		super();
		this.addressRepository = addressRepository;
		this.bandRepository = bandRepository;
		this.genreRepository = genreRepository;
		this.instrumentRepository = instrumentRepository;
		this.bandpositionRepository = bandpositionRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		initData();
	}

	private Genre createGenreIfNotExisting(String genreName) {

		// Create rockGenre if existing
		Optional<Genre> genreOptional = genreRepository.findByName(genreName);
		if (! genreOptional .isPresent()) {
			Genre genre = new Genre();
			genre.setName(genreName);
			genreRepository.save(genre);
			return genre;
		} else {
			return genreOptional.get();
		}
	}

	private Address createAdressIfNotExisting(String city, Integer postCode) {

		// Lookup address in repository
		Optional<Address> addressOptional = addressRepository.findByCityAndPostCode(city, postCode);

		// if address was not found
		if (! addressOptional.isPresent()) {
			// create new address object and save it
			Address address = new Address();
			address.setCity(city);
			address.setPostCode(postCode);
			addressRepository.save(address);
			return address;
		} else {
			return addressOptional.get();
		}
	}

	private Band createBandIfNotExisting(String name) {

		// Lookup address in repository
		Optional<Band> bandOptional = bandRepository.findByName(name);

		// if address was not found
		if (! bandOptional.isPresent()) {
			// create new address object and save it
			Band band = new Band();
			band.setName(name);
			band.setEmail("info@acdc.com");
			band.setDescription("ACDC ist eine Super Band!");
			band.setPhone("0228/181-0");
			bandRepository.save(band);
			return band;
		} else {
			return bandOptional.get();
		}
	}

	private Instrument createInstrumentIfNotExisting(String name) {

		// Lookup address in repository
		Optional<Instrument> instrumentOptional = instrumentRepository.findByName(name);

		// if address was not found
		if (! instrumentOptional.isPresent()) {
			// create new address object and save it
			Instrument instrument = new Instrument();
			instrument.setName(name);
			instrumentRepository.save(instrument);
			return instrument;
		} else {
			return instrumentOptional.get();
		}
	}


	private void initData() {
		
		bandpositionRepository.deleteAllInBatch();
		bandRepository.deleteAllInBatch();
		addressRepository.deleteAllInBatch();
		genreRepository.deleteAllInBatch();
		instrumentRepository.deleteAllInBatch();

		Address acdcAddress = createAdressIfNotExisting("Bonn", 53227);

		// create Genres, if not existing
		Genre rockGenre = createGenreIfNotExisting("Rock");
		Genre popGenre = createGenreIfNotExisting("Pop");
		Genre schlagerGenre = createGenreIfNotExisting("Schlager");

		Band acdc = createBandIfNotExisting("ACDC");

		acdc.setAddress(acdcAddress);
		acdc.getFavoriteGenres().add(rockGenre);
		rockGenre.getBands().add(acdc);
		acdc.getFavoriteGenres().add(popGenre);
		popGenre.getBands().add(acdc);
		acdc.getFavoriteGenres().add(schlagerGenre);
		schlagerGenre.getBands().add(acdc);

		bandRepository.save(acdc);
		
		// Stelle sicher, dass Instrumente in der Datenbank enthalten sind
		Instrument schlagzeug = createInstrumentIfNotExisting("Schlagzeug");
		Instrument keyboard = createInstrumentIfNotExisting("Keyboard");
		Instrument egitarre = createInstrumentIfNotExisting("E-Gitarre");
		Instrument gesang = createInstrumentIfNotExisting("Gesang");

		// Erzeuge Position 1
		Bandposition bandpos1 = new Bandposition();
		bandpos1.setInstrument(schlagzeug);
		bandpos1.setBand(acdc);
		bandpos1.setAgeFrom(20);
		bandpos1.setAgeTo(30);
		bandpos1.setVacant(false);
		bandpositionRepository.save(bandpos1);

		// Erzeuge Position 2
		Bandposition bandpos2 = new Bandposition();
		bandpos2.setInstrument(keyboard);
		bandpos2.setBand(acdc);
		bandpos2.setAgeFrom(25);
		bandpos2.setAgeTo(45);
		bandpos2.setVacant(true);
		bandpositionRepository.save(bandpos2);		

		// Erzeuge Position 3
		Bandposition bandpos3 = new Bandposition();
		bandpos3.setInstrument(egitarre);
		bandpos3.setBand(acdc);
		bandpos3.setAgeFrom(30);
		bandpos3.setAgeTo(50);
		bandpos3.setVacant(true);
		bandpositionRepository.save(bandpos3);

		// Erzeuge Position 4
		Bandposition bandpos4 = new Bandposition();
		bandpos4.setInstrument(gesang);
		bandpos4.setBand(acdc);
		bandpos4.setAgeFrom(30);
		bandpos4.setAgeTo(50);
		bandpos4.setVacant(true);
		bandpositionRepository.save(bandpos4);

	}

}


