package com.wildcodeschool.sea.bonn.whereismyband.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.wildcodeschool.sea.bonn.whereismyband.controller.SearchController;

@Component
public class SkillLevelConverter implements Converter<String, SkillLevel> {

	@Override
	public SkillLevel convert(String skillID) {
				
		return SearchController.skillLevelOptions.get(Integer.parseInt(skillID)-1);
	}
	
}
