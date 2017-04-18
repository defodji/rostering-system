package com.rosteringsys.exception;

/**
 * Checked exception for invalid generated roster
 */
public class InvalidRosterException extends Exception {

	private static final long serialVersionUID = -5929916126131262134L;

	public InvalidRosterException(String message) {
		super(message);
	}
}
