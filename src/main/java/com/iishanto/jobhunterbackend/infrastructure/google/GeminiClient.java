package com.iishanto.jobhunterbackend.infrastructure.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CharMatcher;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.infrastructure.crawler.WebCrawler;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import lombok.Builder;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.jsoup.Jsoup;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Service
public class GeminiClient {
    RestClient restClient;
    String baseUrl;
    String apiKey;
    WebCrawler webCrawler;
    private final ReentrantLock lock=new ReentrantLock();
    public GeminiClient(@Value("${google.gemini.endpoint}") String baseUrl, @Value("${google.gemini.apikey}") String apiKey,WebCrawler webCrawler){
        this.restClient=RestClient.create();
        this.baseUrl=baseUrl;
        this.apiKey=apiKey;
        this.webCrawler=webCrawler;
    }
    public String getResponse(String prompt){
        try{
            System.out.println("Gemini client is requested by "+Thread.currentThread().getName());
            lock.lock();
            System.out.println("Gemini client is being used by: "+Thread.currentThread().getName());
            String response=restClient.post().uri(baseUrl+"?key="+apiKey).body(prompt).retrieve().body(String.class);
            System.out.println("GEMINI RESPONSED: "+response);
            return response;
        }catch (Exception ignored){
            System.out.println("GEMINI CLIENT ERROR: "+ignored.getLocalizedMessage());
            ignored.printStackTrace();
        }finally {
            System.out.println("Gemini client is release by: "+Thread.currentThread().getName());
            lock.unlock();
        }
        return "I am Gemini";
    }

    public List<SimpleJobModel> getJsonResponseOfJobs(GeminiPrompt prompt){
        String response = getResponse(prompt.getPromptTemplate(GeminiPromptLibrary.PromptType.JOB_LIST));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            for(GeminiResponse.Candidate candidate:geminiResponse.getCandidates()){
                String json=candidate.getContent().getParts().get(0).getText();
                json=sanitizeJsonString(json.substring(7,json.length()-3));
                System.out.println("the json: "+json);
                List < SimpleJobModel > jobModels = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, SimpleJobModel.class));
                return jobModels;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("An error happened while parsing the response: "+e.getMessage());
            return null;
        }

    }

    public GeminiPrompt getJobListingPromptFromUrl(String url){
        try{
            String html = webCrawler.getHtml(url);
            return GeminiPrompt.builder().temperature(0).message(html).baseUrl(url).build();
        }catch (Exception e) {
            System.out.println("An error happened while getting prompt: " + e.getMessage());
            return null;
        }
    }

    public GeminiPrompt getMetadataPromptFromUrl(String url){
        try{
            System.out.println("Requesting metadata for job: "+url);
            String html = webCrawler.getHtml(url);
            return GeminiPrompt.builder().temperature(0).message(html).baseUrl(url).build();
        }catch (Exception e) {
            System.out.println("An error happened while getting prompt: " + e.getMessage());
            return null;
        }
    }

    private String sanitizeJsonString(String json){
        return CharMatcher.javaIsoControl().and(CharMatcher.isNot('\n'))
                .and(CharMatcher.isNot('\r'))
                .and(CharMatcher.isNot('\t'))
                .removeFrom(json);
    }

    public SimpleJobModel getJobMetadata(GeminiPrompt prompt) {
        String response = getResponse(prompt.getPromptTemplate(GeminiPromptLibrary.PromptType.JOB_DETAIL));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            for(GeminiResponse.Candidate candidate:geminiResponse.getCandidates()){
                String json=candidate.getContent().getParts().get(0).getText();
                json=sanitizeJsonString(json.substring(7,json.length()-3));
                SimpleJobModel jobModel = objectMapper.readValue(json, SimpleJobModel.class);
                System.out.println(json);
                return jobModel;
            }
            return null;
        }catch (Exception e){
            System.out.println("An error happened while parsing the response: "+e.getMessage());
            return null;
        }
    }

    @Builder
    public static class GeminiPrompt{
        private int temperature;
        private String message;
        String baseUrl;
        public String getPromptTemplate(GeminiPromptLibrary.PromptType promptType){
            Document document=Jsoup.parse(this.message);
            String body = document.html();
            String markdown= FlexmarkHtmlConverter.builder().build().convert(body);
            String escapedMessage = markdown.replace("\"","\\\"");
            escapedMessage=sanitizeMarkdown(markdown);
//            escapedMessage=escapedMessage.replaceAll("\\(data:(image|video)/(.*?)\\)","");
            return GeminiPromptLibrary.getPrompt(
                    promptType,
                    GeminiPromptLibrary.PromptParameters.builder()
                            .escapedMessage(escapedMessage)
                            .baseUrl(this.baseUrl)
                            .temperature(this.temperature)
                            .build()
            );
        }

        private String sanitizeMarkdown(String markdown) {
            return CharMatcher.javaIsoControl() // Remove non-printable control characters
                    .and(CharMatcher.isNot('\n'))
                    .and(CharMatcher.isNot('\r'))
                    .and(CharMatcher.isNot('\t'))
                    .removeFrom(markdown)
                    .replaceAll("\\(data:(image|video)/[^)]+\\)", "") // Remove base64 media links
                    .replace("\"", "\\\"");
        }

    }
}
