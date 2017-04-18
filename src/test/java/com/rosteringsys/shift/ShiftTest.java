package com.rosteringsys.shift;

import static org.junit.Assert.*;

import java.time.DayOfWeek;

import org.junit.Before;
import org.junit.Test;

import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

public class ShiftTest {
	private Shift shift;

	@Before
	public void setUp() throws Exception {
		shift = new Shift(DayOfWeek.MONDAY, PeriodDay.MORNING);
	}

	@Test
	public void testGets() {
		assertEquals(shift.getDay(), DayOfWeek.MONDAY);
		assertEquals(shift.getPeriod(), PeriodDay.MORNING);
	}
	
	@Test
	public void testEquals() {
		Shift shift2 = new Shift(DayOfWeek.MONDAY, PeriodDay.AFTERNOON);
		assertFalse(shift.equals(shift2));
		shift2 = new Shift(DayOfWeek.MONDAY, PeriodDay.MORNING);
		assertTrue(shift.equals(shift2));
		assertEquals(shift.hashCode(), shift2.hashCode());
	}
	
	@Test
	public void testCompareTo() {
		Shift shift2 = new Shift(DayOfWeek.TUESDAY, PeriodDay.AFTERNOON);
		assertTrue(shift.compareTo(shift2) < 0);
		Shift shift3 = new Shift(DayOfWeek.MONDAY, PeriodDay.AFTERNOON);
		assertTrue(shift.compareTo(shift3) < 0);
		assertEquals(shift.compareTo(shift), 0);
	}

}
