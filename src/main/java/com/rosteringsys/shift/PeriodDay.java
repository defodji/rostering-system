package com.rosteringsys.shift;

/**
 * Different periods for shifts
 *
 */
public enum PeriodDay {

	MORNING("Morning"), AFTERNOON("Afternoon");

	private String periodStr;

	PeriodDay(String periodStr) {
		this.periodStr = periodStr;
	}

	public static PeriodDay fromString(String period) {
		if ("morning".equalsIgnoreCase(period)) {
			return MORNING;
		} else if ("afternoon".equalsIgnoreCase(period)) {
			return AFTERNOON;
		} else {
			throw new IllegalArgumentException("Invalid period");
		}
	}

	public static PeriodDay otherPeriod(PeriodDay period) {
		if (period.equals(MORNING)) {
			return AFTERNOON;
		}
		return MORNING;
	}

	public String getValue() {
		return this.periodStr;
	}

}
