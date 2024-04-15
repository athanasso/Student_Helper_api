package gr.uniwa.student_helper.resources;

import gr.uniwa.student_helper.model.FileCourse;
import gr.uniwa.student_helper.model.FileData;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ImportService;
import gr.uniwa.student_helper.services.LoginService;
import gr.uniwa.student_helper.services.RefreshService;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class JavaEE8ResourceTest {

    @Mock
    private LoginService scrapeService;

    @Mock
    private ImportService importService;
    
    @Mock
    private RefreshService refreshService;

    @InjectMocks
    private JavaEE8Resource javaEE8Resource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetStudentWithValidFileData() {
        ArrayList<FileCourse> courses = new ArrayList();
        FileCourse course = new FileCourse();
        courses.add(course);
        FileData fileData = new FileData("ΠΡΟΓΡΑΜΜΑ 5 ΕΤΩΝ ΣΠΟΥΔΩΝ (2019)", "", "2017",courses);
        ResponseBuilder responseBuilder = Response.ok().entity("Student data");
        when(importService.getStudent(fileData)).thenReturn(responseBuilder);

        Response response = javaEE8Resource.getStudent(fileData);

        assertEquals(200, response.getStatus());
        assertEquals("Student data", response.getEntity());
    }

    @Test
    public void testGetStudentWithEmptyFileData() {
        FileData fileData = new FileData("", "", "", new ArrayList<>());

        Response response = javaEE8Resource.getStudent(fileData);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testGetStudentWithValidLoginFormAndAuthorizationHeader() {
        LoginForm loginForm = new LoginForm("username", "password");
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Basic dXNlcm5hbWU6cGFzc3dvcmQ="); // Authorization header with username:password encoded in Base64

        ResponseBuilder responseBuilder = Response.ok().entity("Student data");
        when(scrapeService.getStudent("uniwa", loginForm)).thenReturn(responseBuilder);

        Response response = javaEE8Resource.getStudent(loginForm, headers);

        assertEquals(200, response.getStatus());
        assertEquals("Student data", response.getEntity());
    }

    @Test
    public void testGetStudentWithValidLoginFormAndNoAuthorizationHeader() {
        LoginForm loginForm = new LoginForm("username", "password");
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null); // No Authorization header

        Response response = javaEE8Resource.getStudent(loginForm, headers);

        assertEquals(401, response.getStatus());
    }

    @Test
    public void testGetStudentWithNullLoginForm() {
        LoginForm loginForm = null;
        HttpHeaders headers = mock(HttpHeaders.class);

        Response response = javaEE8Resource.getStudent(loginForm, headers);

        assertEquals(401, response.getStatus());
    }
    
    @Test
    public void testGetStudentWithValidCookies() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("cookie1", "value1");
        cookies.put("cookie2", "value2");

        ResponseBuilder responseBuilder = Response.ok().entity("Student data");
        when(refreshService.getStudent("uniwa", cookies)).thenReturn(responseBuilder);

        Response response = javaEE8Resource.getStudent(cookies);

        assertEquals(200, response.getStatus());
        assertEquals("Student data", response.getEntity());
    }

    @Test
    public void testGetStudentWithEmptyCookies() {
        Map<String, String> cookies = new HashMap<>();

        Response response = javaEE8Resource.getStudent(cookies);

        assertEquals(401, response.getStatus());
    }

    @Test
    public void testGetStudentWithException() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("cookie1", "value1");
        cookies.put("cookie2", "value2");

        when(refreshService.getStudent("uniwa", cookies)).thenThrow(new RuntimeException("Error"));

        Response response = javaEE8Resource.getStudent(cookies);

        assertEquals(400, response.getStatus());
    }
}