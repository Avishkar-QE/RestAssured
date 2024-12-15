package restAssuredBasics;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class Post {

    //url = https://restful-booker.herokuapp.com/auth
    //header = Content-Type: application/json
    //Body = {
    //    "username" : "admin",
    //    "password" : "password123"
    //}


    @Description ("Verify Post Request")
    @Test
    public void test_Post_Request(){

        String payload = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        RestAssured.
                given()
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/auth")
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .log().all()
                .when()
                    .log().all()
                    .post()
                .then()
                    .log().all()
                    .statusCode(200);

    }
}
