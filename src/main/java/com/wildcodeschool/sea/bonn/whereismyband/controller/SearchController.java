package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wildcodeschool.sea.bonn.whereismyband.entity.BandSearch;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Gender;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;
import com.wildcodeschool.sea.bonn.whereismyband.entity.SkillLevel;

@Controller
public class SearchController {
	
	public static List<Gender> genderOptions;
	public static List<Instrument> instrumentOptions;
	public static List<Genre> genreOptions;
	public static List<SkillLevel> skillLevelOptions;
	
	private BandSearch search;
	
	// method loading the initial data 
	@PostConstruct
	private void loadData() {

		// Liste verfügbarer Instrumente, normalerweise aus DB gelesen
		instrumentOptions = Arrays.asList(
				new Instrument(1L, "Trompete"),
				new Instrument(2L, "Gitarre"),
				new Instrument(3L, "Schlagzeug"),
				new Instrument(4L, "Klavier"));

		// Liste verfügbarer Geschlechter, normalerweise aus DB gelesen
		genderOptions = Arrays.asList(
				new Gender(1L, "divers"),
				new Gender(2L, "Herr"),
				new Gender(3L, "Frau"));

		// Liste verfügbarer Geschlechter, normalerweise aus DB gelesen
		genreOptions = Arrays.asList(
				new Genre(1L, "Rock"),
				new Genre(2L, "Metal"),
				new Genre(3L, "Schlager"));

		// Liste verfügbarer Geschlechter, normalerweise aus DB gelesen
		skillLevelOptions = Arrays.asList(
				new SkillLevel(1L, "Keine"),
				new SkillLevel(2L, "Anfänger"),
				new SkillLevel(3L, "Forgeschrittener"),
				new SkillLevel(4L, "Profi"));

		search = new BandSearch();

	}
	@RequestMapping("/")
	public String searchForm(Model theModel) {
		theModel.addAttribute("search", search);
		theModel.addAttribute("genderOptions", genderOptions);
		theModel.addAttribute("skillLevelOptions", skillLevelOptions);
		theModel.addAttribute("instrumentOptions", instrumentOptions);
		theModel.addAttribute("genreOptions", genreOptions);
		return ("index");
	}
	
	@RequestMapping("/search")
	public String searchResult (@ModelAttribute("search") BandSearch search) {
		System.out.println(search);
		return "searchResult";
	}	

}
