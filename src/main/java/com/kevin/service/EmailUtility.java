package com.kevin.service;

// This class will be used to provide email information to the users
// It will be mainly used for recruitements, sign up, offers interactions and dismissals

import com.kevin.config.EmailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailUtility {

    @Autowired
    private EmailConfig emailConfiguration;

    private static final Logger logger = LoggerFactory.getLogger(EmailUtility.class);

    // Email type
    // We will have different set of entities to establish the user and companies interactions

    public static String[] USER_CREATION = {
            "You just created and account, Welcome to Stamps",
            "We are really happy for you to join the platform that makes clocking in and out so seamless you will love " +
                    "it.",
    };

    public static String[] COMPANY_CREATION = {
            "You just created and account, Welcome to Stamps",
            "We are really happy for you to join the platform that makes clocking in and out so seamless you will love " +
                    "it.",
    };

    public static String[] JOB_OFFER = {
            "You just created and account, Welcome to Stamps",
            "We are really happy for you to join the platform that makes clocking in and out so seamless you will love " +
                    "it.",
    };

    public static String[] JOB_REFUSE = {
            "You just created and account, Welcome to Stamps",
            "We are really happy for you to join the platform that makes clocking in and out so seamless you will love " +
                    "it.",
    };

    public static String[] JOB_ACCEPT = {
            "You just created and account, Welcome to Stamps",
            "We are really happy for you to join the platform that makes clocking in and out so seamless you will love " +
                    "it.",
    };

    public static String[] JOB_DISMISSAL = {
            "You just created and account, Welcome to Stamps",
            "We are really happy for you to join the platform that makes clocking in and out so seamless you will love " +
                    "it.",
    };

    // Email format verification
    public static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Bean
    private JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(emailConfiguration.getHost());
        mailSenderImpl.setPort(emailConfiguration.getPort());
        mailSenderImpl.setUsername(emailConfiguration.getUsername());
        mailSenderImpl.setPassword(emailConfiguration.getPassword());
        return mailSenderImpl;
    }

    public void sendSimpleMessage(String to, String from, String [] email_content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(email_content[0]);
        message.setText(email_content[1]);
        message.setFrom(from);
        getJavaMailSender().send(message);
        logger.info("Email delivered successfully to "+to+" at "+ LocalDateTime.now());
    }
}


