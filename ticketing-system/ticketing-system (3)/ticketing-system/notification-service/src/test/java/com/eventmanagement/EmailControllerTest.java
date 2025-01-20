package com.eventmanagement;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailControllerTest {

    private final JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
    private final EmailController emailController = new EmailController(mailSender);

    @Test
    void testSendEmail() {
        // Arrange
        String toEmail = "test@example.com";
        Long reservationId = 123L;

        // Act
        String result = emailController.sendEmail(toEmail, reservationId);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        assert result.equals("Email envoyé avec succès à : " + toEmail);
    }

    @Test
    void testSendEmailWithError() {
        // Arrange
        doThrow(new RuntimeException("Erreur simulée")).when(mailSender).send(any(SimpleMailMessage.class));

        String toEmail = "test@example.com";
        Long reservationId = 123L;

        // Act
        String result = emailController.sendEmail(toEmail, reservationId);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        assert result.startsWith("Erreur lors de l'envoi de l'email");
    }

    @Test
    void testSendEmailWithAttachment() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        doNothing().when(mailSender).send(mimeMessage);

        // Act
        String result = emailController.sendEmailWithAttachment();

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        assert result.equals("success!");
    }

    @Test
    void testSendEmailWithAttachmentError() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(new RuntimeException("Erreur simulée")).when(mailSender).send(mimeMessage);

        // Act
        String result = emailController.sendEmailWithAttachment();

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        assert result.equals("Erreur simulée");
    }

}