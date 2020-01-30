package com.wildcodeschool.sea.bonn.whereismyband.entity;

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

	private boolean isVacant;
	
	public String getState() {
		return (this.isVacant ? "offen" : "besetzt");
	}

}
