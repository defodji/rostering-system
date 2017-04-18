package com.rosteringsys.schedule;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;

import com.rosteringsys.shift.Shift;

/**
 * Interface for schedule of shifts - used by preferences and roster
 *
 */
public interface Schedulable {
	public static final EnumSet<DayOfWeek> WORK_DAYS = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
			DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

	public static final int MIN_VOLUNTEERS_PER_SHIFT = 2;

	public static final int MAX_SHIFTS_PER_VOLUNTEER = 3;

	/**
	 * Add a volunteer on a shift on the schedule
	 * @param shift
	 * @param name
	 */
	public void addVolunteer(Shift shift, String name);

	/**
	 * Set of volunteers for a shift
	 * @param shift
	 * @return
	 */
	public Set<String> getVolunteers(Shift shift);

}
