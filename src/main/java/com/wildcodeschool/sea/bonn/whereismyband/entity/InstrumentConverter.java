package com.wildcodeschool.sea.bonn.whereismyband.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.wildcodeschool.sea.bonn.whereismyband.controller.SearchController;

@Component
public class InstrumentConverter implements Converter<String, Instrument> {

	@Override
	public Instrument convert(String instrumentID) {
				
		return SearchController.instrumentOptions.get(Integer.parseInt(instrumentID)-1);
	}
	
}
