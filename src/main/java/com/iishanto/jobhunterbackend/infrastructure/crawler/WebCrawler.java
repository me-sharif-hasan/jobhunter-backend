package com.iishanto.jobhunterbackend.infrastructure.crawler;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Service
public class WebCrawler {
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
            webDriver.navigate().to(url);
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
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
}
