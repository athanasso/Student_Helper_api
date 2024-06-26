package gr.uniwa.student_helper.scraper;

import gr.uniwa.student_helper.common.UserAgentGenerator;
import gr.uniwa.student_helper.model.LoginForm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Element;

/**
 * The Scraper class is responsible for scraping data from the university website.
 * It handles the authentication process and retrieves various documents such as
 * student information, grades, and thesis information.
 */
public class Scraper {
    private final String UNIVERSITY;
    private final String DOMAIN;
    private final String PRE_LOG;
    private final String USER_AGENT;
    private boolean connected;
    private boolean authorized;
    private String infoJSON;
    private String gradesJSON;
    private String thesisJSON;
    private String totalAverageGrade;
    private Map<String, String> cookies;
    private final Logger logger = LoggerFactory.getLogger(Scraper.class);

    public Scraper(LoginForm loginForm, String university, String system, String domain) {
        this.UNIVERSITY = university;
        this.DOMAIN = domain;
        this.PRE_LOG = university + (system == null ? "" : "." + system);
        this.USER_AGENT = UserAgentGenerator.generate();
        this.connected = true;
        this.authorized = true;
        this.getDocuments(loginForm.getUsername(), loginForm.getPassword(), loginForm.getCookies());
    }
    
    public Scraper(Map<String, String> cookies, String university, String system, String domain) {
        this.UNIVERSITY = university;
        this.DOMAIN = domain;
        this.PRE_LOG = university + (system == null ? "" : "." + system);
        this.USER_AGENT = UserAgentGenerator.generate();
        this.connected = true;
        this.authorized = true;
        this.getDocuments(null, null, cookies);
    }

    /**
     * Retrieves the documents for a given user.
     * If the cookies are null, it retrieves the HTML pages using the provided username and password.
     * If the cookies are not null, it retrieves the HTML pages using the cookies.
     * If the necessary JSON data is not available, it falls back to retrieving the HTML pages using the username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param cookies  The cookies for the user's session.
     */
    private void getDocuments(String username, String password, Map<String, String> cookies) {
        if (cookies == null) {
            getHtmlPages(username, password);
        } else {
            getHtmlPages(cookies);
        }
    }

    /**
     * Retrieves HTML pages by performing a login process and making HTTP requests.
     *
     * @param username The username for the login process.
     * @param password The password for the login process.
     */
    private void getHtmlPages(String username, String password) {
        username = username.trim();
        password = password.trim();

        //
        // Request Login Html Page
        //

        Connection.Response response;
        Document doc;
        Map<String, String> data = new HashMap<>();
        String lt = null;
        String execution;
        String _csrf = null;

        try {
            response = getResponse();
            if (response == null) return;
            doc = response.parse();
            Elements el = doc.getElementsByAttributeValue("name", "lt");
            if (!el.isEmpty()) {
                lt = el.first().attributes().get("value");
            }
            Elements exec = doc.getElementsByAttributeValue("name", "execution");
            execution = exec.first().attributes().get("value");    
            data.put("username", username);
            data.put("password", password);
            if (lt != null) {
                data.put("lt", lt);
                data.put("submitForm", "Είσοδος");
            }
            data.put("execution", execution);
            data.put("_eventId", "submit");
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            return;
        }

        // store session cookies
        Map<String, String> sessionCookies = new HashMap<>();
        sessionCookies.putAll(response.cookies());

        //
        // Try to Login
        //

        try {
            response = Jsoup.connect("https://sso." + UNIVERSITY.toLowerCase() + ".gr/login?service=https%3A%2F%2F" + DOMAIN + "%2Flogin%2Fcas")
                    .data(data)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "en-US,en;q=0.9,el-GR;q=0.8,el;q=0.7")
                    .header("Cache-Control", "max-age=0")
                    .header("Connection", "keep-alive")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Host", "sso." + UNIVERSITY.toLowerCase() + ".gr")
                    .header("Origin", "https://sso." + UNIVERSITY.toLowerCase() + ".gr")
                    .header("Referer", "https://sso." + UNIVERSITY.toLowerCase() + ".gr/login?service=https%3A%2F%2F" + DOMAIN + "%2Flogin%2Fcas")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("Sec-Fetch-User", "?1")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", USER_AGENT)
                    .cookies(sessionCookies)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .execute();

            // authentication check
            Document pageIncludesToken = response.parse();
            if (!isAuthorized(pageIncludesToken)) {
                return;
            }
        } catch (SocketTimeoutException | UnknownHostException | HttpStatusException | ConnectException connException) {
            connected = false;
            logger.warn("[" + PRE_LOG + "] Warning: {}", connException.getMessage(), connException);
            return;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            return;
        }

