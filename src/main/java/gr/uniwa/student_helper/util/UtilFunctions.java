package gr.uniwa.student_helper.util;

import gr.uniwa.student_helper.model.Course;
import gr.uniwa.student_helper.model.NeededCourses;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilFunctions {

    private static final Logger logger = LoggerFactory.getLogger(UtilFunctions.class);

    /**
     * Calculates the filtered courses
     *
     * @param curriculum
     * @param registrationYear
     * @return  int
     * 
     */
     public static int calculateYearOfDeletion (String curriculum, int registrationYear)  {
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
        ArrayList<Course> filteredCourses = new ArrayList<>();
        if (curriculum.equals("ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)")) {
            for (Course course : courses) {
                if (course.getId().contains("ICE1")) {
                    filteredCourses.add(course);
                }
            }
            return filteredCourses;
        }
        return courses;
    }

    public static NeededCourses calculateNeededCourses(ArrayList<Course> courses, String curriculum) {
        NeededCourses result = new NeededCourses();
        ArrayList<Course> mandatoryCoursesLeft = new ArrayList<>();
        ArrayList<Course> basicCoursesLeft = new ArrayList<>();
        int basicCoursesNeeded = 0;
        int choiceCoursesFromSameBasicNeeded = 0;
        ArrayList<Course> choiceCoursesFromSameBasicLeft = new ArrayList<>();
        int choiceCoursesFromOtherBasicAvailable = 0;
        ArrayList<Course> choiceCoursesFromOtherBasicLeft = new ArrayList<>();
        int generalCoursesPassed = 0;
        ArrayList<Course> generalCoursesLeft = new ArrayList<>();

        if (curriculum.equals("ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)")) {
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

            if (containsAllBasicCourses(basic1CoursesJson, takenCourses)) {
                choiceCoursesFromSameBasicLeft = getRemainingCourses(choice1CoursesJson, takenCourses);
                if (choiceCoursesFromSameBasicLeft.size() < 5) {
                    choiceCoursesFromSameBasicNeeded = 5 - choiceCoursesFromSameBasicLeft.size();
                }
                choiceCoursesFromOtherBasicLeft = combineCourseArrays(basic2CoursesJson, basic3CoursesJson, choice2CoursesJson, choice3CoursesJson, generalCoursesJson);
                choiceCoursesFromOtherBasicAvailable = 8-countCoursesInTakenCourses(choiceCoursesFromOtherBasicLeft, takenCourses);//check if correct?
                if (choiceCoursesFromOtherBasicAvailable == 0) {
                    choiceCoursesFromOtherBasicAvailable = 7;
                }
            } else if (containsAllBasicCourses(basic2CoursesJson, takenCourses)) {
                choiceCoursesFromSameBasicLeft = getRemainingCourses(choice2CoursesJson, takenCourses);
                if (choiceCoursesFromSameBasicLeft.size() < 5) {
                    choiceCoursesFromSameBasicNeeded = 5 - choiceCoursesFromSameBasicLeft.size();
                }
                choiceCoursesFromOtherBasicLeft = combineCourseArrays(basic1CoursesJson, basic3CoursesJson, choice1CoursesJson, choice3CoursesJson, generalCoursesJson);
                choiceCoursesFromOtherBasicAvailable = 8-countCoursesInTakenCourses(choiceCoursesFromOtherBasicLeft, takenCourses);//check if correct?
                if (choiceCoursesFromOtherBasicAvailable == 0) {
                    choiceCoursesFromOtherBasicAvailable = 7;
                }
            } else if ((containsAllBasicCourses(basic3CoursesJson, takenCourses))) {
                choiceCoursesFromSameBasicLeft = getRemainingCourses(choice3CoursesJson, takenCourses);
                if (choiceCoursesFromSameBasicLeft.size() < 5) {
                    choiceCoursesFromSameBasicNeeded = 5 - choiceCoursesFromSameBasicLeft.size();
                }
                choiceCoursesFromOtherBasicLeft = combineCourseArrays(basic1CoursesJson, basic2CoursesJson, choice1CoursesJson, choice2CoursesJson, generalCoursesJson);
                choiceCoursesFromOtherBasicAvailable = 8-countCoursesInTakenCourses(choiceCoursesFromOtherBasicLeft, takenCourses);//check if correct?
                if (choiceCoursesFromOtherBasicAvailable == 0) {
                    choiceCoursesFromOtherBasicAvailable = 7;
                }
            } else {
                basicCoursesLeft = (countBasicCoursesLeft(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson, takenCourses));
                basicCoursesNeeded = basicCoursesLeft.size();
                if (basicCoursesNeeded == 12) {
                    basicCoursesNeeded = 4;
                }
                choiceCoursesFromSameBasicNeeded = 5;
            }

            if (totalCourseCount == 55) {
                result.setChoiceCoursesNeeded(0);
            } else {
                //TODO
                result.setChoiceCoursesNeeded(55 - takenCourses.size() + basicCoursesNeeded + choiceCoursesFromSameBasicNeeded);
            }

            generalCoursesLeft = getRemainingCourses(generalCoursesJson, takenCourses);
            generalCoursesPassed = countCoursesInTakenCourses(generalCoursesJson, takenCourses);
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
            return curriculumJson;
        } catch (IOException e) {
            logger.debug(e.getMessage());
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

    private static ArrayList<Course> countBasicCoursesLeft(JSONArray basic1CoursesJson, JSONArray basic2CoursesJson, JSONArray basic3CoursesJson, Set<String> takenCourses) {
        int count1 = countCoursesNotTaken(basic1CoursesJson, takenCourses);
        int count2 = countCoursesNotTaken(basic2CoursesJson, takenCourses);
        int count3 = countCoursesNotTaken(basic3CoursesJson, takenCourses);

        if (count1 == 4 && count2 == 4 && count3 == 4) {
            return allBasicCourses(basic1CoursesJson, basic2CoursesJson, basic3CoursesJson);
        } else if (count1 <= count2 && count1 <= count3) {
            return getRemainingCourses(basic1CoursesJson, takenCourses);
        } else if (count2 <= count1 && count2 <= count3) {
            return getRemainingCourses(basic2CoursesJson, takenCourses);
        } else {
            return getRemainingCourses(basic3CoursesJson, takenCourses);
        }
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

    private static ArrayList<Course> combineCourseArrays(JSONArray array1, JSONArray array2, JSONArray array3, JSONArray array4, JSONArray array5) {
        ArrayList<Course> combinedCourses = new ArrayList<>();

        addCoursesToList(combinedCourses, array1);
        addCoursesToList(combinedCourses, array2);
        addCoursesToList(combinedCourses, array3);
        addCoursesToList(combinedCourses, array4);
        addCoursesToList(combinedCourses, array5);

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
}
