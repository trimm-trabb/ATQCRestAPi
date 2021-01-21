package com.automation.ui.businessobjects;

import com.automation.dataproviders.ConfigFileReader;
import com.automation.ui.pageobjects.SpotifyLogin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SpotifyUserAuthorizationFlow {

    private WebDriver driver;
    private ConfigFileReader reader;

    public SpotifyUserAuthorizationFlow(WebDriver driver) {
        this.driver = driver;
        reader = new ConfigFileReader();
    }

    public String userAuthorization(String scope) {
        driver.get(reader.getAuthEndpoint() + "?client_id=" + reader.getClientId() +
                "&response_type=code&redirect_uri=" + reader.getRedirectUri() + "&scope=" + scope);
        SpotifyLogin loginPage = new SpotifyLogin(driver);
        loginPage.enterUsername(reader.getUsername());
        loginPage.enterPassword(reader.getPassword());
        loginPage.clickNext();
        new WebDriverWait(driver, 20).until(ExpectedConditions.titleContains("localhost"));
        String url = driver.getCurrentUrl();
        driver.close();
        return url.substring(url.indexOf("=") + 1);
    }
}