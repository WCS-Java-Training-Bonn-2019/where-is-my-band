package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Bandposition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// import instrument and other details
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate birthFrom;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate birthTo;
		
}
