package com.isd.parking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isd.parking.models.EmailDto;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.security.model.AccountOperation;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.security.model.payload.register.DeviceInfo;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.isd.parking.controller.ApiEndpoints.confirmAction;
import static com.isd.parking.controller.ApiEndpoints.confirmReset;
import static com.isd.parking.utils.ColorConsoleOutput.methodMsgStatic;


@Service("emailSenderService")
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final ColorConsoleOutput console;

    @Value("${spring.mail.from.email}")
    private String from;

    @Value("${front.url}")
    private String frontUrl;

    @Autowired
    public EmailSenderService(@Qualifier("gmail") JavaMailSender mailSender, ColorConsoleOutput console,
                              @Qualifier("templateEngine") TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.console = console;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendRegistrationConfirmMail(@NotBlank @NonNull UserLdap emailUser,
                                            @NotBlank @NonNull ConfirmationToken confirmationToken,
                                            @NotBlank @NonNull DeviceInfo deviceInfo) throws IOException, MessagingException {

        final String subject = "Complete Registration!";
        final String templateName = "confirm_account_mail.html";

        // https://github.com/dotSwapna/dotEmail.github.io/blob/master/src/main/java/dot/demo/email/EmailerService.java
        EmailDto emailDto = createHtmlEmail(emailUser, confirmationToken, deviceInfo, subject, templateName);

        sendHtmlEmail(emailDto);
    }

    @Async
    public void sendPassResetMail(@NotBlank @NonNull UserLdap emailUser,
                                  @NotBlank @NonNull ConfirmationToken confirmationToken,
                                  @NotBlank @NonNull DeviceInfo deviceInfo) throws IOException, MessagingException {

        final String subject = "Complete Password Reset!";
        final String templateName = "reset_password_mail.html";

        EmailDto emailDto = createHtmlEmail(emailUser, confirmationToken, deviceInfo, subject, templateName);

        sendHtmlEmail(emailDto);
    }

    private EmailDto createHtmlEmail(@NotBlank @NonNull UserLdap emailUser, @NotBlank @NonNull ConfirmationToken confirmationToken, @NotBlank @NonNull DeviceInfo deviceInfo, String subject, String templateName) {
        EmailDto emailDto = createEmailDtoFromUserLdap(emailUser, subject, templateName);
        HashMap<String, Object> parametersMap = createTemplateParametersMap(emailUser, confirmationToken, deviceInfo);
        emailDto.setParameterMap(parametersMap);
        log.info(methodMsgStatic(" EmailDto: " + emailDto));
        return emailDto;
    }

    private HashMap<String, Object> createTemplateParametersMap(@NotBlank @NonNull UserLdap emailUser, @NotBlank @NonNull ConfirmationToken confirmationToken, @NotBlank @NonNull DeviceInfo deviceInfo) {
        ObjectMapper oMapper = new ObjectMapper();
        HashMap<String, Object> deviceInfoMap = (HashMap<String, Object>) oMapper.convertValue(deviceInfo, Map.class);
        HashMap<String, Object> parametersMap = new HashMap<>(deviceInfoMap);

        String username = emailUser.getUid().substring(0, 1).toUpperCase() + emailUser.getUid().substring(1);
        parametersMap.put("username", username);

        String path = confirmationToken.getOperationType() == AccountOperation.ACCOUNT_CONFIRMATION ? confirmAction : confirmReset;
        String confirmLink = frontUrl + path + "?confirmation_token="
            + confirmationToken.getConfirmationToken();
        parametersMap.put("confirmation_link", confirmLink);
        parametersMap.put("confirmation_token_valid_period", AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES);
        parametersMap.put("email", emailUser.getEmail());
        return parametersMap;
    }

    private EmailDto createEmailDtoFromUserLdap(@NotBlank @NonNull UserLdap emailUser, String subject, String templateName) {
        EmailDto emailDto = new EmailDto();

        emailDto.setTo(new String[]{emailUser.getEmail()});
        emailDto.setFrom(from);
        emailDto.setSubject(subject);
        emailDto.setHtml(true);
        emailDto.setTemplateName(templateName);
        return emailDto;
    }

    private SimpleMailMessage createSimpleMailMessage(UserLdap emailUser, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailUser.getEmail());
        mailMessage.setFrom(from);
        return mailMessage;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        email.setFrom(from);
        mailSender.send(email);
    }

    /**
     * Send emails using templates in web/templates/email/ directory
     *
     * @param emailDto
     * @return EmailDto
     * @throws MessagingException
     * @throws IOException
     */
    public EmailDto sendEmail(EmailDto emailDto) throws MessagingException, IOException {

        // Prepare the evaluation context
        Context ctx = prepareContext(emailDto);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        // Prepare message using a Spring helper
        MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);

        // Create the HTML body using Thymeleaf
        String htmlContent = this.templateEngine.process(emailDto.getTemplateName(), ctx);
        message.setText(htmlContent, true /* isHtml */);
        emailDto.setEmailedMessage(htmlContent);

        log.info("Processing email request: " + emailDto.toString());

        message = prepareStaticResources(message, emailDto);

        // Send mail
        this.mailSender.send(mimeMessage);

        this.templateEngine.clearTemplateCache();

        return emailDto;

    }

    /**
     * Send email using Text template
     *
     * @param emailDto
     * @return EmailDto
     * @throws IOException
     * @throws MessagingException
     */
    public EmailDto sendTextTemplateEmail(EmailDto emailDto)
        throws IOException, MessagingException {

        // Prepare email context
        Context ctx = prepareContext(emailDto);

        // Prepare message
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        // Prepare message using a Spring helper
        MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);
        // Create email message using TEXT template
        String textContent = this.templateEngine.process(emailDto.getTemplateName(), ctx); // text/email-text\"

        emailDto.setEmailedMessage(textContent);
        message.setText(textContent);

        // Send email
        this.mailSender.send(mimeMessage);

        return emailDto;

    }

    /**
     * Send email with html template found in classpath resource
     *
     * @param emailDto
     * @return EmailDto
     * @throws MessagingException
     * @throws IOException
     */
    public EmailDto sendHtmlEmail(EmailDto emailDto) throws MessagingException, IOException {
        // Prepare the evaluation context
        Context ctx = prepareContext(emailDto);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);

        // Create the HTML body using Thymeleaf
        String htmlContent = this.templateEngine.process(emailDto.getTemplateName(), ctx);
        message.setText(htmlContent, true /* isHtml */);
        emailDto.setEmailedMessage(htmlContent);

        log.info("Processing html email request: " + emailDto.toString());

        message = prepareStaticResources(message, emailDto);

        // Send mail
        this.mailSender.send(mimeMessage);

        this.templateEngine.clearTemplateCache();

        return emailDto;

    }


    /**
     * Send multiple emails using templates in web/templates/email/ directory
     *
     * @param emailDtos
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public List<EmailDto> sendEmails(List<EmailDto> emailDtos) throws MessagingException, IOException {

        List<MimeMessage> mimeMessages = new ArrayList<>();
        MimeMessage mimeMessage;

        for (EmailDto emailDto : emailDtos) {

            // Prepare the evaluation context
            final Context ctx = prepareContext(emailDto);

            // Prepare message using a Spring helper
            mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);

            // Create the HTML body using Thymeleaf
            String htmlContent = this.templateEngine.process(emailDto.getTemplateName(), ctx);
            message.setText(htmlContent, true /* isHtml */);
            emailDto.setEmailedMessage(htmlContent);

            log.info("Processing emails request: " + emailDto.toString());

            message = prepareStaticResources(message, emailDto);

            mimeMessages.add(mimeMessage);
        }

        // Send mail
        this.mailSender.send(mimeMessages.toArray(new MimeMessage[0]));

        this.templateEngine.clearTemplateCache();

        return emailDtos;

    }

    private MimeMessageHelper prepareMessage(MimeMessage mimeMessage, EmailDto emailDto)
        throws MessagingException, IOException {

        // Prepare message using a Spring helper
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            "UTF-8");
        message.setSubject(emailDto.getSubject());
        message.setFrom(emailDto.getFrom());
        message.setTo(emailDto.getTo());

        if (emailDto.getCc() != null && emailDto.getCc().length != 0) {
            message.setCc(emailDto.getCc());
        }

        if (emailDto.getBcc() != null && emailDto.getBcc().length != 0) {
            message.setBcc(emailDto.getBcc());
        }

        if (emailDto.isHasAttachment()) {
            List<File> attachments = loadResources(
                emailDto.getPathToAttachment() + "/*" + emailDto.getAttachmentName() + "*.*");
            for (File file : attachments) {
                message.addAttachment(file.getName(), file);
            }
        }

        return message;

    }

    private List<File> loadResources(String fileNamePattern) throws IOException {
        PathMatchingResourcePatternResolver fileResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;

        try {
            resources = fileResolver.getResources("file:" + fileNamePattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<File> attachFiles = new ArrayList<>();

        assert resources != null;
        for (Resource resource : resources) {
            attachFiles.add(resource.getFile());
        }

        return attachFiles;

    }

    private Context prepareContext(EmailDto emailDto) {
        // Prepare the evaluation context
        Context ctx = new Context();

        final Map<String, Object> parameterMap = emailDto.getParameterMap();
        if (parameterMap != null) {
            Set<String> keySet = parameterMap.keySet();
            log.info(methodMsgStatic(" keySet: " + keySet));
            if (!keySet.isEmpty()) {
                keySet.forEach(s -> ctx.setVariable(s, parameterMap.get(s)));
            }
        }

        log.info(methodMsgStatic(" resKeySet: "));
        Set<String> resKeySet = emailDto.getStaticResourceMap().keySet();
        if (!resKeySet.isEmpty()) {
            resKeySet.forEach(s -> {
                ctx.setVariable(s, emailDto.getStaticResourceMap().get(s));
            });
        }

        log.info(methodMsgStatic(" Context: " + ctx));

        return ctx;
    }

    private MimeMessageHelper prepareStaticResources(MimeMessageHelper message,
                                                     EmailDto emailDto) throws MessagingException {
        Map<String, Object> staticResources = emailDto.getStaticResourceMap();

        for (Map.Entry<String, Object> entry : staticResources.entrySet()) {

            ClassPathResource imageSource =
                new ClassPathResource("static/" + entry.getValue());
            message.addInline(entry.getKey(), imageSource, "image/png");
            message.addInline((String) entry.getValue(), imageSource, "image/png");

        }

        return message;
    }
}
