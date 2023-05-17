package demoMaven.DemoMavenProject;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

public class TestCase3 {

	 private String baseURI = "https://restful-booker.herokuapp.com";
	 public int bookingid;
	 public static String authtoken;
	    
	    //Create the booking request
	    @Test(priority = 1)
	    public void createBookingTest() {
	    	
	        Response response = given().
	            contentType("application/json").
	            body(Payload.createBookingBody()).
	        when().
	            post(baseURI+"/booking").
	        then().
	            assertThat().
	            statusCode(200).
	            body("bookingid", notNullValue()).
	            body("booking.firstname", equalTo("Jim")).
	            body("booking.lastname", equalTo("Brown")).extract().response();
	        JsonPath res = response.jsonPath();
	        bookingid = res.get("bookingid");
	        
	    }
	    
	    // Get Auth token for PUT and DELETE
	    @Test(priority = 2)
	    public void getAuthToken() {

	        Response response = given().
	            contentType("application/json").
	            body(Payload.getAuthTokenBody()).
	        when().
	            post(baseURI+"/auth").
	        then().
	            assertThat().
	            statusCode(200).
	            extract().response();
	        JsonPath res = response.jsonPath();
	        authtoken = res.get("token");
	      
	    }

//	    //Update the booking request
	    @Test(priority = 3)
	    public void updateBookingTest() {

	        given().contentType("application/json").cookie("token",authtoken).
	        body(Payload.updateBookingBody()).
	        when().
	            put(baseURI + "/booking/" + bookingid).
	        then().
	            assertThat().
	            statusCode(200).
	            body("firstname", equalTo("Jim")).
	            body("lastname", equalTo("Brown")).
	            body("totalprice", equalTo(350)).
	            body("bookingdates.checkin", equalTo("2018-01-01")).
	            body("bookingdates.checkout", equalTo("2019-01-01"));
	    }
	    
	   //Read the booking request
	    @Test(priority = 4)
	    public void getBookingTest() {
	       Response res = given().
	        when().
	            get(baseURI + "/booking/" + bookingid).
	        then().log().all().
	            assertThat().
	            statusCode(200).
	            body("firstname", notNullValue()).
	            body("lastname", notNullValue()).extract().response();
	       System.out.println(res);
	    }

	    //Delete the booking request
	    @Test(priority = 5)
	    public void deleteBookingTest() {
	        given().cookie("token",authtoken).
	        when().
	            delete(baseURI + "/booking/" + bookingid).
	        then().
	            assertThat().
	            statusCode(201);
	    }


}
