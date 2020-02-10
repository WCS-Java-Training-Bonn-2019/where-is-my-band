package com.wildcodeschool.sea.bonn.whereismyband.services;

import java.io.IOException;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

// KrillMi 08.02.2020 => needed for converting the MultipartFile from Browser into a byte array
public class MultipartFileToByteArrayConverter implements Converter<MultipartFile, byte[]>{

	@Override
	public byte[] convert(MultipartFile source) {

		byte[] byteArray = new byte[(int) source.getSize()];
		try {
			byteArray = source.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}

}
