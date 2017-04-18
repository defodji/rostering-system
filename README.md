ROSTERING SYSTEM

1. BUILD AND EXECUTION

Below are the steps to execute the program:

- In the folder of the project (implementation/RosteringSys), type the following command (maven should be installed on the computer):
cd implementation/RosteringSys
mvn clean package 

- Then move into the target folder where a JAR executable has been created and launch the execution of the JAR file :
cd target
java -jar RosteringSys-0.0.1.jar <path-to-excel-file>
where <path-to-excel-file> is the path to the excel file containing the excel preferences. Using a relative path to the preferences excel file included in the zip, this should be:
java -jar RosteringSys-0.0.1.jar ../../../volunteer_preferences.xlsx 
The output of the execution will be the generated roster.

2. DESIGN

The rostering system use a special algorithm inspired by the greedy scheduling algorithm. It iterates through each shift to process and select the "best" volunteers in the list of preferences for the shift. The choice is based on the number of shifts the volunteer has already been assigned to (the loweer the better), the number of preferences remaining for the volunteer (the fewer the better). This ensures that all volunteers are assigned to a shift. Furthermore a volunteer that has no shift yet and whose preference for the shift is his last is automatically added to the shift.
A functional interface is defined for a general rostering algorithm (com.amdocs.rosteringsys.algorithm.RosteringAlgorithm) with one method:

public Roster generateRoster(Schedulable preferences, Set<Volunteer> volunteers);

Hence, another algorithm can easily be used with this application.

The preferences are validated before trying to generate a roster. The preferences must meet the 3 following criteria:
- the number of volunteers must be greater than TotalShifts * MinimumVolunteersPerShift / MaxiumumShiftsPerVolunteer;
- The number of preferences for every shift must be equal or greater to the minimum of volunteers per shift;
- Every volunteer must have at least one preference.

The generated roster is also validated to check if it meets the required constraints
- At least the minimum number of volunteers for every shift;
- Every volunteer is assigned to at least one shift and at most the maximum allowed shifts per volunteer;
- No volunteer has more than one shift in the same day.

Finally the greedy algorithm chosen should always generate a valid roster when the preferences are valid. And in the majority of cases, it will generate the valid roster with the fewest volunteers per shift. But there could be some extreme cases, when it is not the fewest. As it was not a requirement for this problem, no further action was taken about that flaw. 