package com.example.Notificationservice.Controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Notificationservice.DTO.EmailrequestDto;
import com.example.Notificationservice.EmailService.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSendEmailNotification() throws Exception {
        // Arrange: Create a mock EmailrequestDto
        EmailrequestDto emailRequest = new EmailrequestDto("test@example.com","Test Subject","Test Body");
//        emailRequest.setToEmail("test@example.com");
//        emailRequest.setSubject("Test Subject");
//        emailRequest.setBody("Test Body");

        // Mock email service to do nothing when sendEmail is called
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act & Assert: Perform POST request and verify response
        mockMvc.perform(post("/notifications/sendEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully"));

        // Verify sendEmail was called with the correct parameters
        verify(emailService).sendEmail(eq("test@example.com"), eq("Test Subject"), eq("Test Body"));
    }
}
