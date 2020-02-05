//package com.wildcodeschool.sea.bonn.whereismyband.services;
//
//import java.io.IOException;
//import java.util.Iterator;
//
//import javax.imageio.ImageIO;
//import javax.imageio.ImageReader;
//import javax.imageio.stream.ImageInputStream;
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//public class JPEGOrNullValidator implements
//ConstraintValidator<JPEGOrNullConstraint, byte[]> {
//
//	@Override
//	public void initialize(JPEGOrNullConstraint image) {
//	}
//
//	@Override
//	public boolean isValid(byte[] image,
//			ConstraintValidatorContext cxt) {
//
//		boolean isJPEG=false;
//		
//		if (image.length == 0) {
//			return true;
//		}
//		
//		try {
//
//			ImageInputStream iis = ImageIO.createImageInputStream(image);
//
//			// get all currently registered readers that recognize the image format
//			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
//
//			// if not at least one reader was found
//			if (!iter.hasNext()) {
//
//				throw new RuntimeException("No readers found!");
//
//			}
//
//			// get the first reader
//			ImageReader reader = iter.next();
//
//			// set result variable
//			isJPEG = reader.getFormatName() == "JPEG";
//
//			// close stream
//			iis.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		};
//
//		return isJPEG;
//	}
//
//}