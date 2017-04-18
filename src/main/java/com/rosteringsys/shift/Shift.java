package com.rosteringsys.shift;

import java.time.DayOfWeek;

/**
 * Shift for volunteer
 * 
 */
public class Shift implements Comparable<Shift> {
	private DayOfWeek day;
	private PeriodDay period;

	/**
	 * Creates a shift with the day and period.
	 * @param day
	 * @param period
	 */
	public Shift(DayOfWeek day, PeriodDay period) {
		this.day = day;
		this.period = period;
	}

	public DayOfWeek getDay() {
		return day;
	}

	public PeriodDay getPeriod() {
		return period;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shift other = (Shift) obj;
		if (day != other.day)
			return false;
		if (period != other.period)
			return false;
		return true;
	}

	@Override
	public int compareTo(Shift s) {
		if (s == null) {
			return -1;
		}
		int diffDay = this.day.getValue() - s.day.getValue();
		if (diffDay == 0) {
			return this.period.ordinal() - s.period.ordinal();
		}
		return diffDay;
	}
}
