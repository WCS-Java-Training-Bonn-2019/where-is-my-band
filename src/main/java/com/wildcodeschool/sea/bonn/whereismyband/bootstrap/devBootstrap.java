package com.wildcodeschool.sea.bonn.whereismyband.bootstrap;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Gender;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenderRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@Component
@Transactional
public class devBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private AddressRepository addressRepository;
	private BandRepository bandRepository;
	private GenreRepository genreRepository;
	private InstrumentRepository instrumentRepository;
	private BandpositionRepository bandpositionRepository;
	private MusicianRepository musicianRepository;
	private GenderRepository genderRepository;	

	@Autowired
	public devBootstrap(AddressRepository addressRepository, BandRepository bandRepository,
			GenreRepository genreRepository, InstrumentRepository instrumentRepository,
			BandpositionRepository bandpositionRepository, MusicianRepository musicianRepository,
			GenderRepository genderRepository) {
		super();
		this.addressRepository = addressRepository;
		this.bandRepository = bandRepository;
		this.genreRepository = genreRepository;
		this.instrumentRepository = instrumentRepository;
		this.bandpositionRepository = bandpositionRepository;
		this.musicianRepository = musicianRepository;
		this.genderRepository = genderRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		initData();
	}

	private void initData() {
		
		// Delete data from database
		bandpositionRepository.deleteAll();
		bandRepository.deleteAll();
		musicianRepository.deleteAll();
		genderRepository.deleteAll();
		addressRepository.deleteAll();
		genreRepository.deleteAll();
		instrumentRepository.deleteAll();

		// Create Genders
		Gender male = createGenderIfNotExisting("Mann");
		Gender female = createGenderIfNotExisting("Frau");
		Gender diverse = createGenderIfNotExisting("Divers");
		
		// Create Genres
		Genre rock = createGenreIfNotExisting("Rock");
		Genre pop = createGenreIfNotExisting("Pop");
		Genre schlager = createGenreIfNotExisting("Schlager");
		Genre oldies = createGenreIfNotExisting("Oldies");

		// Create instruments
		Instrument schlagzeug = createInstrumentIfNotExisting("Schlagzeug");
		Instrument keyboard = createInstrumentIfNotExisting("Keyboard");
		Instrument egitarre = createInstrumentIfNotExisting("E-Gitarre");
		Instrument gesang = createInstrumentIfNotExisting("Gesang");

		// Create ACDC
		Band acdc = createBandIfNotExisting("ACDC");

		Address acdcAddress = createAdressIfNotExisting("Bonn", 53227);
		acdc.setAddress(acdcAddress);

		// Add favorite genres
		acdc.getFavoriteGenres().add(rock);
		rock.getBands().add(acdc);
		acdc.getFavoriteGenres().add(pop);
		pop.getBands().add(acdc);

		bandRepository.save(acdc);
		
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
		
		// Create Musician Stefan
		Musician stefan = createMusicianIfNotExisting(
				"Stefan", "Schlagzeuger", 
				LocalDate.of(1992, 01, 01), 
				male);

		Address stefansAddress = createAdressIfNotExisting("Sankt Augustin", 53757);
		stefan.setAddress(stefansAddress);

		stefan.setDescription("Hallo, ich bin Stefan, spiele Schlagzeug seit 6 Jahren und spiele unregelmäßig in einem Musikverein!");
		
		// Prepare Genre HashSet for Stefan
		HashSet<Genre> stefansGenres = new HashSet<>();
		stefansGenres.add(rock);
		rock.getMusicians().add(stefan);
		
		stefansGenres.add(pop);
		pop.getMusicians().add(stefan);
		
		stefansGenres.add(schlager);
		schlager.getMusicians().add(stefan);
		
		// Update favoriteGenres of Stefan
		stefan.setFavoriteGenres(stefansGenres);
		
		// Prepare Instruments HashSet
		HashSet<Instrument> stefansInstruments = new HashSet<>();
		stefansInstruments.add(schlagzeug);
		
		// Update Instruments of Stefan
		stefan.setInstruments(stefansInstruments);

		// Save changes in repositories after creation of Stefan
		instrumentRepository.save(schlagzeug);
		genreRepository.save(rock);
		genreRepository.save(schlager);
		genreRepository.save(pop);
		genreRepository.save(oldies);
		musicianRepository.save(stefan);
		
	}


	private Gender createGenderIfNotExisting(String name) {

		// Create gender, if not existing
		Optional<Gender> genderOptional = genderRepository.findByName(name);
		Gender gender = null;
		if (! genderOptional.isPresent()) {
			gender = new Gender();
			gender.setName(name);
			genderRepository.save(gender);
			return gender;
		} else {
			return genderOptional.get();
		}

	}
	
	private Musician createMusicianIfNotExisting(String firstName, String lastName, LocalDate birthday, Gender gender) {
		
		
		// Search for Musician based on parameters
		List<Musician> searchList = musicianRepository.findByFirstNameAndLastNameAndBirthday(
				firstName, lastName, birthday);
		
		// if no such musician exists
		if (searchList.size() == 0) {
			// create one
			Musician musician = new Musician();
			musician.setFirstName(firstName);
			musician.setLastName(lastName);
			musician.setBirthday(birthday);
			musician.setGender(gender);
			musicianRepository.save(musician);
			return musician;
		} else {
			// return the 1st one found
			return searchList.get(1);
		}
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

}


