package restAssuredBasics.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class TestCaseIntegration {

    // create token
    // create booking id
    // perform PUT request
    // Verify PUT by GET
    // Delete ID
    // Verify it deleted by GET request

    RequestSpecification r ;
    Response rs ;
    ValidatableResponse vr ;

    String token;
    String booking_id;

    public String getToken ()
    {
        String payload = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/auth");
        r.contentType(ContentType.JSON);
        r.body(payload);


        //when response
        rs = r.when().post(); // we have to store when request into response

        //then validatable response
        vr  = rs.then();
        vr.statusCode(200);
        token = rs.jsonPath().getString("token");

        return token;
    }

    public String getBooking_id ()
    {
        String payload = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";
        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking");
        r.contentType(ContentType.JSON);
        r.body(payload);


        rs= r.when().post();

        vr = rs.then().log().all();
        vr.statusCode(200);
        booking_id= rs.jsonPath().getString("bookingid");

        return booking_id;
    }

    @Test (priority = 1)
    public void test_update_request_put ()
    {
        token = getToken();
        booking_id = getBooking_id();

        String payload = "{\n" +
                "    \"firstname\" : \"Tatya\",\n" +
                "    \"lastname\" : \"Kumbhar\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/"+booking_id);
        r.cookie("token",token);
        r.contentType(ContentType.JSON);
        r.body(payload).log().all();

        rs = r.when().put();
        vr = rs.then().log().all();
        vr.statusCode(200);


    }

    @Test (priority = 2)
    public void test_update_request_get ()
    {
        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/"+ booking_id);

        rs = r.when().get();
        vr = rs.then().log().all();
        vr.statusCode(200);

    }

    @Test (priority = 3)
    public void test_delete_booking ()
    {
        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/"+booking_id);
        r.cookie("token",token);
        r.contentType(ContentType.JSON);

        rs = r.when().delete();
        vr = rs.then().log().all();
        vr.statusCode(201); // #TODO #1 Bug
    }

    @Test (priority = 4)
    public void test_verify_delete_get ()
    {
        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/"+ booking_id);

        rs = r.when().get();
        vr = rs.then().log().all();
        vr.statusCode(404);
    }


}
