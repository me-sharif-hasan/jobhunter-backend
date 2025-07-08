package com.iishanto.jobhunterbackend.infrastructure.crawler;

import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteValidationDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class WebCrawler {
    RestClient restClient = RestClient.create();
    WebDriver webDriver;

    public WebCrawler(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getRawHtml(String url) {
        return restClient.get().uri(url).retrieve().body(String.class);
    }

    public String getHtml(String url) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            webDriver.manage().deleteAllCookies();
            js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
            js.executeScript("window.localStorage.clear();");
            js.executeScript("window.sessionStorage.clear();");
            webDriver.navigate().to(url);
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            try {
                Thread.sleep(5000);
            } catch (Exception ignored) {
            }
            return webDriver.getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get html from url: " + url);
        }
    }
}
