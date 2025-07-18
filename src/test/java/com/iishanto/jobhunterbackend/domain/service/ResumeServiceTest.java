package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@ExtendWith(MockitoExtension.class)
class ResumeServiceTest {
    @Mock
    private UserDataAdapter userDataAdapter;
    @InjectMocks
    private ResumeService resumeService;
    @Test
    void uploadResume() {
        Mockito.when(userDataAdapter.saveResume(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(1L);
        try {
            Long resumeId = resumeService.uploadResume(getDummyPdfInputStream("Test pdf content"), "application/pdf");
            assertNotNull(resumeId);
            assertEquals(1L, resumeId.longValue());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void uploadResume_When_No_Text(){
        assertThrows(IllegalArgumentException.class, () -> {
            resumeService.uploadResume(getDummyPdfInputStream(""), "application/pdf");
        });
    }

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(userDataAdapter.getLoggedInUser()).then(invocation -> SimpleUserModel.builder().id(1L).build());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testUploadResume() {
    }

    private InputStream getDummyPdfInputStream(String text) throws IOException {
        // Create PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Add some text content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();

        // Convert to InputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return new ByteArrayInputStream(baos.toByteArray());
    }
}