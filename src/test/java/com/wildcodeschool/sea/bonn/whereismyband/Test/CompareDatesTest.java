package com.wildcodeschool.sea.bonn.whereismyband.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;

//import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;

public class CompareDatesTest {
	
	@Test
	public void testCompareDate() throws Exception {
				
//		Bandposition band = new Bandposition();
//		LocalDateTime local = LocalDateTime.now();
//		LocalDateTime past = local.minusDays(7);
//		LocalDateTime random = local.minusHours(24);
//		
//		LocalDateTime db = band.getLastCreated();
//		
//		if(db != null) {
//			System.out.println(db);
//			System.out.println(band.getLastCreated());
//		}
//		System.out.println("Error");
		
		List<Bandposition> test = new ArrayList<>();
		List<Bandposition> result = test.subList(test.size()-3, test.size());
		
		System.out.println(result);
		
//		if(db.compareTo(local) < 0 && db.compareTo(past) > 0) {
//			System.out.println("Erfolg");
//		}
		
		
	}
}