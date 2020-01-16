package com.wildcodeschool.sea.bonn.whereismyband.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.wildcodeschool.sea.bonn.whereismyband.controller.SearchController;

@Component
public class GenreConverter implements Converter<String, Genre> {

	@Override
	public Genre convert(String genreID) {
				
		return SearchController.genreOptions.get(Integer.parseInt(genreID)-1);
	}
	
}
