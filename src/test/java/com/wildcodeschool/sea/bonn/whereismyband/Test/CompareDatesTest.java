package com.wildcodeschool.sea.bonn.whereismyband.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;

//import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;

public class CompareDatesTest {
	

	@Test
	public void testCompareDate() throws Exception {
				
		Bandposition band = new Bandposition();
		
		
		LocalDateTime local = LocalDateTime.now();
//		LocalDate past = local.minusDays(7);
//		LocalDate random = local.minusDays(1);
		LocalDate db2 = band.getLastUpdated();


//		if(db2 != null) {
//			
//			System.out.println("db2: " + db2);
//			System.out.println("local: " + local);
//			System.out.println("past: " + past);
//			System.out.println("random: " + random);
//			
//		}
		System.out.println(local);
		System.out.println(band.getLastUpdated());
		
		
		

		
//		if(db.compareTo(local) < 0 && db.compareTo(past) > 0) {
//			System.out.println("Erfolg");
//		}
		
		
	}
}