package com.rosteringsys;

import java.io.IOException;

import com.rosteringsys.algorithm.impl.GreedyRosteringAlgorithm;
import com.rosteringsys.exception.InvalidPreferencesException;
import com.rosteringsys.exception.InvalidRosterException;

/**
 * Generate a roster according to volunteer preferences as indicated in an excel file
 * using a greedy like rostering algorithm.
 */
public class RosteringClient {;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("No preferences excel file path specified");
			return;
		}
		Rostering rostering = new Rostering(new GreedyRosteringAlgorithm());
		try {
			rostering.loadPreferences(args[0]);
		} catch (IOException e) {
			System.err.println("Could not get preferences from Excel file");
			return;
		}

		try {
			rostering.generate(System.out);
		} catch (InvalidPreferencesException | InvalidRosterException e) {
			System.err.println("The system was not able to generate a valid roster.");
			System.err.println(e.getMessage());
		}
	}
}
