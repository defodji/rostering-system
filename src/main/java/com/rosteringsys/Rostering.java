package com.rosteringsys;

import java.io.IOException;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.util.Set;

import com.rosteringsys.algorithm.RosteringAlgorithm;
import com.rosteringsys.exception.InvalidPreferencesException;
import com.rosteringsys.exception.InvalidRosterException;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.schedule.impl.Preferences;
import com.rosteringsys.schedule.impl.Roster;
import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

/**
 * Generate a roster from volunteer preferences following a specified algorithm.
 * Gives the option to load preferences from an excel file and to automatically print
 * generated roster on specified print stream.
 */
public class Rostering {

	private Preferences preferences;
	private RosteringAlgorithm algorithm;
	private Set<Volunteer> volunteers;

	public Rostering(RosteringAlgorithm algorithm) {
		this.algorithm = algorithm;
		preferences = new Preferences();
	}

	/**
	 * Load preferences from excel file
	 * 
	 * @param excelFilePath - path to excel file
	 * @throws IOException
	 */
	public void loadPreferences(String excelFilePath) throws IOException {
		volunteers = preferences.loadFromExcel(excelFilePath);
	}

	public Set<String> getPreferences(DayOfWeek day, PeriodDay period) {
		return preferences.getVolunteers(new Shift(day, period));
	}

	/**
	 * Validate preferences and then generate a roster.
	 * The roster is validated and then printed if deemed valid.
	 * 
	 * @param ps - print stream when roster is printed
	 * @throws InvalidPreferencesException - notify invalid preferences
	 * @throws InvalidRosterException - notify invalid generated roster
	 */
	public void generate(PrintStream ps) throws InvalidPreferencesException, InvalidRosterException {
		preferences.validate(volunteers);
		Roster roster = algorithm.generateRoster(preferences, volunteers);
		roster.validate(volunteers);
		roster.print(ps);
	}

}
