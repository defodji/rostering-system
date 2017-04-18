package com.rosteringsys.schedule.impl;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.rosteringsys.exception.InvalidPreferencesException;
import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.schedule.impl.Preferences;
import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

public class PreferencesTest {
	
	Preferences preferences;
	private SortedSet<Volunteer> volunteers;

	@Before
	public void setUp() throws Exception {
		preferences = new Preferences();
		volunteers = new TreeSet<>();
		volunteers.add(new Volunteer("John", 10));
		volunteers.add(new Volunteer("Mark", 10));	
	}

	@Test
	public void testAdd() {
		Shift mondayMorningShift = new Shift(DayOfWeek.MONDAY,  PeriodDay.MORNING);
		preferences.addVolunteer(mondayMorningShift, "John");
		preferences.addVolunteer(mondayMorningShift, "Mark");
		Set<String> volunteers = preferences.getVolunteers(mondayMorningShift);
		assertEquals(volunteers.size(), 2);
		assertTrue(volunteers.contains("John"));
		assertTrue(volunteers.contains("Mark"));
	}
	
	@Test(expected=InvalidPreferencesException.class)
	public void testNotEnoughVolunteers() throws Exception {
		preferences.validate(volunteers);
	}
	
	@Test(expected=InvalidPreferencesException.class)
	public void testNotEnoughPreferences() throws Exception {
		volunteers.add(new Volunteer("Chloe", 10));
		volunteers.add(new Volunteer("Zoe", 10));
		volunteers.add(new Volunteer("Frank", 10));
		volunteers.add(new Volunteer("Simon", 10));
		volunteers.add(new Volunteer("Mia", 10));
		preferences.validate(volunteers);
	}
	
	@Test(expected=InvalidPreferencesException.class)
	public void testNotEnoughPreferencesPerShift() throws Exception {
		volunteers.add(new Volunteer("Chloe", 10));
		volunteers.add(new Volunteer("Zoe", 10));
		volunteers.add(new Volunteer("Frank", 10));
		volunteers.add(new Volunteer("Simon", 10));
		volunteers.add(new Volunteer("Mia", 10));
		preferences.validate(volunteers);
	}	
	
	@Test(expected=InvalidPreferencesException.class)
	public void testVolunteerNoPreferences() throws Exception {
		volunteers.add(new Volunteer("Chloe", 10));
		volunteers.add(new Volunteer("Zoe", 10));
		volunteers.add(new Volunteer("Frank", 10));
		volunteers.add(new Volunteer("Simon", 10));
		volunteers.add(new Volunteer("Mia", 10));
		volunteers.add(new Volunteer("Helena", 10));
		volunteers.add(new Volunteer("Alan"));
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
		preferences.validate(volunteers);
	}		
	
	@Test
	public void testValid() throws Exception {
		volunteers.add(new Volunteer("Chloe", 10));
		volunteers.add(new Volunteer("Zoe", 10));
		volunteers.add(new Volunteer("Frank", 10));
		volunteers.add(new Volunteer("Simon", 10));
		volunteers.add(new Volunteer("Mia", 10));
		volunteers.add(new Volunteer("Helena", 10));
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
	}			

}
