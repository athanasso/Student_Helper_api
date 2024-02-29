package gr.uniwa.student_helper;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * The JAXRSConfiguration class is responsible for configuring the JAX-RS (Java API for RESTful Web Services) application.
 * It extends the Application class, which is the base class for all JAX-RS applications.
 * The @ApplicationPath annotation specifies the base URI path for all JAX-RS resources in the application.
 * In this case, the base URI path is "api".
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {
    
}
