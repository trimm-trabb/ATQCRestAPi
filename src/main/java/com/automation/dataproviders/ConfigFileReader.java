package com.automation.dataproviders;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigFileReader {

    private Properties properties;
    private final static String fileName = "config.properties";
    private final static Logger LOGGER = Logger.getLogger(ConfigFileReader.class.getName());

    public ConfigFileReader() {
        ClassLoader classLoader = ConfigFileReader.class.getClassLoader();
        try {
            File file = new File(classLoader.getResource(fileName).getFile());
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                properties = new Properties();
                properties.load(reader);
            }
        } catch (NullPointerException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not read configuration file " + fileName, ex);
        }
    }

    public String getClientId() {
        String clientID = properties.getProperty("clientID");
        if (clientID != null)
            return clientID;
        else
            throw new RuntimeException("ClientID not specified in the config.properties file");
    }

    public String getClientSecret() {
        String clientSecret = properties.getProperty("clientSecret");
        if (clientSecret != null)
            return clientSecret;
        else
            throw new RuntimeException("ClientSecret not specified in the config.properties file");
    }

    public String getTokenEndpoint() {
        String tokenEndpoint = properties.getProperty("tokenEndpoint");
        if (tokenEndpoint != null)
            return tokenEndpoint;
        else
            throw new RuntimeException("Token Endpoint not specified in the config.properties file");
    }

    public String getUsername() {
        String username = properties.getProperty("username");
        if (username != null)
            return username;
        else
            throw new RuntimeException("Username not specified in the config.properties file");
    }

    public String getPassword() {
        String password = properties.getProperty("password");
        if (password != null)
            return password;
        else
            throw new RuntimeException("Password not specified in the config.properties file");
    }

    public String getRedirectUri() {
        String redirectUri = properties.getProperty("redirectUri");
        if (redirectUri != null)
            return redirectUri;
        else
            throw new RuntimeException("Redirect Uri not specified in the config.properties file");
    }

    public String getAuthEndpoint() {
        String authEndpoint = properties.getProperty("authEndpoint");
        if (authEndpoint != null)
            return authEndpoint;
        else
            throw new RuntimeException("Authorization endpoint not specified in the config.properties file");
    }

    public String getScope() {
        String scope = properties.getProperty("scope");
        if (scope != null)
            return scope;
        else
            throw new RuntimeException("Scope not specified in the config.properties file");
    }

    public String getUserId() {
        String userID = properties.getProperty("userID");
        if (userID != null)
            return userID;
        else
            throw new RuntimeException("UserID not specified in the config.properties file");
    }
}