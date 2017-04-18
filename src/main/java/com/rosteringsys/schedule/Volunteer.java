package com.rosteringsys.schedule;

/**
 * Volunteer for the roster
 *
 */
public class Volunteer implements Comparable<Volunteer> {

	private final String name;
	private int nbShifts;
	private int nbPreferredShiftsLeft; // nb of shifts in his preferences

	/**
	 * Creates new volunteer with its name
	 * @param name
	 */
	public Volunteer(String name) {
		this.name = name;
		this.nbShifts = 0;
		this.nbPreferredShiftsLeft = 0;
	}

	public Volunteer(String name, int preferredShifts) {
		this.name = name;
		this.nbShifts = 0;
		this.nbPreferredShiftsLeft = preferredShifts;
	}

	public String getName() {
		return name;
	}

	public int getNbShifts() {
		return nbShifts;
	}

	public int getNbPreferredShiftsLeft() {
		return nbPreferredShiftsLeft;
	}

	public void addPreferredShifts(int add) {
		this.nbPreferredShiftsLeft += add;
	}

	public void decreasePreferredShifts() {
		this.nbPreferredShiftsLeft--;
	}

	public void addShift() {
		if (canAcceptShift()) {
			this.nbShifts++;
			this.nbPreferredShiftsLeft--;
		} else {
			throw new IllegalArgumentException("Cannot add any shift to volunteer " + name);
		}
	}

	
	/**
	 * Indicates if the volunteer can be selected for a shift
	 * @return
	 */
	public boolean canAcceptShift() {
		return nbShifts < Schedulable.MAX_SHIFTS_PER_VOLUNTEER && nbPreferredShiftsLeft > 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Volunteer other = (Volunteer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(Volunteer v) {
		if (v == null)
			return -1;
		if (v.name.equals(this.name))
			return 0;
		int diff = this.nbShifts - v.nbShifts;
		if (diff == 0) {
			int diffPref = this.nbPreferredShiftsLeft - v.nbPreferredShiftsLeft;
			if (diffPref == 0) {
				return this.name.compareTo(v.name);
			}
			return diffPref;
		}
		return diff;
	}

	/**
	 * Indicates if the volunteer with no assigned shift has only one preference left
	 * @return
	 */
	public boolean isLastChanceForShift() {
		return this.nbShifts == 0 && this.nbPreferredShiftsLeft == 1;
	}

}
