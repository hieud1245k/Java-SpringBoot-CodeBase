package com.fcn.fanscoin.service;

import com.fcn.fanscoin.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Slf4j
@Service
public class MailService {

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";
    private final JHipsterProperties jHipsterProperties;
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    public MailService(final JHipsterProperties jHipsterProperties,
                       final JavaMailSender javaMailSender,
                       final MessageSource messageSource,
                       final SpringTemplateEngine templateEngine) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(final String to,
                          final String subject,
                          final String content,
                          final boolean isMultipart,
                          final boolean isHtml) {
        log.info("Send email[multipart #{} and html #{}] to #{} with subject #{} and content=#{}",
                 isMultipart,
                 isHtml,
                 to,
                 subject,
                 content);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Sent email to User #{}", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user #{}", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(final User user,
                                      final String templateName,
                                      final String titleKey) {
        if (user.getEmail() == null) {
            log.info("Email doesn't exist for user #{}", user.getId());
            return;
        }
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
}
