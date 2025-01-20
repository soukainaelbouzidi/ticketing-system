package com.eventmanagement;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class EmailController {

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Envoi d'email dynamique
    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String toEmail, @RequestParam Long reservationId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ennasser.k927@ucd.ac.ma"); // L'adresse d'envoi
            message.setTo(toEmail); // Adresse dynamique
            message.setSubject("Confirmation de réservation réussie");
            message.setText("Votre réservation #" + reservationId + " a été confirmée avec succès. Merci pour votre confiance !");

            mailSender.send(message);
            return "Email envoyé avec succès à : " + toEmail;
        } catch (Exception e) {
            return "Erreur lors de l'envoi de l'email : " + e.getMessage();
        }
    }

    @RequestMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("tutorial.genuinecoder@gmail.com");
            helper.setTo("tutorial.genuinecoder@gmail.com");
            helper.setSubject("Java email with attachment | From GC");
            helper.setText("Please find the attached documents below");

            helper.addAttachment("logo.png", new File("C:\\Users\\Genuine Coder\\Documents\\Attachments\\logo.png"));
            helper.addAttachment("presentation.pptx", new File("C:\\Users\\Genuine Coder\\Documents\\Attachments\\presentation.pptx"));

            mailSender.send(message);
            return "success!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/send-html-email")
    public String sendHtmlEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("tutorial.genuinecoder@gmail.com");
            helper.setTo("tutorial.genuinecoder@gmail.com");
            helper.setSubject("Java email with attachment | From GC");

            try (var inputStream = Objects.requireNonNull(EmailController.class.getResourceAsStream("/templates/email-content.html"))) {
                helper.setText(
                        new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
                        true
                );
            }
            helper.addInline("logo.png", new File("C:\\Users\\Genuine Coder\\Documents\\Attachments\\logo.png"));
            mailSender.send(message);
            return "success!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}