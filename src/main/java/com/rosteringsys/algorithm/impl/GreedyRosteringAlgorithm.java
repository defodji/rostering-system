package com.rosteringsys.algorithm.impl;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.rosteringsys.algorithm.RosteringAlgorithm;
import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.schedule.impl.Roster;
import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

/**
 * Generates roster by using a greedy-like algorithm. 
 * It iterates in order over the shifts and at every step it strives to select
 * the best volunteers (using the current number of shifts already assigned to them and 
 * the number of preferences left for them).
 */
public class GreedyRosteringAlgorithm implements RosteringAlgorithm {

	@Override
	public Roster generateRoster(Schedulable preferences, Set<Volunteer> volunteers) {
		Roster roster = new Roster();
		for (DayOfWeek aDay : Schedulable.WORK_DAYS) {
			for (PeriodDay aPeriod : PeriodDay.values()) {
				allocateShift(roster, aDay, aPeriod, preferences, volunteers);
			}
		}
		return roster;
	}

	/**
	 * Assign the volunteers for the current shift 
	 * @param roster - roster to be updated
	 * @param day - day of the shift
	 * @param period - period of the shift
	 * @param preferences - preferences of all volunteers
	 * @param volunteers - set of volunteers
	 */
	public void allocateShift(Roster roster, DayOfWeek day, PeriodDay period, Schedulable preferences,
			Set<Volunteer> volunteers) {
		int nbVolunteers = 0;
		Shift shift = new Shift(day, period);
		Set<String> availableForShift = preferences.getVolunteers(shift);
		
		SortedSet<Volunteer> sortedVolunteers = new TreeSet<>();
		sortedVolunteers.addAll(volunteers); // sort volunteers by number of shifts and 
											// remaining preferences
		
		for (Volunteer aVolunteer : sortedVolunteers) {
			if (availableForShift.contains(aVolunteer.getName())) {
				if (aVolunteer.isLastChanceForShift()) { // last change to assign a shift to this volunteer
					nbVolunteers = addVolunteerToShift(roster, aVolunteer, shift, nbVolunteers);
				} else if (nbVolunteers < Schedulable.MIN_VOLUNTEERS_PER_SHIFT && aVolunteer.canAcceptShift()
						&& !roster.hasShiftInDay(aVolunteer.getName(), shift)) {
					nbVolunteers = addVolunteerToShift(roster, aVolunteer, shift, nbVolunteers);
				} else {
					aVolunteer.decreasePreferredShifts();
				}
			}
		}
	}

	private int addVolunteerToShift(Roster roster, Volunteer volunteer, Shift shift, int nbVolunteers) {
		volunteer.addShift();
		roster.addVolunteer(shift, volunteer.getName());
		nbVolunteers++;
		return nbVolunteers;
	}
}