        // store session cookies
        StringBuilder cookie = new StringBuilder();
        sessionCookies.putAll(response.cookies());
        for (Map.Entry<String, String> entry: sessionCookies.entrySet()) {
            cookie.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        
        try {
            response = Jsoup.connect("https://sso." + UNIVERSITY.toLowerCase() + ".gr/login?service=https%3A%2F%2F" + DOMAIN)
                    .data(data)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "en-US,en;q=0.9,el-GR;q=0.8,el;q=0.7")
                    .header("Cache-Control", "max-age=0")
                    .header("Connection", "keep-alive")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Host", "sso." + UNIVERSITY.toLowerCase() + ".gr")
                    .header("Origin", "https://sso." + UNIVERSITY.toLowerCase() + ".gr")
                    .header("Referer", "https://sso." + UNIVERSITY.toLowerCase() + ".gr/login?service=https%3A%2F%2F" + DOMAIN + "%2Flogin%2Fcas")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("Sec-Fetch-User", "?1")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", USER_AGENT)
                    .cookies(sessionCookies)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .execute();

            // authentication check
            Document pageIncludesToken = response.parse();
            if (!isAuthorized(pageIncludesToken)) {
                return;
            }
            
            Elements el = pageIncludesToken.getElementsByAttributeValue("name", "_csrf");
            if (!el.isEmpty()) {
                Element csrfElement = el.first();
                if (csrfElement != null) {
                    _csrf = csrfElement.attributes().get("content");
                }
            }
        } catch (SocketTimeoutException | UnknownHostException | HttpStatusException | ConnectException connException) {
            connected = false;
            logger.warn("[" + PRE_LOG + "] Warning: {}", connException.getMessage(), connException);
            return;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            return;
        }
       
        String profilesJSON = httpGET("https://" + DOMAIN + "/api/person/profiles", cookie.toString(), _csrf);
        if (profilesJSON == null) return;

        // get X-Profile variable from infoJSON
        String xProfile = getXProfile(profilesJSON);

        infoJSON = httpGET("https://" + DOMAIN + "/feign/student/student_data", cookie.toString(), _csrf, xProfile);
        if (infoJSON == null) return;

        gradesJSON = httpGET("https://" + DOMAIN + "/feign/student/grades/diploma", cookie.toString(), _csrf, xProfile);
        if (gradesJSON == null) return;
        
        thesisJSON = httpGET("https://" + DOMAIN + "/feign/student/thesis", cookie.toString(), _csrf, xProfile);
        if (thesisJSON == null) return;

