package com.rosteringsys.schedule.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rosteringsys.exception.InvalidPreferencesException;
import com.rosteringsys.schedule.Schedulable;
import com.rosteringsys.schedule.Volunteer;
import com.rosteringsys.shift.PeriodDay;
import com.rosteringsys.shift.Shift;

/**
 * Preferences of all volunteers
 *
 */
public class Preferences implements Schedulable {

	public static final String BOTH = "Either";

	private Map<Shift, Set<String>> availabilities = new TreeMap<>();

	public Preferences() {
		for (DayOfWeek aDay : WORK_DAYS) {
			for (PeriodDay aPeriod : PeriodDay.values()) {
				availabilities.put(new Shift(aDay, aPeriod), new HashSet<>());
			}
		}
	}

	/**
	 * Load preferences from an excel file
	 * @param excelFilePath - path to excel file as resource
	 * @return set of all the volunteers
	 * @throws IOException
	 */
	public Set<Volunteer> loadFromExcel(String excelFilePath) throws IOException {
		Set<Volunteer> volunteers = new HashSet<>();
		try (InputStream inputStream = Files.newInputStream(
				Paths.get(excelFilePath))) {

			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			List<DayOfWeek> days = new ArrayList<>();
			if (iterator.hasNext()) {
				// first row - days
				Row headingRow = iterator.next();
				Iterator<Cell> cellIterator = headingRow.cellIterator();
				cellIterator.next(); // skip first cell
				while (cellIterator.hasNext()) {
					days.add(DayOfWeek.valueOf(cellIterator.next().getStringCellValue().toUpperCase().trim()));
				}
			}
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Volunteer volunteer = null;
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				String name = null;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (cell.getColumnIndex() == 0) { // volunteer name column
						name = cell.getStringCellValue();
						if (name == null || name.length() == 0) {
							break;
						}
						volunteer = new Volunteer(name);
					} else {
						String periodAvail = cell.getStringCellValue();
						if (periodAvail != null && periodAvail.length() > 0) {
							DayOfWeek day = days.get(cell.getColumnIndex() - 1); // get
																					// day
																					// in
																					// column
							if (BOTH.equals(periodAvail)) {
								addVolunteer(new Shift(day, PeriodDay.MORNING), name);
								addVolunteer(new Shift(day, PeriodDay.AFTERNOON), name);
								volunteer.addPreferredShifts(2);
							} else {
								addVolunteer(new Shift(day, PeriodDay.fromString(periodAvail)), name);
								volunteer.addPreferredShifts(1);
							}
						}
					}
				}
				if (volunteer != null) {
					volunteers.add(volunteer);
				}
			}
			workbook.close();
		}
		return volunteers;
	}

	@Override
	public void addVolunteer(Shift shift, String name) {
		availabilities.get(shift).add(name);
	}

	@Override
	public Set<String> getVolunteers(Shift shift) {
		return availabilities.get(shift);
	}

	/**
	 * Validate that the preferences are suitable to generate a valid roster.
	 * @param volunteers - set of all volunteers
	 * @return true - when the preferences are valid
	 * @throws InvalidPreferencesException - exception of invalid preferences
	 */
	public boolean validate(Set<Volunteer> volunteers) throws InvalidPreferencesException {
		int totalShifts = WORK_DAYS.size() * PeriodDay.values().length;
		int minNbVolunteers = (int) Math
				.ceil((double) (totalShifts * MIN_VOLUNTEERS_PER_SHIFT) / MAX_SHIFTS_PER_VOLUNTEER);
		if (volunteers.size() < minNbVolunteers) {
			throw new InvalidPreferencesException("Not enough volunteers - minimum needed : " + minNbVolunteers
					+ "; available ;" + volunteers.size());
		}
		for (Set<String> shiftPrefs : availabilities.values()) {
			if (shiftPrefs.size() < MIN_VOLUNTEERS_PER_SHIFT) {
				throw new InvalidPreferencesException("Each shift should have at least 2 possible volunteers");
			}
		}
		boolean invalidPreferred = volunteers.stream().filter(v -> v.getNbPreferredShiftsLeft() == 0)
				.collect(Collectors.toSet()).size() > 0;
		if (invalidPreferred) {
			throw new InvalidPreferencesException("Each volunteer should have at least a preference");
		}
		return true;
	}

}
