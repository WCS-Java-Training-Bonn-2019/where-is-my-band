package com.wildcodeschool.sea.bonn.whereismyband.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.wildcodeschool.sea.bonn.whereismyband.controller.SearchController;

@Component
public class GenderConverter implements Converter<String, Gender> {

	@Override
	public Gender convert(String genderStr) {
				
		return SearchController.genderOptions.get(Integer.parseInt(genderStr)-1);
	}
	
}
