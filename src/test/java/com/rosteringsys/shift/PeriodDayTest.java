package com.rosteringsys.shift;

import static org.junit.Assert.*;

import org.junit.Test;

import com.rosteringsys.shift.PeriodDay;

public class PeriodDayTest {

	@Test
	public void testFromString() {
		assertEquals(PeriodDay.AFTERNOON, PeriodDay.fromString("Afternoon"));
		assertEquals(PeriodDay.MORNING, PeriodDay.fromString("Morning"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromStringInvalid() {
		PeriodDay.fromString("None");
	}	

	@Test
	public void testOtherPeriod() {
		assertEquals(PeriodDay.AFTERNOON, PeriodDay.otherPeriod(PeriodDay.MORNING));
		assertEquals(PeriodDay.MORNING, PeriodDay.otherPeriod(PeriodDay.AFTERNOON));
	}

}
