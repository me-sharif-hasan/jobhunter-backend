package com.iishanto.jobhunterbackend.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.infrastructure.database.UserResume;
import com.iishanto.jobhunterbackend.infrastructure.google.GeminiClient;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserResumeRepository;
import com.iishanto.jobhunterbackend.testutils.TestDataFactory;
import com.iishanto.jobhunterbackend.testutils.TestUtils;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ResumeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserResumeRepository userResumeRepository;
    @MockitoBean
    private GeminiClient geminiClient;
    @DynamicPropertySource
    static void setProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        TestUtils.setupMySQL(registry);
    }
    String jwtToken;
    @BeforeEach
    void setUp() {
        jwtToken = testUtils.setupAuthentication();
        assertNotNull(jwtToken, "JWT Token should not be null");
    }

    private final static String GEMINI_RESPONSE = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      {
                        "text": "```json\\n{\\n  \\"resumeScore\\": 45,\\n  \\"evaluationBreakdown\\": {\\n    \\"irrelevant_content\\": -2,\\n    \\"soft_skill_match\\": 2,\\n    \\"paragraph_heavy_sections\\": -1,\\n    \\"job_title_relevance\\": 0,\\n    \\"jd_to_resume_keyword_density\\": 3,\\n    \\"institution_tier\\": 1,\\n    \\"buzzword_without_evidence\\": -1,\\n    \\"too_long_resume\\": 0,\\n    \\"proper_headings\\": 2,\\n    \\"relevant_experience\\": 5,\\n    \\"reverse_chronological_format\\": 2,\\n    \\"file_type_and_size\\": 2,\\n    \\"hard_skill_match\\": 8,\\n    \\"typos_or_grammar_issues\\": 0,\\n    \\"action_oriented_language\\": 3,\\n    \\"leadership_or_initiative\\": 1,\\n    \\"bullet_point_usage\\": 2,\\n    \\"domain_keywords\\": 2,\\n    \\"consistent_fonts_layout\\": 2,\\n    \\"generic_objective_section\\": -1,\\n    \\"relevant_degree\\": 5,\\n    \\"soft_skill_indicators\\": 1,\\n    \\"regional_indicators\\": 3,\\n    \\"measurable_achievements\\": 2,\\n    \\"no_table_usage\\": 2,\\n    \\"certifications\\": 0,\\n    \\"industry_lingo\\": 1,\\n    \\"contact_info_and_links\\": 2\\n  },\\n  \\"reasoning\\": \\"The resume demonstrates some relevant skills like Docker and CI/CD, and experience with cloud platforms (Azure). However, it lacks explicit mention of key DevOps concepts like infrastructure as code (Terraform/Ansible), networking fundamentals (CCNA, TCP/IP), Linux/Windows system administration, monitoring tools (Grafana/Prometheus), and virtualization technologies. The focus is more on backend development than DevOps practices. The resume is tailored more towards a software engineer role than a DevOps role.\\"\\n}\\n```"
                      }
                    ],
                    "role": "model"
                  },
                  "finishReason": "STOP",
                  "avgLogprobs": -0.0744597808174465
                }
              ],
              "usageMetadata": {
                "promptTokenCount": 1519,
                "candidatesTokenCount": 460,
                "totalTokenCount": 1979,
                "promptTokensDetails": [
                  {
                    "modality": "TEXT",
                    "tokenCount": 1519
                  }
                ],
                "candidatesTokensDetails": [
                  {
                    "modality": "TEXT",
                    "tokenCount": 460
                  }
                ]
              },
              "modelVersion": "gemini-2.0-flash",
              "responseId": "Ubl4aMGfAbW3mNAP3Pv2wA4"
            }
            """;


    @Test
    void upload() throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Test Resume Content");
        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        MockMultipartFile file = new MockMultipartFile(
            "file",                   // parameter name
            "test-resume.pdf",      // original filename
            "application/pdf",      // content type
            baos.toByteArray()
        );

        // Perform the upload request
        MvcResult apiResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/resume/upload")
                .file(file)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNumber()).andReturn();

        ApiResponse apiResponse = objectMapper.readValue(apiResult.getResponse().getContentAsString(), ApiResponse.class);

        UserResume userResume = userResumeRepository.findById(Long.valueOf((Integer) apiResponse.getData())).orElse(null);
        assertNotNull(userResume, "UserResume should not be null");
        assertNotNull(userResume.getUser(), "User should not be null");
        assertEquals("Test Resume Content", userResume.getContent().trim(), "Resume content should match");
    }

    @AfterEach
    void tearDown() {
        testUtils.clearDatabase();
    }

    @Test
    void measureResumeStrength() {
        testDataFactory.createJob("Test_Job", "This is a test job description.","https://example.com/job/test-job",true);
        Mockito.lenient().when(geminiClient.getResponse(Mockito.anyString())).thenReturn(GEMINI_RESPONSE);
        try{
            upload();
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/resume/me-vs-job")
                            .param("job_id", "Test_Job")
                            .header("Authorization", "Bearer " + jwtToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.score").value(45));
        }catch (Exception e){
            fail("Resume upload failed: " + e.getMessage());
        }
    }
}