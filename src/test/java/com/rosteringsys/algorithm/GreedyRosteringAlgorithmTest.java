package com.rosteringsys.algorithm;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.rosteringsys.algorithm.impl.GreedyRosteringAlgorithm;
import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.schedule.impl.Preferences;
import com.rosteringsys.schedule.impl.Roster;
import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

public class GreedyRosteringAlgorithmTest {
	
	GreedyRosteringAlgorithm algorithm;
	private SortedSet<Volunteer> volunteers;
	private Preferences preferences;
	

	@Before
	public void setUp() throws Exception {
		algorithm = new GreedyRosteringAlgorithm();
		volunteers = new TreeSet<>();
		volunteers.add(new Volunteer("John", 3));
		volunteers.add(new Volunteer("Mark", 3));	
		volunteers.add(new Volunteer("Chloe", 3));
		volunteers.add(new Volunteer("Zoe", 3));
		volunteers.add(new Volunteer("Frank", 2));
		volunteers.add(new Volunteer("Simon", 2));
		volunteers.add(new Volunteer("Mia", 2));
		volunteers.add(new Volunteer("Helena", 2));
		preferences = new Preferences();
	}

	@Test
	public void testGenerateRoster() throws Exception{

		for (DayOfWeek aDay : Schedulable.WORK_DAYS) {
			for (PeriodDay aPeriod : PeriodDay.values()) {
				if (aPeriod == PeriodDay.MORNING) {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						preferences.addVolunteer(new Shift(aDay, aPeriod), "John");
						preferences.addVolunteer(new Shift(aDay, aPeriod), "Chloe");
					} else {
						preferences.addVolunteer(new Shift(aDay, aPeriod), "Frank");
						preferences.addVolunteer(new Shift(aDay, aPeriod), "Mia");
					}
				} else {
					if (aDay.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
						preferences.addVolunteer(new Shift(aDay, aPeriod), "Mark");
						preferences.addVolunteer(new Shift(aDay, aPeriod), "Zoe");
					} else {
						preferences.addVolunteer(new Shift(aDay, aPeriod), "Simon");
						preferences.addVolunteer(new Shift(aDay, aPeriod), "Helena");
					}
				}
			}
		}		
		assertTrue(preferences.validate(volunteers));
		Roster roster = algorithm.generateRoster(preferences, volunteers);
		assertTrue(roster.validate(volunteers));
	}

	@Test
	public void testAllocateShiftChooseAll() {
		Roster roster = new Roster();
		Shift mondayMorning = new Shift(DayOfWeek.MONDAY, PeriodDay.MORNING);
		preferences.addVolunteer(mondayMorning, "John");
		preferences.addVolunteer(mondayMorning, "Mark");
		
		algorithm.allocateShift(roster, DayOfWeek.MONDAY, PeriodDay.MORNING, preferences, volunteers);
		
		Set<String> mondayMorningVolunteers = roster.getVolunteers(mondayMorning);
		assertTrue(mondayMorningVolunteers.contains("John"));
		assertTrue(mondayMorningVolunteers.contains("Mark"));
		
		Volunteer john = volunteers.stream().filter(s -> s.getName().equals("John")).findFirst().get();
		Volunteer mark = volunteers.stream().filter(s -> s.getName().equals("Mark")).findFirst().get();
		assertEquals(john.getNbShifts(), 1);
		assertEquals(mark.getNbShifts(), 1);
	}
	
	@Test
	public void testAllocateShiftChooseLowerNbShifts() {
		Roster roster = new Roster();
		Shift mondayMorning = new Shift(DayOfWeek.MONDAY, PeriodDay.MORNING);
		preferences.addVolunteer(mondayMorning, "John");
		preferences.addVolunteer(mondayMorning, "Mark");
		preferences.addVolunteer(mondayMorning, "Chloe");
		Volunteer chloe = volunteers.stream().filter(s -> s.getName().equals("Chloe")).findFirst().get();
		Volunteer john = volunteers.stream().filter(s -> s.getName().equals("John")).findFirst().get();		
		Volunteer mark = volunteers.stream().filter(s -> s.getName().equals("Mark")).findFirst().get();	
		volunteers.remove(john);
		john.addShift();
		john.addShift();		
		mark.addShift();
		volunteers.add(john);
		
		
		algorithm.allocateShift(roster, DayOfWeek.MONDAY, PeriodDay.MORNING, preferences, volunteers);
		
		Set<String> mondayMorningVolunteers = roster.getVolunteers(mondayMorning);
		assertTrue(mondayMorningVolunteers.contains("Chloe"));
		assertTrue(mondayMorningVolunteers.contains("Mark"));
		
		assertEquals(mark.getNbShifts(), 2);
		assertEquals(chloe.getNbShifts(), 1);
	}	
	
	@Test
	public void testAllocateShiftChooseLastChance() {
		Roster roster = new Roster();
		Shift mondayMorning = new Shift(DayOfWeek.MONDAY, PeriodDay.MORNING);
		preferences.addVolunteer(mondayMorning, "John");
		preferences.addVolunteer(mondayMorning, "Mark");
		preferences.addVolunteer(mondayMorning, "Chloe");
		Volunteer chloe = volunteers.stream().filter(s -> s.getName().equals("Chloe")).findFirst().get();
		Volunteer john = volunteers.stream().filter(s -> s.getName().equals("John")).findFirst().get();		
		Volunteer mark = volunteers.stream().filter(s -> s.getName().equals("Mark")).findFirst().get();	
		volunteers.remove(john);
		john.decreasePreferredShifts();
		john.decreasePreferredShifts();
		mark.decreasePreferredShifts();
		mark.decreasePreferredShifts();
		chloe.decreasePreferredShifts();
		chloe.decreasePreferredShifts();
		volunteers.add(john);
		
		
		algorithm.allocateShift(roster, DayOfWeek.MONDAY, PeriodDay.MORNING, preferences, volunteers);
		
		Set<String> mondayMorningVolunteers = roster.getVolunteers(mondayMorning);
		assertTrue(mondayMorningVolunteers.contains("Chloe"));
		assertTrue(mondayMorningVolunteers.contains("Mark"));
		assertTrue(mondayMorningVolunteers.contains("John"));
		
		assertEquals(mark.getNbShifts(), 1);
		assertEquals(chloe.getNbShifts(), 1);
		assertEquals(john.getNbShifts(), 1);
	}		

}
