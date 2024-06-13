package Test;

import Base.BaseTest;
import Utils.ReaderConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserProfileTest extends BaseTest {


    @Test
    public void getUserProfile() {
        ReaderConfig config = new ReaderConfig();
        String USER_ID = config.getUserId(); // Replace with actual user ID

        Response response = RestAssured
                .given()
                .spec(requestSpec)
                .get("/users/" + USER_ID);

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to get user profile");
        System.out.println("User Profile: " + response.asString());
    }
}
