package gr.uniwa.student_helper.util;

import gr.uniwa.student_helper.model.Course;
import gr.uniwa.student_helper.model.FileCourse;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesICE1;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesN1;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesN2;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesPeir;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.HashSet;
import org.json.JSONException;

/**
 * Utility functions for calculating and filtering courses based on curriculum and other parameters.
 */
public class UtilFunctions {

    private static final Logger logger = LoggerFactory.getLogger(UtilFunctions.class);

    /**
     * Calculates the year of deletion for a student based on their curriculum, registration year, and part-time status.
     *
     * @param curriculum The curriculum of the student.
     * @param registrationYear The registration year of the student.
     * @param partTime Indicates whether the student is part-time or not.
     * @return The year of deletion for the student. Returns -1 if the student is part-time.
     */
    public static int calculateYearOfDeletion(String curriculum, int registrationYear, boolean partTime) {
        if (!partTime) {
            if (curriculum.equals("ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)")){
                if (registrationYear<2015) return 2026;
                if (registrationYear<2021) return 2029;
                if (registrationYear>=2022) return registrationYear+8;
            }
            else {
                if (registrationYear<=2016) return 2025;
                if (registrationYear<2021) return 2027;
                if (registrationYear>=2022) return registrationYear+6;
            }
        }
        return -1;
    }

    /**
     * Calculates and returns a filtered list of courses based on the given curriculum.
     *
     * @param courses    The list of courses to filter.
     * @param curriculum The curriculum to use for filtering the courses.
     * @return The filtered list of courses based on the given curriculum.
     */
    public static ArrayList<Course> calculateCourses(ArrayList<Course> courses, String curriculum) {
        switch (curriculum) {
            case "ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)" -> {
                return filterCourses(courses, "ICE1");
            }
            case "Υπό Εφαρμογή - Νέο Πρόγραμμα Σπουδών (από 20/9/2014)" -> {
                return filterCourses(courses, "N2");
            }
            case "Νέο Πρόγραμμα Σπουδών (από 20/9/2010)" -> {
                return filterCourses(courses, "N1");
            }
            case "2017 - ΝΕΟ [24]" -> {
                ArrayList<Course> filteredCourses = new ArrayList<>();
                for (Course course : courses) {
                    if (course.getId().length() >= 6 && course.getId().length() <= 8) {
                        filteredCourses.add(course);
                    }
                }
                return filteredCourses;
            }
            default ->
                logger.error("Error: couldn't calculate courses");
        }

        return courses;
    }
    
    /**
     * Calculates the file courses based on the given curriculum.
     *
     * @param courses    The list of file courses.
     * @param curriculum The curriculum to filter the file courses.
     * @return The filtered list of file courses based on the curriculum.
     */
    public static ArrayList<FileCourse> calculateFileCourses(ArrayList<FileCourse> courses, String curriculum) {
        switch (curriculum) {
            case "ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)" -> {
                return filterFileCourses(courses, "ICE1");
            }
            case "Υπό Εφαρμογή - Νέο Πρόγραμμα Σπουδών (από 20/9/2014)" -> {
                return filterFileCourses(courses, "N2");
            }
            case "Νέο Πρόγραμμα Σπουδών (από 20/9/2010)" -> {
                return filterFileCourses(courses, "N1");
            }
            case "2017 - ΝΕΟ [24]" -> {
                ArrayList<FileCourse> filteredCourses = new ArrayList<>();
                for (FileCourse course : courses) {
                    if (course.getId().length() >= 6 && course.getId().length() <= 8) {
                        filteredCourses.add(course);
                    }
                }
                return filteredCourses;
            }
            default ->
                logger.error("Error: couldn't calculate courses");
        }

        return courses;
    }

