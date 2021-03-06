package edu.hm.shareit.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Uses HTTP. Use this for local testing. (both auth-ms and main-ms run on localhost).
 * 
 * @author Rebecca Brydon, Michael Eggers
 *
 */
public class JettyAuth implements AuthInterface {

    private final String httpURL = "http://localhost:9999/auth/users/";
    
    public JettyAuth() {
        
    }
    
    /**
     * See interface.
     */
    @Override
    public String authenticate(final String token) throws IOException {
        String result = "";
        
        URL targetURL = new URL(httpURL + token);
        HttpURLConnection connection = (HttpURLConnection) targetURL.openConnection();

        try (InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(isr)) {

            result = in.readLine();
        }

        return result;
    }

}
