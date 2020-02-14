package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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

	@Enumerated(EnumType.STRING)
	private PositionState state;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@UpdateTimestamp
	private LocalDate lastUpdated;
	
}
