package com.iishanto.jobhunterbackend.testutils;

import com.iishanto.jobhunterbackend.infrastructure.crawler.WebCrawler;
import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import org.apache.commons.io.FileUtils;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Component
public class TestDataFactory {
    private final WebCrawler webCrawler=Mockito.mock(WebCrawler.class);
    @Autowired
    private SiteRepository siteRepository;

    public Site createSite(String homepage, String careerPageUrl) {
        Site site = Site.builder()
                .name("Test Site")
                .jobListPageUrl(careerPageUrl)
                .homepage(homepage)
                .description("This is a test site for job listings.")
                .build();
        site = siteRepository.save(site);
        return site;
    }

    public void setupWebCrawler() throws IOException {
        setupWebCrawler(anyString(),"/test/sample-html-bs.html");
    }

    public void setupWebCrawler(String urlMatcher,String pathOfHtml) throws IOException {
        String htmlFile = this.getClass().getResource(pathOfHtml).getFile();
        htmlFile = FileUtils.readFileToString(new File(htmlFile), "UTF-8");
        when(webCrawler.getRawHtml(urlMatcher)).thenReturn(htmlFile);
        when(webCrawler.getHtml(urlMatcher)).thenReturn(htmlFile);
    }
    public void resetWebCrawler() {
        Mockito.reset(webCrawler);
    }

    @Autowired
    private JobsRepository jobsRepository;

    public Opportunity createJob(String jobId, String title, String url, boolean isApproved) {
        Opportunity opportunity = new Opportunity();
        opportunity.setJobId(jobId);
        opportunity.setTitle(title);
        opportunity.setJobUrl(url);
        opportunity.setApproved(isApproved);
        opportunity.setJobParsedAt(Timestamp.from(Instant.now()));
        opportunity.setJobUpdatedAt(Timestamp.from(Instant.now()));
        opportunity.setIsPresentOnSite(true);
        return jobsRepository.save(opportunity);
    }
}
