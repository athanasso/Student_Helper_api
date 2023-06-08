package gr.uniwa.student_helper.util;

import gr.uniwa.student_helper.model.Course;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesICE1;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesN1;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesN2;
import gr.uniwa.student_helper.model.neededCourses.NeededCoursesPeir;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import org.json.JSONException;

public class UtilFunctions {

    private static final Logger logger = LoggerFactory.getLogger(UtilFunctions.class);

    /**
     * Calculates the filtered courses
     *
     * @param curriculum
     * @param registrationYear
     * @param partTime
     * @return  int
     * 
     */
    public static int calculateYearOfDeletion(String curriculum, int registrationYear, boolean partTime) {
        if (!partTime) {
            if (curriculum.equals("ICE1")){
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
     * Calculates the filtered courses
     *
     * @param courses
     * @param curriculum
     * @return ArrayList<Course>
     *
     */
    public static ArrayList<Course> calculateCourses(ArrayList<Course> courses, String curriculum) {

        switch (curriculum) {
            case "ICE1" -> {
                return filterCourses(courses, "ICE1");
            }
            case "N2" -> {
                return filterCourses(courses, "N2");
            }
            case "N1" -> {
                return filterCourses(courses, "N1");
            }
            case "Peir" -> {
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

    private static ArrayList<Course> filterCourses(ArrayList<Course> courses, String code) {
        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getId().matches(code+"-\\d{4}")) {
                filteredCourses.add(course);
            }
        }
        return filteredCourses;
    }

    public static Object calculateNeededCourses(ArrayList<Course> courses, String curriculum) {
        switch (curriculum) {
            case "ICE1" -> {
                return calculateICE1(courses);
            }
            case "N2" -> {
                return calculateN2(courses);
            }
            case "N1" -> {
                return calculateN1(courses);
            }
            case "Peir" -> {
                return calculatePeir(courses);
            }
            default ->
                logger.error("Error: couldn't calculate needed courses");
        }
        return null;
    }

    private static NeededCoursesICE1 calculateICE1(ArrayList<Course> courses) throws JSONException {
        NeededCoursesICE1 result = new NeededCoursesICE1();
        ArrayList<Course> mandatoryCoursesLeft = new ArrayList<>();
        ArrayList<Course> basicCoursesLeft = new ArrayList<>();
        int basicCoursesNeeded = 0;
        int choiceCoursesFromSameBasicNeeded = 0;
        ArrayList<Course> choiceCoursesFromSameBasicLeft = new ArrayList<>();
        int choiceCoursesFromOtherBasicAvailable = 7;
        ArrayList<Course> choiceCoursesFromOtherBasicLeft = new ArrayList<>();
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

        // Create a set to keep track of the courses already taken
        Set<String> takenCourses = createTakenCoursesSet(courses);

        // Add all the mandatory courses to the neededCourses list
        addCoursesToList(mandatoryCoursesLeft, mandatoryCoursesJson, takenCourses);

        //Check the basic requirements and add courses accordingly
        int totalCourseCount = courses.size();
        boolean hasSpecialCourse = containsAtLeastOneCourse(courses, "ICE1-9020");

        generalCoursesLeft = getRemainingCourses(generalCoursesJson, takenCourses);
        generalCoursesPassed = countCoursesInTakenCourses(generalCoursesJson, takenCourses);

        if (containsAllBasicCourses(basic1CoursesJson, takenCourses)) {
            choiceCoursesFromSameBasicLeft = getRemainingCourses(choice1CoursesJson, takenCourses);
            if (choiceCoursesFromSameBasicLeft.size() < 5) {
                choiceCoursesFromSameBasicNeeded = 5 - choiceCoursesFromSameBasicLeft.size();
            }
            ArrayList<JSONArray> array = new ArrayList<>();
            if (generalCoursesPassed<2){
                array.addAll(Arrays.asList(basic2CoursesJson, basic3CoursesJson, choice2CoursesJson, choice3CoursesJson, generalCoursesJson));
            } else {
                array.addAll(Arrays.asList(basic2CoursesJson, basic3CoursesJson, choice2CoursesJson, choice3CoursesJson));
            }

            choiceCoursesFromOtherBasicLeft = combineCourseArrays(array);
            choiceCoursesFromOtherBasicAvailable = countCoursesInOthersTakenCourses(choiceCoursesFromOtherBasicLeft, takenCourses) - generalCoursesPassed;
            if (choiceCoursesFromOtherBasicAvailable < 0) {
                choiceCoursesFromOtherBasicAvailable = 0;
            }
        } else if (containsAllBasicCourses(basic2CoursesJson, takenCourses)) {
            choiceCoursesFromSameBasicLeft = getRemainingCourses(choice2CoursesJson, takenCourses);
            if (choiceCoursesFromSameBasicLeft.size() < 5) {
                choiceCoursesFromSameBasicNeeded = 5 - choiceCoursesFromSameBasicLeft.size();
            }
            ArrayList<JSONArray> array = new ArrayList<>();
            if (generalCoursesPassed<2){
                array.addAll(Arrays.asList(basic1CoursesJson, basic3CoursesJson, choice1CoursesJson, choice3CoursesJson, generalCoursesJson));
            } else {
                array.addAll(Arrays.asList(basic1CoursesJson, basic3CoursesJson, choice1CoursesJson, choice3CoursesJson));
            }

            choiceCoursesFromOtherBasicLeft = combineCourseArrays(array);
            choiceCoursesFromOtherBasicAvailable = countCoursesInOthersTakenCourses(choiceCoursesFromOtherBasicLeft, takenCourses) - generalCoursesPassed;
            if (choiceCoursesFromOtherBasicAvailable < 0) {
                choiceCoursesFromOtherBasicAvailable = 0;
            }
        } else if ((containsAllBasicCourses(basic3CoursesJson, takenCourses))) {
            choiceCoursesFromSameBasicLeft = getRemainingCourses(choice3CoursesJson, takenCourses);
            if (choiceCoursesFromSameBasicLeft.size() < 5) {
                choiceCoursesFromSameBasicNeeded = 5 - choiceCoursesFromSameBasicLeft.size();
            }
            ArrayList<JSONArray> array = new ArrayList<>();
            if (generalCoursesPassed<2) {
                array.addAll(Arrays.asList(basic1CoursesJson, basic2CoursesJson, choice1CoursesJson, choice2CoursesJson, generalCoursesJson));
            } else {
                array.addAll(Arrays.asList(basic1CoursesJson, basic2CoursesJson, choice1CoursesJson, choice2CoursesJson));
            }

            choiceCoursesFromOtherBasicLeft = combineCourseArrays(array);
            choiceCoursesFromOtherBasicAvailable = countCoursesInOthersTakenCourses(choiceCoursesFromOtherBasicLeft, takenCourses) - generalCoursesPassed;
            if (choiceCoursesFromOtherBasicAvailable < 0) {
                choiceCoursesFromOtherBasicAvailable = 0;
            }
        } else {
            basicCoursesLeft = (countBasicCoursesLeftICE1(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson, takenCourses));
            basicCoursesNeeded = basicCoursesLeft.size();
            if (basicCoursesNeeded == 12) {
                basicCoursesNeeded = 4;
            }
            choiceCoursesFromSameBasicNeeded = 5;
            
            ArrayList<JSONArray> array = new ArrayList<>();
            array.addAll(Arrays.asList(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson,choice1CoursesJson, choice2CoursesJson, choice3CoursesJson,generalCoursesJson));
            choiceCoursesFromSameBasicLeft = combineCourseArrays(array);
            choiceCoursesFromOtherBasicLeft = choiceCoursesFromSameBasicLeft;
        }

        if (totalCourseCount >= 55 && mandatoryCoursesLeft.isEmpty() &&  basicCoursesNeeded == 0) {
            result.setChoiceCoursesNeeded(0);
            passedAll = true;
        } else {
            ArrayList<JSONArray> array = new ArrayList<>();
            array.addAll(Arrays.asList(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson, choice1CoursesJson, choice2CoursesJson, choice3CoursesJson, generalCoursesJson));
            result.setChoiceCoursesNeeded(16 - countCoursesInTakenCoursesFromJsonArrays(array, takenCourses));
        }

        result.setMandatoryCoursesLeft(mandatoryCoursesLeft);
        result.setMandatoryCoursesNeeded(mandatoryCoursesLeft.size());
        result.setBasicCoursesLeft(basicCoursesLeft);
        result.setBasicCoursesNeeded(basicCoursesNeeded);
        result.setChoiceCoursesFromSameBasicLeft(choiceCoursesFromSameBasicLeft);
        result.setChoiceCoursesFromSameBasicNeeded(choiceCoursesFromSameBasicNeeded);
        result.setChoiceCoursesFromOtherBasicLeft(choiceCoursesFromOtherBasicLeft);
        result.setChoiceCoursesFromOtherBasicAvailable(choiceCoursesFromOtherBasicAvailable);
        result.setGeneralCoursesPassed(generalCoursesPassed);
        result.setGeneralCoursesLeft(generalCoursesLeft);
        result.setPassedAll(passedAll);
        
        logger.debug("calculated courses");

        return result;
    }
    
    private static NeededCoursesN2 calculateN2(ArrayList<Course> courses) {
        
        NeededCoursesN2 result = new NeededCoursesN2();
        
        ArrayList<Course> mandatoryCoursesLeft = new ArrayList<>();
        int basicCoursesNeeded = 5;
        ArrayList<Course> basicCoursesLeft = new ArrayList<>();
        int choice1CoursesNeeded = 2;
        ArrayList<Course> choice1CoursesLeft = new ArrayList<>();
        int choice2CoursesNeeded = 4;
        ArrayList<Course> choice2CoursesLeft = new ArrayList<>();
        int choiceFromOthersBasicPassed = 0; //max2
        ArrayList<Course> choiceFromOthersBasicAvailable = new ArrayList<>();
        boolean passedAll = false;
        
        int coursesCount = 0;
        
        JSONObject curriculumJson = readJson("N2.json");
        JSONArray mandatoryCoursesJson = curriculumJson.getJSONArray("mandatory");
        JSONArray basic1CoursesJson = curriculumJson.getJSONArray("basic1");
        JSONArray basic2CoursesJson = curriculumJson.getJSONArray("basic2");
        JSONArray basic3CoursesJson = curriculumJson.getJSONArray("basic3");
        JSONArray choice1_1CoursesJson = curriculumJson.getJSONArray("choice1_1");
        JSONArray choice2_1CoursesJson = curriculumJson.getJSONArray("choice2_1");
        JSONArray choice3_1CoursesJson = curriculumJson.getJSONArray("choice3_1");
        JSONArray choice1_2CoursesJson = curriculumJson.getJSONArray("choice1_2");
        JSONArray choice2_2CoursesJson = curriculumJson.getJSONArray("choice2_2");
        JSONArray choice3_2CoursesJson = curriculumJson.getJSONArray("choice2_2");
        
        Set<String> takenCourses = createTakenCoursesSet(courses);
        addCoursesToList(mandatoryCoursesLeft, mandatoryCoursesJson, takenCourses);
        
        int totalCourseCount = courses.size();
        
        if (containsAtLeastOneCourse(basic1CoursesJson,takenCourses)) {
            coursesCount +=4;
            choice1CoursesLeft = getRemainingCourses(choice1_1CoursesJson, takenCourses);
            choice2CoursesLeft = getRemainingCourses(choice1_2CoursesJson, takenCourses);
            
            choice1CoursesNeeded = countPassed(choice1_1CoursesJson, takenCourses);
            choice2CoursesNeeded = countPassed(choice1_2CoursesJson, takenCourses);
            
            if (choice1CoursesNeeded>=2){
                coursesCount+=choice1CoursesNeeded;
                choice1CoursesNeeded = 0;
            }
             if (choice2CoursesNeeded>=4){
                 coursesCount+=choice2CoursesNeeded;
                 choice2CoursesNeeded = 0;
            }
             
            if (coursesCount<8){
                ArrayList<JSONArray> array = new ArrayList<>();
                array.addAll(Arrays.asList(choice2_1CoursesJson, choice3_1CoursesJson, choice2_2CoursesJson, choice3_2CoursesJson));
                choiceFromOthersBasicAvailable = combineCourseArrays(array);
                choiceFromOthersBasicPassed = countPassed(array, takenCourses);
            }
            
            if (containsAllBasicCourses(basic1CoursesJson, takenCourses)){
                coursesCount+=5;
            }
            
        } else if (containsAtLeastOneCourse(basic2CoursesJson, takenCourses)) {
            coursesCount +=4;
            choice1CoursesLeft = getRemainingCourses(choice2_1CoursesJson, takenCourses);
            choice2CoursesLeft = getRemainingCourses(choice2_2CoursesJson, takenCourses);
            
            choice1CoursesNeeded = countPassed(choice2_1CoursesJson, takenCourses);
            choice2CoursesNeeded = countPassed(choice2_2CoursesJson, takenCourses);
            
            if (choice1CoursesNeeded>=2){
                coursesCount+=choice1CoursesNeeded;
                choice1CoursesNeeded = 0;
            }
             if (choice2CoursesNeeded>=4){
                 coursesCount+=choice2CoursesNeeded;
                 choice2CoursesNeeded = 0;
            }
             
            if (coursesCount<8){
                ArrayList<JSONArray> array = new ArrayList<>();
                array.addAll(Arrays.asList(choice1_1CoursesJson, choice3_1CoursesJson, choice1_2CoursesJson, choice3_2CoursesJson));
                choiceFromOthersBasicAvailable = combineCourseArrays(array);
                choiceFromOthersBasicPassed = countPassed(array, takenCourses);
            }
            
            if (containsAllBasicCourses(basic2CoursesJson, takenCourses)){
                coursesCount+=5;
            }
            
        } else if (containsAtLeastOneCourse(basic3CoursesJson, takenCourses)) {
            coursesCount +=4;
            choice1CoursesLeft = getRemainingCourses(choice3_1CoursesJson, takenCourses);
            choice2CoursesLeft = getRemainingCourses(choice3_2CoursesJson, takenCourses);
            
            choice1CoursesNeeded = countPassed(choice3_1CoursesJson, takenCourses);
            choice2CoursesNeeded = countPassed(choice3_2CoursesJson, takenCourses);
            
            if (choice1CoursesNeeded>=2){
                coursesCount+=choice1CoursesNeeded;
                choice1CoursesNeeded = 0;
            }
             if (choice2CoursesNeeded>=4){
                 coursesCount+=choice2CoursesNeeded;
                 choice2CoursesNeeded = 0;
            }
             
            if (coursesCount<8){
                ArrayList<JSONArray> array = new ArrayList<>();
                array.addAll(Arrays.asList(choice1_1CoursesJson, choice2_1CoursesJson, choice1_2CoursesJson, choice2_2CoursesJson));
                choiceFromOthersBasicAvailable = combineCourseArrays(array);
                choiceFromOthersBasicPassed = countPassed(array, takenCourses);
            }
            
            if (containsAllBasicCourses(basic3CoursesJson, takenCourses)){
                coursesCount+=5;
            }
            
        } else {
            basicCoursesLeft = (countBasicCoursesLeftN2(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson, takenCourses));
            basicCoursesNeeded = basicCoursesLeft.size();
            if (basicCoursesNeeded == 15) {
                basicCoursesNeeded = 5;
            }
        }
        
        if (totalCourseCount >= 40 && mandatoryCoursesLeft.isEmpty() && basicCoursesNeeded == 0){
            passedAll = true;
        }
        
        result.setMandatoryCoursesLeft(mandatoryCoursesLeft);
        result.setMandatoryCoursesNeeded(mandatoryCoursesLeft.size());
        result.setBasicCoursesLeft(basicCoursesLeft);
        result.setBasicCoursesNeeded(basicCoursesNeeded);
        result.setChoice1CoursesLeft(choice1CoursesLeft);
        result.setChoice1CoursesNeeded(choice1CoursesNeeded);
        result.setChoice2CoursesLeft(choice2CoursesLeft);
        result.setChoice2CoursesNeeded(choice2CoursesNeeded);
        result.setChoiceFromOthersBasicAvailable(choiceFromOthersBasicAvailable);
        result.setChoiceFromOthersBasicPassed(choiceFromOthersBasicPassed);
        result.setPassedAll(passedAll);
        
        return result;
    }
    
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

    private static JSONObject readJson(String file) {
        try{
            // Read the JSON file containing the curriculum data
            String filePath = "data/curriculums/"+file;
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

    private static boolean addCoursesToList(List<Course> neededCourses, JSONArray coursesJson, Set<String> takenCourses) {
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

    private static boolean containsAtLeastOneCourse(ArrayList<Course> courses, String courseId) {
        for (Course course : courses) {
            if (course.getId().equals(courseId)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsAllBasicCourses(JSONArray coursesJson, Set<String> takenCourses) {
        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");

            if (!takenCourses.contains(courseId)) {
                return false;
            }
        }

        return true;
    }

    private static ArrayList<Course> countBasicCoursesLeftICE1(JSONArray basic1CoursesJson, JSONArray basic2CoursesJson, JSONArray basic3CoursesJson, Set<String> takenCourses) {
        int count1 = countCoursesNotTaken(basic1CoursesJson, takenCourses);
        int count2 = countCoursesNotTaken(basic2CoursesJson, takenCourses);
        int count3 = countCoursesNotTaken(basic3CoursesJson, takenCourses);

        if (count1 == 4 && count2 == 4 && count3 == 4)
            return allBasicCourses(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson);
         else if (count1 <= count2 && count1 <= count3) 
            return getRemainingCourses(basic1CoursesJson, takenCourses);
         else if (count2 <= count1 && count2 <= count3) 
            return getRemainingCourses(basic2CoursesJson, takenCourses);
         else if (count3 <= count1 && count3 <= count2) 
            return getRemainingCourses(basic3CoursesJson, takenCourses);
         else return new ArrayList<>();
    }

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

    private static ArrayList<Course> allBasicCourses(JSONArray basic1CoursesJson, JSONArray basic2CoursesJson, JSONArray basic3CoursesJson) {
        ArrayList<Course> allCourses = new ArrayList<>();

        addCoursesToList(allCourses, basic1CoursesJson);
        addCoursesToList(allCourses, basic2CoursesJson);
        addCoursesToList(allCourses, basic3CoursesJson);

        return allCourses;
    }

    private static void addCoursesToList(ArrayList<Course> courseList, JSONArray coursesJson) {
        for (int i = 0; i < coursesJson.length(); i++) {
            JSONObject courseJson = coursesJson.getJSONObject(i);
            String courseId = courseJson.getString("id");
            String courseName = courseJson.getString("name");

            courseList.add(new Course(courseId, courseName));
        }
    }

    private static int countCoursesInTakenCourses(ArrayList<Course> coursesList, Set<String> takenCourses) {
        int count = 0;

        for (Course course : coursesList) {
            if (takenCourses.contains(course.getId())) {
                count++;
            }
        }

        return count;
    }
    
    private static int countCoursesInOthersTakenCourses(ArrayList<Course> coursesList, Set<String> takenCourses) {
        int count = 7;

        for (Course course : coursesList) {
            if (takenCourses.contains(course.getId())) {
                count--;
            }
        }

        return count;
    }

    private static ArrayList<Course> combineCourseArrays(ArrayList<JSONArray> arrays) {
        ArrayList<Course> combinedCourses = new ArrayList<>();

        for (JSONArray array : arrays) {
            addCoursesToList(combinedCourses, array);
        }

        return combinedCourses;
    }

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

    private static Set<String> createTakenCoursesSet(ArrayList<Course> courses) {
        Set<String> takenCourses = new HashSet<>();
        for (Course course : courses) {
            takenCourses.add(course.getId());
        }
        return takenCourses;
    }
    
    private static int countCoursesInTakenCoursesFromJsonArrays(ArrayList<JSONArray> coursesJsonArrays, Set<String> takenCourses) {
        int count = 0;
        int generalCount = 0;

        for (JSONArray coursesJson : coursesJsonArrays) {
            if (coursesJson.equals("generalCoursesJson")) {
                // Count the number of general courses taken separately
                generalCount = countCoursesInTakenCourses(coursesJson, takenCourses);
            } else {
                count += countCoursesInTakenCourses(coursesJson, takenCourses);
            }
        }

        // Limit the count of general courses to a maximum of 2
        count += Math.min(generalCount, 2);

        return count;
    }

    private static int countPassed(JSONArray courses, Set<String> takenCourses) {
        int count = 0;
        for (int i = 0; i < courses.length(); i++) {
            JSONObject course = courses.getJSONObject(i);
            String courseId = course.getString("id");
            if (takenCourses.contains(courseId)) {
                count++;
            }
        }
        return count;
    }

    private static int countPassed(ArrayList<JSONArray> array, Set<String> takenCourses) {
        int count = 0;
        for (JSONArray courses : array) {
            for (int i = 0; i < courses.length(); i++) {
                JSONObject course = courses.getJSONObject(i);
                String courseId = course.getString("id");
                if (takenCourses.contains(courseId)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static ArrayList<Course> countBasicCoursesLeftN2(JSONArray basic1CoursesJson, JSONArray basic2CoursesJson, JSONArray basic3CoursesJson, Set<String> takenCourses) {
        int count1 = countCoursesNotTaken(basic1CoursesJson, takenCourses);
        int count2 = countCoursesNotTaken(basic2CoursesJson, takenCourses);
        int count3 = countCoursesNotTaken(basic3CoursesJson, takenCourses);

        if (count1 == 5 && count2 == 5 && count3 == 5)
            return allBasicCourses(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson);
         else if (count1 <= count2 && count1 <= count3) 
            return getRemainingCourses(basic1CoursesJson, takenCourses);
         else if (count2 <= count1 && count2 <= count3) 
            return getRemainingCourses(basic2CoursesJson, takenCourses);
         else if (count3 <= count1 && count3 <= count2) 
            return getRemainingCourses(basic3CoursesJson, takenCourses);
         else return new ArrayList<>();
    }

    private static boolean containsAtLeastOneCourse(JSONArray basic1CoursesJson, Set<String> takenCourses) {
        for (int i = 0; i < basic1CoursesJson.length(); i++) {
            JSONObject course = basic1CoursesJson.getJSONObject(i);
            String courseId = course.getString("id");
            if (takenCourses.contains(courseId)) {
                return true;
            }
        }
        return false;
    }
    
    public static String convertTimestampToString(String timestampString) {
        if (timestampString=="null") return "N/A";
        
        // Convert timestamp to LocalDateTime
        Instant instant = Instant.ofEpochMilli(Long.parseLong(timestampString));
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Format LocalDateTime to desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = dateTime.format(formatter);

        return formattedDate;
    }
    
    public static String calculateLastExaminationDate(String dateString) {

        long timestamp = Long.parseLong(dateString);
        // Parse the date string
        LocalDate assignmentDate = LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        int year = assignmentDate.getYear();
        Month month = assignmentDate.getMonth();

        // Determine the last examination month based on the assignment month
        Month lastExaminationMonth;
        if (month.getValue() >= Month.MARCH.getValue() && month.getValue()< Month.SEPTEMBER.getValue()) {
            lastExaminationMonth = Month.JUNE;
        } else if (month.getValue() == Month.SEPTEMBER.getValue()){
            lastExaminationMonth = Month.SEPTEMBER;
        }
        else {
            lastExaminationMonth = Month.FEBRUARY;
        }
        
        year++;

        // Construct the last examination date using the determined month and year
        LocalDate lastExaminationDate = LocalDate.of(year, lastExaminationMonth, 1);

        // Format the last examination date as a string
        String lastExaminationDateString = lastExaminationDate.format(formatter);

        return lastExaminationDateString;
    }

}
