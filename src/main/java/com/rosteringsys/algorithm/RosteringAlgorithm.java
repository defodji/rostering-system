package com.rosteringsys.algorithm;

import java.util.Set;

import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.schedule.impl.Roster;


/**
 * Functional interface to be implemented for a rostering algorithm
 *
 */
public interface RosteringAlgorithm {

	/**
	 * Generates a roster using preferences of a set of volunteers
	 * @param preferences - volunteers preferences
	 * @param volunteers - set of volunteers
	 * @return generated roster
	 */
	public Roster generateRoster(Schedulable preferences, Set<Volunteer> volunteers);
}
