package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Bandposition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// import instrument and other details
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate birthFrom;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate birthTo;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getBirthFrom() {
		return birthFrom;
	}
	public void setBirthFrom(LocalDate birthFrom) {
		this.birthFrom = birthFrom;
	}
	public LocalDate getBirthTo() {
		return birthTo;
	}
	public void setBirthTo(LocalDate birthTo) {
		this.birthTo = birthTo;
	}
	
	
	
}
