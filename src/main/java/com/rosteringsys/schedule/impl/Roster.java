package com.rosteringsys.schedule.impl;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.rosteringsys.exception.InvalidRosterException;
import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

/**
 * Roster of shifts with assigned volunteers
 *
 */
public class Roster implements Schedulable {

	private Map<Shift, Set<String>> assignations = new TreeMap<>();

	public Roster() {
		for (DayOfWeek aDay : WORK_DAYS) {
			for (PeriodDay aPeriod : PeriodDay.values()) {
				assignations.put(new Shift(aDay, aPeriod), new HashSet<>());
			}
		}
	}

	@Override
	public void addVolunteer(Shift shift, String name) {
		assignations.get(shift).add(name);
	}

	@Override
	public Set<String> getVolunteers(Shift shift) {
		return assignations.get(shift);
	}

	
	/**
	 * Indicates if the volunteer has been assigned in the other shift of the same day as the shift
	 * in parameter
	 * @param volunteerName - name of volunteer
	 * @param shift - shift
	 * @return
	 */
	public boolean hasShiftInDay(String volunteerName, Shift shift) {
		if (shift == null) {
			return false;
		}
		return assignations.get(new Shift(shift.getDay(), PeriodDay.otherPeriod(shift.getPeriod())))
				.contains(volunteerName);
	}

	public void print(PrintStream ps) {
		ps.println("Generated roster: ");
		for (Shift aShift : assignations.keySet()) {
			ps.print(aShift.getDay().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " - "
					+ aShift.getPeriod().getValue() + " : ");
			StringJoiner shiftStr = new StringJoiner(", ");
			assignations.get(aShift).stream().forEach(s -> shiftStr.add(s));
			ps.println(shiftStr.toString());
		}
	}

	public boolean validate(Set<Volunteer> volunteers) throws InvalidRosterException {
		// every shift with at least 2 volunteers
		for (Shift aShift : assignations.keySet()) {
			if (assignations.get(aShift).size() < MIN_VOLUNTEERS_PER_SHIFT) {
				System.out.print("Shift not enough volunteers : " + aShift.getDay().toString() + " - "
						+ aShift.getPeriod().getValue() + " Volunteers:");
				assignations.get(aShift).stream().forEach(System.out::println);
				throw new InvalidRosterException(
						"Each shift must have at least " + MIN_VOLUNTEERS_PER_SHIFT + " volunteers.");
			}
		}
		// each volunteer must have at least one shift and a maximum of
		// MAX_SHIFTS_PER_VOLUNTEER
		for (Volunteer aVolunteer : volunteers) {
			if (aVolunteer.getNbShifts() == 0 || aVolunteer.getNbShifts() > MAX_SHIFTS_PER_VOLUNTEER) {
				throw new InvalidRosterException(
						"Each volunteer must have at least a shift and a maximum of" + MAX_SHIFTS_PER_VOLUNTEER + ".");
			}
		}
		// A volunteer cannot have more than one shift in a day
		for (DayOfWeek day : WORK_DAYS) {
			if (assignations.get(new Shift(day, PeriodDay.MORNING)).stream()
					.filter(assignations.get(new Shift(day, PeriodDay.AFTERNOON))::contains).collect(Collectors.toSet())
					.size() > 0) {
				throw new InvalidRosterException("A volunteer cannot have more than one shift in a day");
			}
		}
		return true;
	}

}
