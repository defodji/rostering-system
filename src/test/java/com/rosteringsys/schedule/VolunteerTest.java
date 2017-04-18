package com.rosteringsys.schedule;

import static org.junit.Assert.*;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;

public class VolunteerTest {

	Volunteer volunteer;
	
	@Before
	public void setUp() throws Exception {
		volunteer = new Volunteer("John");
	}

	@Test
	public void testName() {
		assertEquals(volunteer.getName(), "John");
	}
	
	@Test
	public void testInitShifts() {
		assertEquals(volunteer.getNbShifts(), 0);
	}
	
	@Test
	public void testPreferredShifts() throws Exception {
		volunteer.addPreferredShifts(2);
		assertEquals(2, volunteer.getNbPreferredShiftsLeft());
	}
	
	@Test
	public void testLastChanceForShift() throws Exception {
		volunteer.addPreferredShifts(1);
		assertTrue(volunteer.isLastChanceForShift());
		volunteer.addPreferredShifts(1);
		assertFalse(volunteer.isLastChanceForShift());
	}	
	
	@Test
	public void testAddShift() {
		volunteer.addPreferredShifts(3);
		volunteer.addShift();
		assertEquals(volunteer.getNbShifts(), 1);
		assertEquals(volunteer.getNbPreferredShiftsLeft(), 2);
		volunteer.addShift();
		assertEquals(volunteer.getNbShifts(), 2);
		assertEquals(volunteer.getNbPreferredShiftsLeft(), 1);
		volunteer.addShift();
		assertEquals(volunteer.getNbShifts(), 3);
		assertEquals(volunteer.getNbPreferredShiftsLeft(), 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddShiftException() throws Exception{
		IntStream.rangeClosed(0, Schedulable.MAX_SHIFTS_PER_VOLUNTEER+1).forEach(i -> volunteer.addShift());
	}
	
	@Test
	public void testCompareTo() {
		Volunteer volunteer2 = new Volunteer("Mark");
		volunteer2.addPreferredShifts(1);
		volunteer2.addShift();
		assertTrue(volunteer.compareTo(volunteer2) < 0);
		volunteer.addPreferredShifts(1);
		volunteer.addShift();
		assertTrue(volunteer.compareTo(volunteer2) < 0);
		volunteer.addPreferredShifts(2);
		assertTrue(volunteer.compareTo(volunteer2) > 0);
		assertTrue(volunteer.equals(volunteer));
	}

}
