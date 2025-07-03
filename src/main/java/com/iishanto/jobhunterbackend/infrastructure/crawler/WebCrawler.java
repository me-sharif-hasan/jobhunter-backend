package com.iishanto.jobhunterbackend.infrastructure.crawler;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteValidationDataAdapter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Service
public class WebCrawler implements AdminSiteValidationDataAdapter {
    RestClient restClient=RestClient.create();
    WebDriver webDriver;
    public WebCrawler(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    public String getRawHtml(String url){
        return restClient.get().uri(url).retrieve().body(String.class);
    }
    public String getHtml(String url) {
        try{
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            webDriver.manage().deleteAllCookies();
            js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
            js.executeScript("window.localStorage.clear();");
            js.executeScript("window.sessionStorage.clear();");
            webDriver.navigate().to(url);
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            try{
                Thread.sleep(5000);
            }catch (Exception ignored) {
            }
            return webDriver.getPageSource();
        }catch (Exception e){
            throw new RuntimeException("Failed to get html from url: "+url);
        }
    }

    @Override
    public String getRenderedHtmlPage(String url) {
        return getHtml(url);
    }
}
