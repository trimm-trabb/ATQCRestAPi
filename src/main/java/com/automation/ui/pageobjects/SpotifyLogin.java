package com.automation.ui.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SpotifyLogin {

    private WebDriver driver;
    @FindBy(id = "login-username")
    private WebElement usernameField;
    @FindBy(id = "login-password")
    private WebElement passwordField;
    @FindBy(id = "login-button")
    private WebElement loginButton;

    public SpotifyLogin(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String testUsername) {
        usernameField.sendKeys(testUsername);
    }

    public void enterPassword(String testPassword) {
        passwordField.sendKeys(testPassword);
    }

    public void clickNext() {
        loginButton.click();
    }
}