package Base;

import EndPoints.EndPoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

public class BaseTest extends SpotifyAuth {


    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setupRequestSpec() {
        String token = getAccessToken();
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(EndPoints.BASE_URL)
                .setBasePath(EndPoints.BASE_PATH)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        System.out.println("setupRequestSpec: ");
    }

}
