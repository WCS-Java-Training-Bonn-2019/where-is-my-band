package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bandposition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer ageFrom;
	private Integer ageTo;

	@ManyToOne
	@JoinColumn(name = "band_id")
	private Band band;

	@ManyToOne
	@JoinColumn (name = "instrument_id")
	private Instrument instrument;

	private PositionState state;
	
	private LocalDateTime lastCreated;
	
}
