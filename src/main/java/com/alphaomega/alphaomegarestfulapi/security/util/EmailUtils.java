package com.alphaomega.alphaomegarestfulapi.security.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailUtils {

    private JavaMailSender javaMailSender;

    public EmailUtils(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public Integer generateOtp() {
        Random random = new Random();
        return  100000 + random.nextInt(900000);
    }

    public String htmlBodyOrderSuccess() {
        String text = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f2f2f2;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 500px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 40px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            font-size: 24px;\n" +
                "            margin-top: 0;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            color: #777;\n" +
                "            margin-bottom: 20px;\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "\n" +
                "        .success-image {\n" +
                "            margin-bottom: 20px;\n" +
                "            max-width: 100%;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            background-color: #ff6b6b;\n" +
                "            color: #fff;\n" +
                "            padding: 12px 24px;\n" +
                "            border-radius: 4px;\n" +
                "            text-decoration: none;\n" +
                "            transition: background-color 0.3s;\n" +
                "            font-size: 18px;\n" +
                "        }\n" +
                "\n" +
                "        .button:hover {\n" +
                "            background-color: #ff5252;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Payment Successful!</h1>\n" +
                "        <img src=\"https://i.ibb.co/wwcrddx/ezgif-com-gif-maker.gif\" alt=\"Payment Success\" class=\"success-image\">\n" +
                "        <p>Thank you for subscribing to our premium educational platform. Your payment has been successfully processed.</p>\n" +
                "        <p>Start your learning journey today and explore a world of knowledge!</p>\n" +
                "        <a href=\"#\" class=\"button\">Access Courses</a>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

        return text;
    }

    public String htmlBody(String name, String code) {
        String text = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Verification Code</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style type=\"text/css\">\n" +
                "      /* Font styles */\n" +
                "      @import url('https://fonts.googleapis.com/css?family=Roboto:400,700&display=swap');\n" +
                "      body {\n" +
                "        font-family: 'Roboto', sans-serif;\n" +
                "        font-size: 16px;\n" +
                "        line-height: 1.6;\n" +
                "        color: #333333;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "      /* Container */\n" +
                "      .container {\n" +
                "        background-color: #ffffff;\n" +
                "        border-radius: 5px;\n" +
                "        box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.05);\n" +
                "        margin: 0 auto;\n" +
                "        max-width: 600px;\n" +
                "        padding: 40px;\n" +
                "      }\n" +
                "      /* Logo */\n" +
                "      .logo {\n" +
                "        text-align: center;\n" +
                "        margin-bottom: 20px;\n" +
                "      }\n" +
                "      .logo img {\n" +
                "        height: 50px;\n" +
                "      }\n" +
                "      /* Heading */\n" +
                "      .heading {\n" +
                "        text-align: center;\n" +
                "        font-size: 24px;\n" +
                "        font-weight: 700;\n" +
                "        margin-bottom: 20px;\n" +
                "      }\n" +
                "      /* Paragraph */\n" +
                "      .paragraph {\n" +
                "        margin-bottom: 20px;\n" +
                "      }\n" +
                "      /* Verification Code */\n" +
                "      .verification-code {\n" +
                "        background-color: #f2f2f2;\n" +
                "        border: 1px solid #d9d9d9;\n" +
                "        border-radius: 5px;\n" +
                "        color: #333333;\n" +
                "        display: inline-block;\n" +
                "        font-size: 30px;\n" +
                "        font-weight: 700;\n" +
                "        padding: 10px 20px;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <div class=\"logo\">\n" +
                "        <img src=\"https://i.ibb.co/5FDrYYx/Daco-6045133.png\" alt=\"Logo\">\n" +
                "      </div>\n" +
                "      <div class=\"heading\">\n" +
                "        Verification Code\n" +
                "      </div>\n" +
                "      <div class=\"paragraph\">\n" +
                name.concat(",<br>\n") +
                "        Here is your verification code:\n" +
                "      </div>\n" +
                "      <div class=\"verification-code\">\n" +
                code.concat("\n") +
                "      </div>\n" +
                "      <div class=\"paragraph\">\n" +
                "        Please use this code to verify your email address.\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>";
        return text;
    }

    public void sendEmail(String to, String name, String subject, Integer code) throws MessagingException {
        String text = htmlBody(name, code.toString());
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        javaMailSender.send(message);
    }

    public void sendEmailOrderSuccess(String to) throws MessagingException{
        String text = htmlBodyOrderSuccess();
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Thank You! Your Payment has been Successfully Processed");
        helper.setText(text, true);

        javaMailSender.send(message);
    }


}