        totalAverageGrade = httpGET("https://" + DOMAIN + "/feign/student/grades/average_student_course_grades", cookie.toString(), _csrf, xProfile);
        setCookies(cookie.toString(), _csrf, xProfile);
    }

    /**
     * Retrieves HTML pages using the provided cookies.
     *
     * @param cookies a map containing the necessary cookies for authentication
     */
    private void getHtmlPages(Map<String, String> cookies) {
        // Extract the nested cookies map from the main cookies map
        String cookieData = cookies.get("cookies");
        
        String cookie = null;
        String _csrf = null;
        String xProfile = null;
        
         // Parse the cookie data as a JSON string to extract individual cookies
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode cookiesNode = objectMapper.readTree(cookieData);

            // Retrieve individual cookies from the JSON object
            cookie = cookiesNode.get("cookie").asText();
            _csrf = cookiesNode.get("_csrf").asText();
            xProfile = cookiesNode.get("xProfile").asText();

            // Now you can use the retrieved cookie, _csrf, and xProfile values
            // for further processing
        } catch (IOException e) {
            // Handle JSON parsing exception
            e.printStackTrace();
        }

        if (cookie == null ||
            _csrf == null ||
            xProfile == null) return;
        
        infoJSON = httpGET("https://" + DOMAIN + "/feign/student/student_data", cookie, _csrf, xProfile);
        if (infoJSON == null) return;

        gradesJSON = httpGET("https://" + DOMAIN + "/feign/student/grades/diploma", cookie, _csrf, xProfile);
        if (gradesJSON == null) return;
        
        thesisJSON = httpGET("https://" + DOMAIN + "/feign/student/thesis", cookie, _csrf, xProfile);
        if (thesisJSON == null) return;

        totalAverageGrade = httpGET("https://" + DOMAIN + "/feign/student/grades/average_student_course_grades", cookie, _csrf, xProfile);
        setCookies(cookie, _csrf, xProfile);
    }

    /**
    * Represents a response from a web server after making a request.
    */
    private Connection.Response getResponse() {
        try {
            return Jsoup.connect("https://" + DOMAIN + "/")
                    .method(Connection.Method.GET)
                    .header("User-Agent", USER_AGENT)
                    .execute();
        } catch (SocketTimeoutException | UnknownHostException | HttpStatusException | ConnectException connException) {
            connected = false;
            logger.warn("[" + PRE_LOG + "] Warning: {}", connException.getMessage(), connException);
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Sends an HTTP GET request to the specified URL with the given cookie and CSRF token.
     * 
     * @param url the URL to send the request to
     * @param cookie the cookie to include in the request header
     * @param _csrf the CSRF token to include in the request header
     * @return the response from the server as a string, or null if the request was unsuccessful
     */
    private String httpGET(String url, String cookie, String _csrf) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setRequestProperty("X-CSRF-TOKEN", _csrf);
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuilder responseString = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responseString.append(inputLine);
                }
                in.close();
                con.disconnect();

                // print result
                return responseString.toString();
            } else {
                return null;
            }
        }  catch (ConnectException | UnknownHostException | HttpStatusException connException) {
            connected = false;
            logger.warn("[" + PRE_LOG + "] Warning: {}", connException.getMessage(), connException);
        }  catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Sends an HTTP GET request to the specified URL with the provided headers and retrieves the response.
     *
     * @param url     the URL to send the GET request to
     * @param cookie  the cookie value to include in the request headers
     * @param _csrf   the CSRF token value to include in the request headers
     * @param xProfile the X-Profile value to include in the request headers
     * @return the response string if the request is successful, or null otherwise
     */
    private String httpGET(String url, String cookie, String _csrf, String xProfile) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            con.setRequestProperty("Cache-Control", "max-age=0");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Host", DOMAIN);
            con.setRequestProperty("Referer", "https://" + DOMAIN  + "/student/grades/list_diploma?p=" + xProfile);
            con.setRequestProperty("Sec-Fetch-Dest", "empty");
            con.setRequestProperty("Sec-Fetch-Mode", "cors");
            con.setRequestProperty("Sec-Fetch-Site", "same-origin");
            con.setRequestProperty("Upgrade-Insecure-Requests", "1");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("X-CSRF-TOKEN", _csrf);
            con.setRequestProperty("X-Profile", xProfile);
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            con.setRequestProperty("Cookie", cookie);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder responseString = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responseString.append(inputLine);
                }
                in.close();
                con.disconnect();

                // print result
                return responseString.toString();
            } else {
                return null;
            }
        } catch (ConnectException | UnknownHostException | HttpStatusException connException) {
            connected = false;
            logger.warn("[" + PRE_LOG + "] Warning: {}", connException.getMessage(), connException);
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Checks if the provided document contains authorization information.
     *
     * @param document The document to check for authorization information.
     * @return true if the document contains authorization information, false otherwise.
     */
    private boolean isAuthorized(Document document) {
        String html;
        html = document.toString();
        if (html.contains("The credentials you provided cannot be determined to be authentic.")
                || html.contains("Your account is not recognized and cannot login at this time.")) {
            this.authorized = false;
            return false;
        } else {
            this.authorized = true;
            return true;
        }
    }

    /**
     * Retrieves the XProfile from the given JSON string.
     *
     * @param infoJSON the JSON string containing the student information
     * @return the XProfile as a string, or null if it cannot be retrieved
     */
    private String getXProfile(String infoJSON) {
        try {
            JsonNode node = new ObjectMapper().readTree(infoJSON);
            JsonNode studentProfiles = node.get("studentProfiles");
            for (JsonNode student: studentProfiles)  {
                return student.get("id").asText();
            }
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
        }
        return null;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public String getInfoJSON() {
        return infoJSON;
    }

    public String getGradesJSON() {
        return gradesJSON;
    }

    public String getThesisJSON() {
        return thesisJSON;
    }

    public String getTotalAverageGrade() {
        return totalAverageGrade;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(String cookie, String _csrf, String xProfile) {
        this.cookies = new HashMap<>();
        cookies.put("cookie", cookie);
        cookies.put("_csrf", _csrf);
        cookies.put("xProfile", xProfile);
    }
}