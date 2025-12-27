package org.hkrdi.eden.ggm.algebraic.test;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MullersTest {

	@Test
	public void testBigDecimal() {
		List<BigDecimal> list = new ArrayList(Arrays.asList(new BigDecimal[] {new BigDecimal(4), new BigDecimal(4.25)}));
		int iterations = 0;
		for (int i=2; i<iterations; i++) {
			list.add(mullers(list.get(i-1), list.get(i-2), iterations*2));
			System.out.println("x"+i+":"+ list.get(i));
		}
	}
	
	//see https://latkin.org/blog/2014/11/22/mullers-recurrence-roundoff-gone-wrong/
	public BigDecimal mullers(BigDecimal xi, BigDecimal xi1, int iterations) {
		MathContext mathContext = new MathContext(iterations, RoundingMode.HALF_EVEN);
		return new BigDecimal(108, mathContext).subtract(
				new BigDecimal(815, mathContext).subtract(
						new BigDecimal(1500, mathContext).divide(xi1, mathContext)).divide(xi, mathContext)
			);
	}
	
	@Test
	public void testDouble() {
		List<Double> list = new ArrayList(Arrays.asList(new Double[] {4d, 4.25}));
		int iterations = 20;
		for (int i=2; i<iterations; i++) {
			list.add(mullers(list.get(i-1), list.get(i-2)));
			System.out.println("x"+i+":"+ list.get(i));
		}
	}
	
	public double mullers(double di, double di1) {
		return 108-(815-1500/di1)/di;
	}
	
}
