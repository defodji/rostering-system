package com.rosteringsys.schedule.impl;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.rosteringsys.exception.InvalidRosterException;
import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.schedule.impl.Roster;
import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

public class RosterTest {
	
	private Roster roster;
	private Set<Volunteer> volunteers;

	@Before
	public void setUp() throws Exception {
		roster = new Roster();
		volunteers = new TreeSet<>();
		
		volunteers.add(new Volunteer("John", 10));
		volunteers.add(new Volunteer("Mark", 10));
		volunteers.add(new Volunteer("Chloe", 10));
		volunteers.add(new Volunteer("Zoe", 10));
		volunteers.add(new Volunteer("Frank", 10));
		volunteers.add(new Volunteer("Simon", 10));
		volunteers.add(new Volunteer("Mia", 10));
		volunteers.add(new Volunteer("Helena", 10));
		volunteers.add(new Volunteer("Alan", 10));
		
	}

	@Test
	public void testAdd() {
		Shift mondayMorningShift = new Shift(DayOfWeek.MONDAY,  PeriodDay.MORNING);
		roster.addVolunteer(mondayMorningShift, "John");
		roster.addVolunteer(mondayMorningShift, "Mark");
		Set<String> volunteers = roster.getVolunteers(mondayMorningShift);
		assertEquals(volunteers.size(), 2);
		assertTrue(volunteers.contains("John"));
		assertTrue(volunteers.contains("Mark"));
	}
	
	@Test
	public void testHasShiftInDay() {
		Shift mondayMorningShift = new Shift(DayOfWeek.MONDAY,  PeriodDay.MORNING);
		roster.addVolunteer(mondayMorningShift, "John");
		Shift mondayAfternoonShift = new Shift(DayOfWeek.MONDAY,  PeriodDay.AFTERNOON);
		assertTrue(roster.hasShiftInDay("John", mondayAfternoonShift));
		Shift tuesdayMorningShift = new Shift(DayOfWeek.TUESDAY,  PeriodDay.MORNING);
		assertFalse(roster.hasShiftInDay("John", tuesdayMorningShift));
	}
	
	@Test(expected=InvalidRosterException.class)
	public void validateMinVolunteersTest() throws Exception {
		
		roster.validate(volunteers);
	}
	
	@Test(expected=InvalidRosterException.class)
	public void validateMinShiftsTest() throws Exception {
		Volunteer john = volunteers.stream().filter(s -> s.getName().equals("John")).findFirst().get();
		Volunteer chloe = volunteers.stream().filter(s -> s.getName().equals("Chloe")).findFirst().get();
		Volunteer mark = volunteers.stream().filter(s -> s.getName().equals("Mark")).findFirst().get();
		Volunteer zoe = volunteers.stream().filter(s -> s.getName().equals("Zoe")).findFirst().get();
		Volunteer frank = volunteers.stream().filter(s -> s.getName().equals("Frank")).findFirst().get();
		Volunteer mia = volunteers.stream().filter(s -> s.getName().equals("Mia")).findFirst().get();
		Volunteer simon = volunteers.stream().filter(s -> s.getName().equals("Simon")).findFirst().get();
		Volunteer helena = volunteers.stream().filter(s -> s.getName().equals("Helena")).findFirst().get();
		for (DayOfWeek aDay : Schedulable.WORK_DAYS) {
			for (PeriodDay aPeriod : PeriodDay.values()) {
				if (aPeriod == PeriodDay.MORNING) {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						roster.addVolunteer(new Shift(aDay, aPeriod), "John");
						john.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Chloe");
						chloe.addShift();
					} else {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Frank");
						frank.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Mia");
						mia.addShift();
					}
				} else {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Mark");
						mark.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Zoe");
						zoe.addShift();
					} else {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Simon");
						simon.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Helena");
						helena.addShift();
					}
				}
			}
		}
		roster.validate(volunteers);
	}
	
	@Test(expected=InvalidRosterException.class)
	public void validateMaxDayTest() throws Exception {
		Volunteer john = volunteers.stream().filter(s -> s.getName().equals("John")).findFirst().get();
		Volunteer chloe = volunteers.stream().filter(s -> s.getName().equals("Chloe")).findFirst().get();
		Volunteer mark = volunteers.stream().filter(s -> s.getName().equals("Mark")).findFirst().get();
		Volunteer zoe = volunteers.stream().filter(s -> s.getName().equals("Zoe")).findFirst().get();
		Volunteer frank = volunteers.stream().filter(s -> s.getName().equals("Frank")).findFirst().get();
		Volunteer mia = volunteers.stream().filter(s -> s.getName().equals("Mia")).findFirst().get();
		Volunteer simon = volunteers.stream().filter(s -> s.getName().equals("Simon")).findFirst().get();
		Volunteer helena = volunteers.stream().filter(s -> s.getName().equals("Helena")).findFirst().get();
		Volunteer alan = volunteers.stream().filter(s -> s.getName().equals("Alan")).findFirst().get();
		for (DayOfWeek aDay : Schedulable.WORK_DAYS) {
			for (PeriodDay aPeriod : PeriodDay.values()) {
				if (aPeriod == PeriodDay.MORNING) {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						roster.addVolunteer(new Shift(aDay, aPeriod), "John");
						john.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Chloe");
						chloe.addShift();
					} else {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Frank");
						frank.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Mia");
						mia.addShift();
					}
				} else {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Mark");
						mark.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Zoe");
						zoe.addShift();
					} else {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Simon");
						simon.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Helena");
						helena.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Alan");
						alan.addShift();
					}
				}
			}
		}
		roster.addVolunteer(new Shift(DayOfWeek.MONDAY, PeriodDay.AFTERNOON), "John");
		roster.validate(volunteers);
	}	
	
	@Test
	public void validTest() throws Exception {
		Volunteer john = volunteers.stream().filter(s -> s.getName().equals("John")).findFirst().get();
		Volunteer chloe = volunteers.stream().filter(s -> s.getName().equals("Chloe")).findFirst().get();
		Volunteer mark = volunteers.stream().filter(s -> s.getName().equals("Mark")).findFirst().get();
		Volunteer zoe = volunteers.stream().filter(s -> s.getName().equals("Zoe")).findFirst().get();
		Volunteer frank = volunteers.stream().filter(s -> s.getName().equals("Frank")).findFirst().get();
		Volunteer mia = volunteers.stream().filter(s -> s.getName().equals("Mia")).findFirst().get();
		Volunteer simon = volunteers.stream().filter(s -> s.getName().equals("Simon")).findFirst().get();
		Volunteer helena = volunteers.stream().filter(s -> s.getName().equals("Helena")).findFirst().get();
		Volunteer alan = volunteers.stream().filter(s -> s.getName().equals("Alan")).findFirst().get();
		volunteers.remove(alan);
		for (DayOfWeek aDay : Schedulable.WORK_DAYS) {
			for (PeriodDay aPeriod : PeriodDay.values()) {
				if (aPeriod == PeriodDay.MORNING) {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						roster.addVolunteer(new Shift(aDay, aPeriod), "John");
						john.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Chloe");
						chloe.addShift();
					} else {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Frank");
						frank.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Mia");
						mia.addShift();
					}
				} else {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Mark");
						mark.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Zoe");
						zoe.addShift();
					} else {
						roster.addVolunteer(new Shift(aDay, aPeriod), "Simon");
						simon.addShift();
						roster.addVolunteer(new Shift(aDay, aPeriod), "Helena");
						helena.addShift();
					}
				}
			}
		}
		assertTrue(roster.validate(volunteers));
	}	
	

}