    /**
     * Filters the given list of courses based on the provided code.
     *
     * @param courses The list of courses to filter.
     * @param code The code to match against the course IDs.
     * @return The filtered list of courses.
     */
    private static ArrayList<Course> filterCourses(ArrayList<Course> courses, String code) {
        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getId().matches(code+"-\\d{4}")) {
                filteredCourses.add(course);
            }
        }
        return filteredCourses;
    }
    
    /**
     * Filters the given list of FileCourses based on the provided code.
     *
     * @param courses The list of FileCourses to filter.
     * @param code The code to match against the FileCourse IDs.
     * @return The filtered list of FileCourses.
     */
    private static ArrayList<FileCourse> filterFileCourses(ArrayList<FileCourse> courses, String code) {
        ArrayList<FileCourse> filteredCourses = new ArrayList<>();
        for (FileCourse course : courses) {
            if (course.getId().matches(code+"-\\d{4}")) {
                filteredCourses.add(course);
            }
        }
        return filteredCourses;
    }

    /**
     * Calculates the needed courses based on the given curriculum.
     *
     * @param courses    the list of courses
     * @param curriculum the curriculum to calculate the needed courses for
     * @param flow the specialisation flow
     * @return an Object representing the calculated needed courses
     */
    public static Object calculateNeededCourses(ArrayList<Course> courses, String curriculum, String flow) {
        switch (curriculum) {
            case "ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)" -> {
                return calculateICE1(courses);
            }
            case "Υπό Εφαρμογή - Νέο Πρόγραμμα Σπουδών (από 20/9/2014)" -> {
                return calculateN2(courses, flow);
            }
            case "Νέο Πρόγραμμα Σπουδών (από 20/9/2010)" -> {
                return calculateN1(courses);
            }
            case "2017 - ΝΕΟ [24]" -> {
                return calculatePeir(courses);
            }
            default ->
                logger.error("Error: couldn't calculate needed courses");
        }
        return null;
    }

    /**
     * Represents the needed courses for ICE1 (Information and Communication Engineering) program.
     * This class contains information about the courses needed and left for the program, including
     * mandatory courses, basic courses, choice courses, and general courses.
     */
    private static NeededCoursesICE1 calculateICE1(ArrayList<Course> courses) throws JSONException {
        NeededCoursesICE1 result = new NeededCoursesICE1();
        ArrayList<Course> mandatoryCoursesLeft = new ArrayList<>();
        
        int basicCoursesNeededForSoftware = 0;
        ArrayList<Course> basicCoursesLeftForSoftware = new ArrayList<>();
        int choiceCoursesFromSameBasicNeededForSoftware = 5;
        ArrayList<Course> choiceCoursesFromSameBasicLeftForSoftware = new ArrayList<>();
        int choiceCoursesFromOtherBasicAvailableForSoftware = 7;
        ArrayList<Course> choiceCoursesFromOtherBasicLeftForSoftware = new ArrayList<>();
        int choiceCoursesFromSameBasicPassedForSoftware = 0;
        int choiceCoursesPassedForSoftware = 0;
        
        int basicCoursesNeededForHardware = 0;
        ArrayList<Course> basicCoursesLeftForHardware = new ArrayList<>();
        int choiceCoursesFromSameBasicNeededForHardware = 5;
        ArrayList<Course> choiceCoursesFromSameBasicLeftForHardware = new ArrayList<>();
        int choiceCoursesFromOtherBasicAvailableForHardware = 7;
        ArrayList<Course> choiceCoursesFromOtherBasicLeftForHardware = new ArrayList<>();
        int choiceCoursesFromSameBasicPassedForHardware = 0;
        int choiceCoursesPassedForHardware = 0;
        
        int basicCoursesNeededForNetwork = 0;
        ArrayList<Course> basicCoursesLeftForNetwork = new ArrayList<>();
        int choiceCoursesFromSameBasicNeededForNetwork = 5;
        ArrayList<Course> choiceCoursesFromSameBasicLeftForNetwork = new ArrayList<>();
        int choiceCoursesFromOtherBasicAvailableForNetwork = 7;
        ArrayList<Course> choiceCoursesFromOtherBasicLeftForNetwork = new ArrayList<>();
        int choiceCoursesFromSameBasicPassedForNetwork = 0;
        int choiceCoursesPassedForNetwork = 0;
        
        int generalCoursesPassed = 0;
        ArrayList<Course> generalCoursesLeft = new ArrayList<>();
        boolean passedAll = false;
        JSONObject curriculumJson = readJson("ICE1.json");

        // Get the course arrays from the curriculum JSON
        JSONArray mandatoryCoursesJson = curriculumJson.getJSONArray("mandatory");
        JSONArray basic1CoursesJson = curriculumJson.getJSONArray("basic1");
        JSONArray basic2CoursesJson = curriculumJson.getJSONArray("basic2");
        JSONArray basic3CoursesJson = curriculumJson.getJSONArray("basic3");
        JSONArray choice1CoursesJson = curriculumJson.getJSONArray("choice1");
        JSONArray choice2CoursesJson = curriculumJson.getJSONArray("choice2");
        JSONArray choice3CoursesJson = curriculumJson.getJSONArray("choice3");
        JSONArray generalCoursesJson = curriculumJson.getJSONArray("general");
        JSONArray specialCoursesJson = curriculumJson.getJSONArray("special");

        // Create a set to keep track of the courses already taken
        Set<String> takenCourses = createTakenCoursesSet(courses);

        // Add all the mandatory courses to the neededCourses list
        addCoursesToList(mandatoryCoursesLeft, mandatoryCoursesJson, takenCourses);

        //Check the basic requirements and add courses accordingly
        int totalCourseCount = courses.size();

        generalCoursesLeft = getRemainingCourses(generalCoursesJson, takenCourses);
        generalCoursesPassed = countCoursesInTakenCourses(generalCoursesJson, takenCourses);

        // Software Courses
        ArrayList<JSONArray> array = new ArrayList<>();

        basicCoursesNeededForSoftware = countCoursesNotTaken(basic1CoursesJson, takenCourses);
        basicCoursesLeftForSoftware = getRemainingCourses(basic1CoursesJson, takenCourses);
        choiceCoursesFromSameBasicLeftForSoftware = getRemainingCourses(choice1CoursesJson, takenCourses);
        choiceCoursesFromSameBasicNeededForSoftware -= countCoursesTaken(choice1CoursesJson, takenCourses);
        if (choiceCoursesFromSameBasicNeededForSoftware < 0) {
            choiceCoursesFromSameBasicNeededForSoftware = 0;
        }
        
        choiceCoursesFromSameBasicPassedForSoftware = countCoursesTaken(choice1CoursesJson, takenCourses);
        
        array.addAll(Arrays.asList(basic2CoursesJson, basic3CoursesJson, choice2CoursesJson, choice3CoursesJson, specialCoursesJson));

        choiceCoursesFromOtherBasicLeftForSoftware = combineCourseArrays(array);
        choiceCoursesFromOtherBasicAvailableForSoftware = Math.max(countCoursesInOthersTakenCoursesIce(choiceCoursesFromOtherBasicLeftForSoftware, takenCourses) - generalCoursesPassed, 0);
        
        choiceCoursesPassedForSoftware = choiceCoursesFromSameBasicPassedForSoftware + Math.min(Math.min(generalCoursesPassed, 2) + countCoursesTaken(choiceCoursesFromOtherBasicLeftForSoftware, takenCourses), 7) + 4 - basicCoursesNeededForSoftware; 

        // Hardware Courses
        array = new ArrayList<>();
        
        basicCoursesNeededForHardware = countCoursesNotTaken(basic2CoursesJson, takenCourses);
        basicCoursesLeftForHardware = getRemainingCourses(basic2CoursesJson, takenCourses);
        choiceCoursesFromSameBasicLeftForHardware = getRemainingCourses(choice2CoursesJson, takenCourses);
        choiceCoursesFromSameBasicNeededForHardware -= countCoursesTaken(choice2CoursesJson, takenCourses);
        if (choiceCoursesFromSameBasicNeededForHardware < 0) {
            choiceCoursesFromSameBasicNeededForHardware = 0;
        }
        
        choiceCoursesFromSameBasicPassedForHardware = countCoursesTaken(choice2CoursesJson, takenCourses);
        
        array.addAll(Arrays.asList(basic1CoursesJson, basic3CoursesJson, choice1CoursesJson, choice3CoursesJson, specialCoursesJson));

        choiceCoursesFromOtherBasicLeftForHardware = combineCourseArrays(array);
        choiceCoursesFromOtherBasicAvailableForHardware = Math.max(countCoursesInOthersTakenCoursesIce(choiceCoursesFromOtherBasicLeftForHardware, takenCourses) - generalCoursesPassed,0);
           
        choiceCoursesPassedForHardware = choiceCoursesFromSameBasicPassedForHardware + Math.min(Math.min(generalCoursesPassed, 2) + countCoursesTaken(choiceCoursesFromOtherBasicLeftForHardware, takenCourses), 7) + 4 - basicCoursesNeededForHardware; 
        
        // Network Courses
        array = new ArrayList<>();
        
        basicCoursesNeededForNetwork = countCoursesNotTaken(basic3CoursesJson, takenCourses);
        basicCoursesLeftForNetwork = getRemainingCourses(basic3CoursesJson, takenCourses);
        choiceCoursesFromSameBasicLeftForNetwork = getRemainingCourses(choice3CoursesJson, takenCourses);
        choiceCoursesFromSameBasicNeededForNetwork -= countCoursesTaken(choice3CoursesJson, takenCourses);
        if (choiceCoursesFromSameBasicNeededForNetwork < 0) {
            choiceCoursesFromSameBasicNeededForNetwork = 0;
        }
        
        choiceCoursesFromSameBasicPassedForNetwork = countCoursesTaken(choice3CoursesJson, takenCourses);
        
        array.addAll(Arrays.asList(basic1CoursesJson, basic2CoursesJson, choice1CoursesJson, choice2CoursesJson, specialCoursesJson));

        choiceCoursesFromOtherBasicLeftForNetwork = combineCourseArrays(array);
        choiceCoursesFromOtherBasicAvailableForNetwork = Math.max(countCoursesInOthersTakenCoursesIce(choiceCoursesFromOtherBasicLeftForNetwork, takenCourses) - generalCoursesPassed,0);
        
        choiceCoursesPassedForNetwork = choiceCoursesFromSameBasicPassedForNetwork + Math.min(Math.min(generalCoursesPassed, 2) + countCoursesTaken(choiceCoursesFromOtherBasicLeftForNetwork, takenCourses), 7) + 4 - basicCoursesNeededForNetwork; 
        
        if (totalCourseCount >= 55 && mandatoryCoursesLeft.isEmpty()) {
            if (basicCoursesNeededForSoftware == 0 && choiceCoursesFromSameBasicNeededForSoftware <= 0 && choiceCoursesPassedForSoftware>=16) {
                 passedAll = true;
            }
                
            else if (basicCoursesNeededForHardware == 0 && choiceCoursesFromSameBasicNeededForHardware <= 0 && choiceCoursesPassedForHardware>=16) {
                 passedAll = true;
            }
            
            else if (basicCoursesNeededForNetwork == 0 && choiceCoursesFromSameBasicNeededForNetwork <= 0 && choiceCoursesPassedForNetwork>=16) {
                 passedAll = true;
            }
        }

        result.setMandatoryCoursesLeft(mandatoryCoursesLeft);
        result.setMandatoryCoursesNeeded(mandatoryCoursesLeft.size());
        
        result.setBasicCoursesLeftForSoftware(basicCoursesLeftForSoftware);
        result.setBasicCoursesNeededForSoftware(basicCoursesNeededForSoftware);
        result.setChoiceCoursesFromSameBasicLeftForSoftware(choiceCoursesFromSameBasicLeftForSoftware);
        result.setChoiceCoursesFromSameBasicNeededForSoftware(choiceCoursesFromSameBasicNeededForSoftware);
        result.setChoiceCoursesFromOtherBasicLeftForSoftware(choiceCoursesFromOtherBasicLeftForSoftware);
        result.setChoiceCoursesFromOtherBasicAvailableForSoftware(choiceCoursesFromOtherBasicAvailableForSoftware);
        result.setChoiceCoursesFromSameBasicPassedForSoftware(choiceCoursesFromSameBasicPassedForSoftware);
        result.setChoiceCoursesPassedForSoftware(choiceCoursesPassedForSoftware);
        
        result.setBasicCoursesLeftForHardware(basicCoursesLeftForHardware);
        result.setBasicCoursesNeededForHardware(basicCoursesNeededForHardware);
        result.setChoiceCoursesFromSameBasicLeftForHardware(choiceCoursesFromSameBasicLeftForHardware);
        result.setChoiceCoursesFromSameBasicNeededForHardware(choiceCoursesFromSameBasicNeededForHardware);
        result.setChoiceCoursesFromOtherBasicLeftForHardware(choiceCoursesFromOtherBasicLeftForHardware);
        result.setChoiceCoursesFromOtherBasicAvailableForHardware(choiceCoursesFromOtherBasicAvailableForHardware);
        result.setChoiceCoursesFromSameBasicPassedForHardware(choiceCoursesFromSameBasicPassedForHardware);
        result.setChoiceCoursesPassedForHardware(choiceCoursesPassedForHardware);
        
        result.setBasicCoursesLeftForNetwork(basicCoursesLeftForNetwork);
        result.setBasicCoursesNeededForNetwork(basicCoursesNeededForNetwork);
        result.setChoiceCoursesFromSameBasicLeftForNetwork(choiceCoursesFromSameBasicLeftForNetwork);
        result.setChoiceCoursesFromSameBasicNeededForNetwork(choiceCoursesFromSameBasicNeededForNetwork);
        result.setChoiceCoursesFromOtherBasicLeftForNetwork(choiceCoursesFromOtherBasicLeftForNetwork);
        result.setChoiceCoursesFromOtherBasicAvailableForNetwork(choiceCoursesFromOtherBasicAvailableForNetwork);
        result.setChoiceCoursesFromSameBasicPassedForNetwork(choiceCoursesFromSameBasicPassedForNetwork);
        result.setChoiceCoursesPassedForNetwork(choiceCoursesPassedForNetwork);
        
        result.setGeneralCoursesPassed(generalCoursesPassed);
        result.setGeneralCoursesLeft(generalCoursesLeft);
        result.setPassedAll(passedAll);
        
        logger.debug("calculated courses");

        return result;
    }
    
    /**
     * Represents the needed courses for N2 curriculum.
     */
    private static NeededCoursesN2 calculateN2(ArrayList<Course> courses, String flow) {
        
        NeededCoursesN2 result = new NeededCoursesN2();
        
        ArrayList<Course> mandatoryCoursesLeft = new ArrayList<>();
        
        int basicCoursesNeeded = 0;
        ArrayList<Course> basicCoursesLeft = new ArrayList<>();
        int choiceCoursesFromSameBasicNeeded = 6;
        ArrayList<Course> choiceCoursesFromSameBasicLeft = new ArrayList<>();
        int choiceCoursesFromOtherBasicAvailable = 2;
        ArrayList<Course> choiceCoursesFromOtherBasicLeft = new ArrayList<>();
        int choiceCoursesFromSameBasicPassed = 0;
        int choiceCoursesPassed = 0;
        
        boolean passedAll = false;
        JSONObject curriculumJson = readJson("N2.json");

        // Get the course arrays from the curriculum JSON
        JSONArray mandatoryCoursesJson = curriculumJson.getJSONArray("mandatory");
        JSONArray basic1CoursesJson = curriculumJson.getJSONArray("basic1");
        JSONArray basic2CoursesJson = curriculumJson.getJSONArray("basic2");
        JSONArray basic3CoursesJson = curriculumJson.getJSONArray("basic3");
        JSONArray choice1CoursesJson = curriculumJson.getJSONArray("choice1");
        JSONArray choice2CoursesJson = curriculumJson.getJSONArray("choice2");
        JSONArray choice3CoursesJson = curriculumJson.getJSONArray("choice3");

        JSONArray specialCoursesJson = curriculumJson.getJSONArray("special");

        // Create a set to keep track of the courses already taken
        Set<String> takenCourses = createTakenCoursesSet(courses);

        // Add all the mandatory courses to the neededCourses list
        addCoursesToList(mandatoryCoursesLeft, mandatoryCoursesJson, takenCourses);

        //Check the basic requirements and add courses accordingly
        int totalCourseCount = courses.size();

        // Software Courses
        ArrayList<JSONArray> array = new ArrayList<>();
        
        if (flow.equals("Μηχανικών Λογισμικού")){
            basicCoursesNeeded = countCoursesNotTaken(basic1CoursesJson, takenCourses);
            basicCoursesLeft = getRemainingCourses(basic1CoursesJson, takenCourses);
            choiceCoursesFromSameBasicLeft = getRemainingCourses(choice1CoursesJson, takenCourses);
            choiceCoursesFromSameBasicNeeded -= countUniqueCoursesTaken(choice1CoursesJson, takenCourses);
            if (choiceCoursesFromSameBasicNeeded < 0) {
                choiceCoursesFromSameBasicNeeded = 0;
            }

            choiceCoursesFromSameBasicPassed = countUniqueCoursesTaken(choice1CoursesJson, takenCourses);

            array.addAll(Arrays.asList( choice2CoursesJson, choice3CoursesJson, specialCoursesJson));

            choiceCoursesFromOtherBasicLeft = combineCourseArrays(array);
            choiceCoursesFromOtherBasicLeft = removeCourses(choiceCoursesFromOtherBasicLeft, basic1CoursesJson);
            choiceCoursesFromOtherBasicLeft = removeCourses(choiceCoursesFromOtherBasicLeft, choice1CoursesJson);
            choiceCoursesFromOtherBasicAvailable = Math.max(countCoursesInOthersTakenCoursesN2(choiceCoursesFromOtherBasicLeft, takenCourses), 0);

            choiceCoursesPassed = choiceCoursesFromSameBasicPassed + countUniqueCoursesTaken(choiceCoursesFromOtherBasicLeft, takenCourses) + 5 - basicCoursesNeeded; 

        }
        
        else if (flow.equals("Μηχανικών Η/Υ")){
            // Hardware Courses
            array = new ArrayList<>();

            basicCoursesNeeded = countCoursesNotTaken(basic2CoursesJson, takenCourses);
            basicCoursesLeft = getRemainingCourses(basic2CoursesJson, takenCourses);
            choiceCoursesFromSameBasicLeft = getRemainingCourses(choice2CoursesJson, takenCourses);
            choiceCoursesFromSameBasicNeeded -= countUniqueCoursesTaken(choice2CoursesJson, takenCourses);
            if (choiceCoursesFromSameBasicNeeded < 0) {
                choiceCoursesFromSameBasicNeeded = 0;
            }

            choiceCoursesFromSameBasicPassed = countUniqueCoursesTaken(choice2CoursesJson, takenCourses);

            array.addAll(Arrays.asList( choice1CoursesJson, choice3CoursesJson, specialCoursesJson));

            choiceCoursesFromOtherBasicLeft = combineCourseArrays(array);
            choiceCoursesFromOtherBasicLeft = removeCourses(choiceCoursesFromOtherBasicLeft, basic2CoursesJson);
            choiceCoursesFromOtherBasicLeft = removeCourses(choiceCoursesFromOtherBasicLeft, choice2CoursesJson);            
            choiceCoursesFromOtherBasicAvailable = Math.max(countCoursesInOthersTakenCoursesN2(choiceCoursesFromOtherBasicLeft, takenCourses),0);

            choiceCoursesPassed = choiceCoursesFromSameBasicPassed + countUniqueCoursesTaken(choiceCoursesFromOtherBasicLeft, takenCourses) + 5 - basicCoursesNeeded; 
        
        }
        
        else if (flow.equals("Μηχανικών Δικτύων")){
            // Network Courses
            array = new ArrayList<>();

            basicCoursesNeeded = countCoursesNotTaken(basic3CoursesJson, takenCourses);
            basicCoursesLeft = getRemainingCourses(basic3CoursesJson, takenCourses);
            choiceCoursesFromSameBasicLeft = getRemainingCourses(choice3CoursesJson, takenCourses);
            choiceCoursesFromSameBasicNeeded -= countUniqueCoursesTaken(choice3CoursesJson, takenCourses);
            if (choiceCoursesFromSameBasicNeeded < 0) {
                choiceCoursesFromSameBasicNeeded = 0;
            }

            choiceCoursesFromSameBasicPassed = countUniqueCoursesTaken(choice3CoursesJson, takenCourses);

            array.addAll(Arrays.asList( choice1CoursesJson, choice2CoursesJson, specialCoursesJson));

            choiceCoursesFromOtherBasicLeft = combineCourseArrays(array);
            choiceCoursesFromOtherBasicLeft = removeCourses(choiceCoursesFromOtherBasicLeft, basic3CoursesJson);
            choiceCoursesFromOtherBasicLeft = removeCourses(choiceCoursesFromOtherBasicLeft, choice3CoursesJson);
            choiceCoursesFromOtherBasicAvailable = Math.max(countCoursesInOthersTakenCoursesN2(choiceCoursesFromOtherBasicLeft, takenCourses),0);

            choiceCoursesPassed = choiceCoursesFromSameBasicPassed + countUniqueCoursesTaken(choiceCoursesFromOtherBasicLeft, takenCourses) + 5 - basicCoursesNeeded;

        }
        
        if (totalCourseCount >= 40 && mandatoryCoursesLeft.isEmpty()) {
            if (basicCoursesNeeded == 0 && choiceCoursesFromSameBasicNeeded <= 0 && choiceCoursesPassed>=13) {
                 passedAll = true;
            }
        }

        result.setMandatoryCoursesLeft(mandatoryCoursesLeft);
        result.setMandatoryCoursesNeeded(mandatoryCoursesLeft.size());
        
        result.setBasicCoursesLeft(basicCoursesLeft);
        result.setBasicCoursesNeeded(basicCoursesNeeded);
        result.setChoiceCoursesFromSameBasicLeft(choiceCoursesFromSameBasicLeft);
        result.setChoiceCoursesFromSameBasicNeeded(choiceCoursesFromSameBasicNeeded);
        result.setChoiceCoursesFromOtherBasicLeft(choiceCoursesFromOtherBasicLeft);
        result.setChoiceCoursesFromOtherBasicAvailable(choiceCoursesFromOtherBasicAvailable);
        result.setChoiceCoursesFromSameBasicPassed(choiceCoursesFromSameBasicPassed);
        result.setChoiceCoursesPassed(choiceCoursesPassed);
       
        result.setPassedAll(passedAll);
        
        return result;
    }
    
    /**
     * Represents the needed courses for N1 curriculum.
     * This class contains information about the mandatory courses, choice courses 1, and choice courses 2.
     * It also provides methods to get and set the courses left and the number of courses needed.
     */
    private static NeededCoursesN1 calculateN1(ArrayList<Course> courses) {
        
        NeededCoursesN1 result = new NeededCoursesN1();
        ArrayList<Course> mandatoryCoursesLeft = new ArrayList<>();
        int choiceCourses1Needed = 2;
        ArrayList<Course> choiceCourses1Left = new ArrayList<>();
        int choiceCourses2Needed = 6;
        ArrayList<Course> choiceCourses2Left = new ArrayList<>();
        boolean passedAll = false;

        JSONObject curriculumJson = readJson("N1.json");

        JSONArray mandatoryCoursesJson = curriculumJson.getJSONArray("mandatory");
        JSONArray choice1CoursesJson = curriculumJson.getJSONArray("choice1");
        JSONArray choice2CoursesJson = curriculumJson.getJSONArray("choice2");

        Set<String> takenCourses = createTakenCoursesSet(courses);

        addCoursesToList(mandatoryCoursesLeft, mandatoryCoursesJson, takenCourses);
        addCoursesToList(choiceCourses1Left, choice1CoursesJson, takenCourses);
        addCoursesToList(choiceCourses2Left, choice2CoursesJson, takenCourses);
        
        choiceCourses1Needed -= 4-choiceCourses1Left.size();
        choiceCourses2Needed -= 12-choiceCourses2Left.size();
        
        if (choiceCourses1Needed<=0 && choiceCourses2Needed<=0){
            if (mandatoryCoursesLeft.isEmpty()) {
                passedAll = true;
            }
            
            choiceCourses1Needed=0;
            choiceCourses2Needed=0;
        }
              
        result.setMandatoryCoursesLeft(mandatoryCoursesLeft);
        result.setMandatoryCoursesNeeded(mandatoryCoursesLeft.size());
        result.setChoiceCourses1Left(choiceCourses1Left);
        result.setChoiceCourses1Needed(choiceCourses1Needed);
        result.setChoiceCourses2Left(choiceCourses2Left);
        result.setChoiceCourses2Needed(choiceCourses2Needed);
        result.setMandatoryCoursesNeeded(choiceCourses2Needed);
        result.setPassedAll(passedAll);
        return result;
    }

    /**
     * Represents the needed courses for a student's PEIR calculation.
     */
    private static NeededCoursesPeir calculatePeir(ArrayList<Course> courses) {
        NeededCoursesPeir result = new NeededCoursesPeir();
        
        ArrayList<Course> mandatoryCoursesLeft = new ArrayList<>();
        int choiceCourses1Needed = 1;
        ArrayList<Course> choiceCourses1Left = new ArrayList<>();
        int choiceCourses2Needed = 1;
        ArrayList<Course> choiceCourses2Left = new ArrayList<>();
        int choiceCourses3Needed = 2;
        ArrayList<Course> choiceCourses3Left = new ArrayList<>();
        boolean passedAll = false;

        JSONObject curriculumJson = readJson("peir.json");

        JSONArray mandatoryCoursesJson = curriculumJson.getJSONArray("mandatory");
        JSONArray choice1CoursesJson = curriculumJson.getJSONArray("choice1");
        JSONArray choice2CoursesJson = curriculumJson.getJSONArray("choice2");
        JSONArray choice3CoursesJson = curriculumJson.getJSONArray("choice3");

        Set<String> takenCourses = createTakenCoursesSet(courses);

        addCoursesToList(mandatoryCoursesLeft, mandatoryCoursesJson, takenCourses);
        addCoursesToList(choiceCourses1Left, choice1CoursesJson, takenCourses);
        addCoursesToList(choiceCourses2Left, choice2CoursesJson, takenCourses);
        addCoursesToList(choiceCourses3Left, choice3CoursesJson, takenCourses);
        
        choiceCourses1Needed -= 4-choiceCourses1Left.size();
        choiceCourses2Needed -= 5-choiceCourses2Left.size();
        choiceCourses3Needed -= 5-choiceCourses3Left.size();
        
        if (choiceCourses1Needed<=0 && choiceCourses2Needed<=0 && choiceCourses3Needed<=0){
            if (mandatoryCoursesLeft.isEmpty()) {
                passedAll = true;
            }
            
            choiceCourses1Needed=0;
            choiceCourses2Needed=0;
            choiceCourses3Needed=0;
        }
              
        result.setMandatoryCoursesLeft(mandatoryCoursesLeft);
        result.setMandatoryCoursesNeeded(mandatoryCoursesLeft.size());
        result.setChoiceCourses1Left(choiceCourses1Left);
        result.setChoiceCourses1Needed(choiceCourses1Needed);
        result.setChoiceCourses2Left(choiceCourses2Left);
        result.setMandatoryCoursesNeeded(choiceCourses2Needed);
        result.setPassedAll(passedAll);
        return result;
    }

    /**
     * Reads a JSON file and returns its content as a JSONObject.
     *
     * @param file the name of the JSON file to read
     * @return the content of the JSON file as a JSONObject, or null if an error occurs
     */
    private static JSONObject readJson(String file) {
        try {
            // Read the JSON file containing the curriculum data
            String filePath = "data/curriculums/" + file;
            InputStream inputStream = UtilFunctions.class.getClassLoader().getResourceAsStream(filePath);
            String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            // Parse the JSON string
            JSONObject curriculumJson = new JSONObject(jsonContent);
            logger.debug("file read");
            return curriculumJson;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Adds courses from a JSON array to a list of needed courses, if they are not already taken.
     *
     * @param neededCourses The list of needed courses to add the courses to.
     * @param coursesJson The JSON array containing the courses to be added.
     * @param takenCourses The set of courses that have already been taken.
     * @return true if any courses were added to the list, false otherwise.
     */
    private static boolean addCoursesToList(ArrayList<Course> neededCourses, JSONArray coursesJson, Set<String> takenCourses) {
        boolean addedCourses = false;

        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");

            if (!takenCourses.contains(courseId)) {
                String courseName = courseJson.getString("name");
                neededCourses.add(new Course(courseId, courseName));
                takenCourses.add(courseId);
                addedCourses = true;
            }
        }

        return addedCourses;
    }
    
    /**
     * Counts the number of courses taken by a student.
     *
     * @param coursesJson   the JSON array containing the courses information
     * @param takenCourses  the set of course IDs representing the courses taken by the student
     * @return the number of courses taken by the student
     */
    private static int countCoursesTaken(JSONArray coursesJson, Set<String> takenCourses) {
        int count = 0;

        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");

            if (takenCourses.contains(courseId)) {
                count++;
            }
        }

        return count;
    }
    
    /**
     * Counts the number of courses taken from a given list of courses.
     *
     * @param courseList   the list of courses to check
     * @param takenCourses the set of courses taken
     * @return the number of courses taken from the given list
     */
   private static int countCoursesTaken(ArrayList<Course> courseList, Set<String> takenCourses) {
        int count = 0;

        for (Course course : courseList) {
            if (takenCourses.contains(course.getId())) {
                count++;
            }
        }

        return count;
    }
   
    /**
     * Counts the number of unique courses taken by a student.
     *
     * @param coursesJson   the JSON array containing the courses information
     * @param takenCourses  the set of course IDs representing the courses taken by the student
     * @return the number of unique courses taken by the student
     */
    private static int countUniqueCoursesTaken(JSONArray coursesJson, Set<String> takenCourses) {
        int count = 0;
        Set<String> countedCourses = new HashSet<>();

        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");

            if (takenCourses.contains(courseId) && !countedCourses.contains(courseId)) {
                count++;
                countedCourses.add(courseId);
            }
        }

        return count;
    }
   
   /**
     * Counts the number of unique courses taken from a given list of courses.
     *
     * @param courseList   the list of courses to check
     * @param takenCourses the set of courses taken
     * @return the number of unique courses taken from the given list
     */
    private static int countUniqueCoursesTaken(ArrayList<Course> courseList, Set<String> takenCourses) {
        int count = 0;
        Set<String> countedCourses = new HashSet<>();

        for (Course course : courseList) {
            if (takenCourses.contains(course.getId()) && !countedCourses.contains(course.getId())) {
                count++;
                countedCourses.add(course.getId());
            }
        }

        return count;
    }

    /**
     * Counts the number of courses that have not been taken.
     *
     * @param coursesJson   the JSONArray containing the courses information
     * @param takenCourses  the Set of course IDs that have been taken
     * @return the count of courses that have not been taken
     */
    private static int countCoursesNotTaken(JSONArray coursesJson, Set<String> takenCourses) {
        int count = 0;

        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");

            if (!takenCourses.contains(courseId)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns a list of remaining courses based on the given courses JSON array and the set of taken courses.
     *
     * @param coursesJson   The JSON array containing the courses information.
     * @param takenCourses  The set of taken courses.
     * @return              The list of remaining courses.
     */
    private static ArrayList<Course> getRemainingCourses(JSONArray coursesJson, Set<String> takenCourses) {
        ArrayList<Course> remainingCourses = new ArrayList<>();

        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");
            String courseName = courseJson.getString("name");

            if (!takenCourses.contains(courseId)) {
                remainingCourses.add(new Course(courseId, courseName));
            }
        }

        return remainingCourses;
    }

    /**
     * Adds courses from a JSONArray to an ArrayList of Course objects.
     *
     * @param courseList  the ArrayList to which the courses will be added
     * @param coursesJson the JSONArray containing the course data
     */
    private static void addCoursesToList(ArrayList<Course> courseList, JSONArray coursesJson) {
        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");
            String courseName = courseJson.getString("name");

            courseList.add(new Course(courseId, courseName));
        }
    }

    /**
     * Counts the number of courses in the given coursesList that are not present in the takenCourses set.
     *
     * @param coursesList  the list of courses to check
     * @param takenCourses the set of courses that have been taken
     * @return the count of courses in coursesList that are not present in takenCourses
     */
    private static int countCoursesInOthersTakenCoursesIce(ArrayList<Course> coursesList, Set<String> takenCourses) {
        int count = 7;

        for (Course course : coursesList) {
            if (takenCourses.contains(course.getId())) {
                count--;
            }
        }

        return count;
    }
    
        /**
     * Counts the number of courses in the given coursesList that are not present in the takenCourses set.
     *
     * @param coursesList  the list of courses to check
     * @param takenCourses the set of courses that have been taken
     * @return the count of courses in coursesList that are not present in takenCourses
     */
    private static int countCoursesInOthersTakenCoursesN2(ArrayList<Course> coursesList, Set<String> takenCourses) {
        int count = 2;

        for (Course course : coursesList) {
            if (takenCourses.contains(course.getId())) {
                count--;
            }
        }

        return count;
    }

    /**
     * Combines multiple arrays of courses into a single ArrayList of courses.
     *
     * @param arrays The ArrayList of JSONArrays containing courses to be combined.
     * @return The combined ArrayList of courses.
     */
    private static ArrayList<Course> combineCourseArrays(ArrayList<JSONArray> arrays) {
        ArrayList<Course> combinedCourses = new ArrayList<>();

        for (JSONArray array : arrays) {
            addCoursesToList(combinedCourses, array);
        }

        return combinedCourses;
    }

    /**
     * Counts the number of courses in the given JSONArray that are present in the set of taken courses.
     *
     * @param coursesJson   the JSONArray containing the courses
     * @param takenCourses  the set of taken courses
     * @return the count of courses in the JSONArray that are present in the set of taken courses
     */
    private static int countCoursesInTakenCourses(JSONArray coursesJson, Set<String> takenCourses) {
        int count = 0;

        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");

            if (takenCourses.contains(courseId)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Creates a set of taken courses based on the provided list of courses.
     *
     * @param courses the list of courses
     * @return a set of taken courses
     */
    private static Set<String> createTakenCoursesSet(ArrayList<Course> courses) {
        Set<String> takenCourses = new HashSet<>();
        for (Course course : courses) {
            takenCourses.add(course.getId());
        }
        return takenCourses;
    }
    
    /**
     * Converts a timestamp string to a formatted date string.
     *
     * @param timestampString the timestamp string to convert
     * @return the formatted date string
     */
    public static String convertTimestampToString(String timestampString) {
        if (timestampString.equals("null")) return "N/A";
        
        // Convert timestamp to LocalDateTime
        Instant instant = Instant.ofEpochMilli(Long.parseLong(timestampString));
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Format LocalDateTime to desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = dateTime.format(formatter);

        return formattedDate;
    }


    /**
     * Calculates the curriculum identifier based on the given curriculum.
     *
     * @param curriculum the curriculum for which to calculate the identifier
     * @return the curriculum identifier
     */
    public static String calculateCurriculumIdentifier(String curriculum) {
        switch (curriculum) {
            case "ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)" -> {
                return "ICE1";
            }
            case "Υπό Εφαρμογή - Νέο Πρόγραμμα Σπουδών (από 20/9/2014)" -> {
                return "N2";
            }
            case "Νέο Πρόγραμμα Σπουδών (από 20/9/2010)" -> {
                return "N1";
            }
            case "2017 - ΝΕΟ [24]" -> {
                return "Peir";
            }
            default -> {
                logger.error("Error: couldn't set CurriculumCode");
                return "";
            }
        }
    }

    /**
     * Removes courses from the given list based on the IDs provided in the JSON array.
     *
     * @param courses    the list of courses to remove from
     * @param courseJson the JSON array containing the course IDs to remove
     * @return the updated list of courses after removing the specified courses
     */
    private static ArrayList<Course> removeCourses(ArrayList<Course> courses, JSONArray courseJson) {
         // Iterate over the courses in the JSON array
         for (int i = 0; i < courseJson.length(); i++) {
             JSONObject jsonObject = courseJson.getJSONObject(i);
             String id = jsonObject.getString("id");

             // Iterate over the list of courses
             for (int j = 0; j < courses.size(); j++) {
                 Course course = courses.get(j);

                 // If the course ID matches, remove it from the list
                 if (course.getId().equals(id)) {
                     courses.remove(j);
                     // Decrement the index to account for the removed element
                     j--;
                 }
             }
         }
         
         return courses;
     }
}
