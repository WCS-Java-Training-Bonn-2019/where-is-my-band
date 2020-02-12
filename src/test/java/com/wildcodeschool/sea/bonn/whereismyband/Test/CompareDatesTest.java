package com.wildcodeschool.sea.bonn.whereismyband.Test;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;

//import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;

public class CompareDatesTest {
	
	@Test
	public void testCompareDate() throws Exception {
		
		Bandposition band = new Bandposition();
		LocalDateTime local = LocalDateTime.now();
		LocalDateTime past = local.minusDays(7);
		LocalDateTime random = local.minusHours(24);
		
		LocalDateTime db = band.getLastCreated();
		
		if(db != null) {
			System.out.println(db);
			System.out.println(band.getLastCreated());
		}
		System.out.println("Error");
		
//		if(db.compareTo(local) < 0 && db.compareTo(past) > 0) {
//			System.out.println("Erfolg");
//		}
		
		
	}
}