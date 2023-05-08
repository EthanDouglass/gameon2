package com.coms309;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

@RunWith(SpringRunner.class)
public class TestingSystemTest {

    @LocalServerPort
    int port;
    private static int signupID;
    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost:8080";
    }



    @Test
    public void AsignupSuccessful() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "TESTWORKUSERNAME");
        requestBody.put("password", "TESTWORKPW");

        // Send request and receive response
        Response response = RestAssured.given().
                contentType("application/json").
                body(requestBody.toString()).
                when().
                post("/signup/?displayName=TESTINGWORK");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();

        try {
            JSONObject returnObj = new JSONObject(returnString);
            signupID = (int) returnObj.get("playerid");
            assertEquals("success", returnObj.get("SR"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void BloginSuccessful() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "TESTWORKUSERNAME");
        requestBody.put("password", "TESTWORKPW");

        // Send request and receive response
        Response response = RestAssured.given().
                contentType("application/json").
                body(requestBody.toString()).
                when().
                post("/Login/");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals(signupID, returnObj.get("playerid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void DsignupUsernameExists() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "TESTWORKUSERNAME");
        requestBody.put("password", "TESTWORKPW");

        // Send request and receive response
        Response response = RestAssured.given().
                contentType("application/json").
                body(requestBody.toString()).
                when().
                post("/signup/?displayName=TESTINGWORK");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("Username already exists", returnObj.get("SR"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void EdeleteAccount() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "TESTWORKUSERNAME");
        requestBody.put("password", "TESTWORKPW");

        // Send request and receive response
        Response response = RestAssured.given().
                contentType("application/json").
                body(requestBody.toString()).
                when().
                delete("/deleteaccount/?ID=" + signupID);

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("Account Deleted Successfully", returnObj.get("SR"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}