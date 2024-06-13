package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReaderConfig {

    Properties properties;
    String File = "C:\\Users\\Harsh\\IdeaProjects\\SpotifyCustomPlaylist\\Configuration\\config.properties";


    public ReaderConfig() {
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(File);
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getClientId() {
        return properties.getProperty("CLIENT_ID");
    }

    public String getClientSecret() {
        return properties.getProperty("CLIENT_SECRET");
    }

    public String getUserId() {
        return properties.getProperty("USER_ID");
    }

    public String getRedirectUri() {
        return properties.getProperty("REDIRECT_URI");
    }

    public String getAuthUrl() {
        return properties.getProperty("AUTH_URL");
    }

    public String getTokenUrl() {
        return properties.getProperty("TOKEN_URL");
    }


}
