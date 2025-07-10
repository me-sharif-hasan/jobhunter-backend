package com.iishanto.jobhunterbackend.config;

import jakarta.annotation.PreDestroy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;
import java.util.UUID;

@Component
@RequestScope
public class WebDriverManager {
    private WebDriver driver;
    private static WebDriverManager instance;
    public WebDriverManager() {
        init();
    }

    public WebDriver getDriver() {
        try{
            driver.getWindowHandle();
        } catch (Exception e) {
            init();
        }
        if(driver == null || ((ChromeDriver) driver).getSessionId() == null) {
            throw new RuntimeException("WebDriver is not initialized properly.");
        }
        return driver;
    }


    public void init(){
//        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); // Ensure JS execution in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--enable-javascript");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + UUID.randomUUID());
        options.addArguments("--lang=en-US");
        options.setExperimentalOption("prefs", Map.of(
                "intl.accept_languages", "en-US"
        ));

        driver = new ChromeDriver(options);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Destroying WebDriver instance");
        try{
            if (driver != null) {
                driver.quit();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
