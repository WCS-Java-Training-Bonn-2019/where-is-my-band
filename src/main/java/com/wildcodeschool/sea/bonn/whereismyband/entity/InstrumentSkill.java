package com.wildcodeschool.sea.bonn.whereismyband.entity;

public class InstrumentSkill {

	// database id
	private Long id;
	private Instrument instrument;
	private SkillLevel level;

	public InstrumentSkill(Long id, Instrument instrument, SkillLevel level) {
		super();
		this.id = id;
		this.instrument = instrument;
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public SkillLevel getLevel() {
		return level;
	}

	public void setLevel(SkillLevel level) {
		this.level = level;
	}
	
}
