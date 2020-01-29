package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Instrument {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany (mappedBy = "instrument")
	private List<Bandposition> bandpositions = new ArrayList<>();
	
	@ManyToMany(mappedBy = "instruments")
	private List<Musician> musicians = new ArrayList<>();
		
}

