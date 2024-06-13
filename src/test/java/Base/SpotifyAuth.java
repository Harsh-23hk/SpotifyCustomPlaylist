package Base;


import Utils.ReaderConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;


public class SpotifyAuth {

    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private static String REDIRECT_URI;
    private static String AUTH_URL;
    private static String TOKEN_URL;
    private static String accessToken;

    @BeforeSuite
    public void setup() {
        ReaderConfig config = new ReaderConfig();
        CLIENT_ID = config.getClientId();
        CLIENT_SECRET = config.getClientSecret();
        REDIRECT_URI = config.getRedirectUri();
        AUTH_URL = config.getAuthUrl();
        TOKEN_URL = config.getTokenUrl();
            generateAuthorizationUrl();

    }


    public void generateAuthorizationUrl() {
        String authorizationUrl = AUTH_URL + "?client_id=" + CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=playlist-read-private playlist-modify-private playlist-modify-public " +
                "user-follow-modify user-follow-read user-read-email " +
                "user-read-private user-read-currently-playing " +
                "user-read-playback-state user-library-read user-library-modify" +
                "user-top-read user-read-recently-played user-read-playback-position";

        System.out.println("Visit the following URL in your browser");
        System.out.println(authorizationUrl);

        String authorizationCode = "Authorization Code";

        // Simulate obtaining the authorization code for testing
        exchangeAuthorizationCodeForAccessToken(authorizationCode);
    }

    public void exchangeAuthorizationCodeForAccessToken(String authorizationCode) {
        Assert.assertNotNull(authorizationCode, "Authorization code is null. Please authorize the application first.");

        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "authorization_code");
        formParams.put("code", authorizationCode);
        formParams.put("redirect_uri", REDIRECT_URI);
        formParams.put("client_id", CLIENT_ID);
        formParams.put("client_secret", CLIENT_SECRET);

        Response response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(formParams)
                .post(TOKEN_URL);

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to get access token");

        accessToken = response.jsonPath().getString("access_token");
       // System.out.println("Access Token: " + accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

}







