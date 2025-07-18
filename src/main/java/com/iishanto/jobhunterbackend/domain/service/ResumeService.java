package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.AiAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.JobDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleCalculatedResumeStrengthModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.usecase.ResumeManagementUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeService implements ResumeManagementUseCase {
    private final UserDataAdapter userDataAdapter;
    private final JobDataAdapter jobDataAdapter;
    private final AiAdapter aiAdapter;
    @Override
    public Long uploadResume(InputStream fileIo, String contentType) throws IOException{
        if(contentType ==null|| !(contentType.equals("application/pdf")||contentType.equals("application/octet-stream"))){
            throw new IllegalArgumentException("Invalid file type. Only PDF files are allowed.");
        }
        if(fileIo==null){
            throw new IllegalArgumentException("File input stream cannot be null.");
        }
        String text = getPdfText(fileIo);
        if(text.isBlank()){
            throw new IllegalArgumentException("The PDF file is empty or could not be read. Is this a image based PDF?");
        }
        Long userId = userDataAdapter.getLoggedInUser().getId();
        if(userId == null){
            throw new IllegalStateException("User is not logged in or user ID is not available.");
        }
        return userDataAdapter.saveResume(text,userId);
    }

    @Override
    public SimpleCalculatedResumeStrengthModel getResumeStrength(String jobId) {
        Long userId = userDataAdapter.getLoggedInUser().getId();
        String resumeText = userDataAdapter.getResumeTextByUserId(userId);
        if (resumeText == null || resumeText.isBlank()) {
            throw new IllegalArgumentException("Resume is empty or not found for user ID: " + userId);
        }
        SimpleJobModel job = jobDataAdapter.getJobById(jobId);
        if (job == null) {
            throw new IllegalArgumentException("Job not found for ID: " + jobId);
        }
        String prompt= """
                You are a resume strength evaluator.
                Evaluate the resume text against the job description and return a score between 0 to 100.
                The higher the score, the better the match.
                Job Description: %s
                Resume Text: %s
                -----
                Evaluate the resume based on the following criteria: %s
                """.formatted(job.getJobDescription(), resumeText, StringUtils.join(getWeights().keySet().stream().map(key->key+" : Maximum Mark: "+getWeights().get(key)).toList(),", "));
        AiResponse data = aiAdapter.getPromptResponse(prompt,AiResponse.class);
        return SimpleCalculatedResumeStrengthModel.builder().score(data.resumeScore).reasoning(data.reasoning).build();
    }

    private Map<String,Integer> getWeights(){
        // Keyword Matching (40)
        // ATS Friendliness (14)
        // Experience and Achievements (23)
        // Education and Certifications (10)
        // Regional and Industry Alignment (8)
        // Red Flags (Negative up to -20)
        return Map.ofEntries(
                // Keyword Matching (40)
                Map.entry("hard_skill_match", 20),
                Map.entry("soft_skill_match", 5),
                Map.entry("job_title_relevance", 5),
                Map.entry("domain_keywords", 5),
                Map.entry("jd_to_resume_keyword_density", 5),

                // ATS Friendliness (14)
                Map.entry("no_table_usage", 2),
                Map.entry("proper_headings", 2),
                Map.entry("reverse_chronological_format", 2),
                Map.entry("consistent_fonts_layout", 2),
                Map.entry("bullet_point_usage", 2),
                Map.entry("contact_info_and_links", 2),
                Map.entry("file_type_and_size", 2),

                // Experience and Achievements (23)
                Map.entry("relevant_experience", 10),
                Map.entry("measurable_achievements", 5),
                Map.entry("action_oriented_language", 5),
                Map.entry("leadership_or_initiative", 3),

                // Education and Certifications (10)
                Map.entry("relevant_degree", 5),
                Map.entry("institution_tier", 2),
                Map.entry("certifications", 3),

                // Regional and Industry Alignment (8)
                Map.entry("regional_indicators", 3),
                Map.entry("industry_lingo", 3),
                Map.entry("soft_skill_indicators", 2),

                // Red Flags (Negative up to -20)
                Map.entry("typos_or_grammar_issues", -5),
                Map.entry("paragraph_heavy_sections", -3),
                Map.entry("irrelevant_content", -5),
                Map.entry("generic_objective_section", -2),
                Map.entry("too_long_resume", -2),
                Map.entry("buzzword_without_evidence", -3)
        );
    }

    private String getPdfText(InputStream fileIo) throws IOException {
        File file = File.createTempFile("resume_%d".formatted(1), ".pdf");
        FileUtils.copyInputStreamToFile(fileIo,file);
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();
        PDFTextStripper stripper = new PDFTextStripper();
        PDDocument document = parser.getPDDocument();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    @Data
    static class AiResponse{
        private Integer resumeScore;
        private Map<String, Integer> evaluationBreakdown;
        private String reasoning;
    }
}
