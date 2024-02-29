package gr.uniwa.student_helper.resources;

import gr.uniwa.student_helper.model.FileCourse;
import gr.uniwa.student_helper.model.FileData;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ImportService;
import gr.uniwa.student_helper.services.ScrapeService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class JavaEE8ResourceTest {

    @Mock
    private ScrapeService scrapeService;

    @Mock
    private ImportService importService;

    @InjectMocks
    private JavaEE8Resource javaEE8Resource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetStudentWithValidLoginForm() {
        LoginForm loginForm = new LoginForm("username", "password");
        ResponseBuilder responseBuilder = Response.ok().entity("Student data");
        when(scrapeService.getStudent("uniwa", loginForm)).thenReturn(responseBuilder);

        Response response = javaEE8Resource.getStudent(loginForm);

        assertEquals(200, response.getStatus());
        assertEquals("Student data", response.getEntity());
    }

    @Test
    public void testGetStudentWithValidFileData() {
        ArrayList<FileCourse> courses = new ArrayList();
        FileCourse course = new FileCourse();
        courses.add(course);
        FileData fileData = new FileData("ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)", "2017",courses);
        ResponseBuilder responseBuilder = Response.ok().entity("Student data");
        when(importService.getStudent(fileData)).thenReturn(responseBuilder);

        Response response = javaEE8Resource.getStudent(fileData);

        assertEquals(200, response.getStatus());
        assertEquals("Student data", response.getEntity());
    }
    
    @Test
    public void testGetStudentWithNullLoginForm() {
        LoginForm loginForm = null;

        Response response = javaEE8Resource.getStudent(loginForm);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testGetStudentWithEmptyFileData() {
        FileData fileData = new FileData("", "", new ArrayList<>());

        Response response = javaEE8Resource.getStudent(fileData);

        assertEquals(400, response.getStatus());
    }
}