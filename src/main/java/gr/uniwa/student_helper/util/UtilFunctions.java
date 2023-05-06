package gr.uniwa.student_helper.util;

import gr.uniwa.student_helper.model.Course;
import java.util.ArrayList;

public class UtilFunctions {
    
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
}
