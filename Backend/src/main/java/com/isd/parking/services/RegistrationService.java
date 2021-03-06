package com.isd.parking.services;


import com.isd.parking.config.locale.SmartLocaleResolver;
import com.isd.parking.models.AccountOperation;
import com.isd.parking.models.ConfirmationRecord;
import com.isd.parking.models.users.User;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.models.users.UserMapper;
import com.isd.parking.services.implementations.ConfirmationServiceImpl;
import com.isd.parking.services.implementations.EmailSenderServiceImpl;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.web.rest.payload.DeviceInfo;
import com.isd.parking.web.rest.payload.ResponseEntityFactory;
import com.isd.parking.web.rest.payload.account.register.RegistrationRequest;
import com.isd.parking.web.rest.payload.account.register.RegistrationSuccessResponse;
import com.isd.parking.web.rest.payload.account.register.SocialRegisterRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static com.isd.parking.utilities.AppStringUtils.generateCommonLangPassword;


@Service
public class RegistrationService {

    private final UserServiceImpl userService;

    private final ConfirmationServiceImpl confirmationTokenService;

    private final EmailSenderServiceImpl emailSenderService;

    private final UserMapper userMapper;

    private final SmartLocaleResolver localeResolver;

    private final ResponseEntityFactory responseEntityFactory;

    @Autowired
    public RegistrationService(UserServiceImpl userService,
                               ConfirmationServiceImpl confirmationTokenService,
                               EmailSenderServiceImpl emailSenderService,
                               UserMapper userMapper,
                               SmartLocaleResolver localeResolver,
                               ResponseEntityFactory responseEntityFactory) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.userMapper = userMapper;
        this.localeResolver = localeResolver;
        this.responseEntityFactory = responseEntityFactory;
    }

    /**
     * Handles user registration in system
     *
     * @param request - registration request contains user information and device data for set email language
     * @return - HTTP response with registration error or success details
     */
    public @NotNull ResponseEntity<?> registration(@NotNull RegistrationRequest request,
                                                   Map<String, String> headers) {
        final User user = request.getUser();
        final String username = user.getUsername();
        final String email = user.getEmail();
        final Locale locale = localeResolver.resolveLocale(headers);

        //verify if user exists in DB and throw error, else create
        boolean userExists = userService.searchUser(username);
        boolean emailExists = userService.searchUsersByEmail(email);

        if (userExists) {
            return responseEntityFactory.usernameExists(locale);
        } else if (emailExists) {
            final UserLdap existedUser = userService.getUserByEmail(email);

            // check account confirmation state
            // if user exists but account confirmation is expired (did not have time or forgot to confirm)
            // temporary unconfirmed user with this email will be deleted and new one created
            assert existedUser != null;
            if (existedUser.accountConfirmationIsExpired()) {
                final UserLdap newUser = userMapper.userToUserLdap(user);
                userService.deleteUser(newUser);

                return processUserCreation(newUser, request.getDeviceInfo(), locale);
            }
            // if user exists and account confirmation link is valid
            if (existedUser.accountConfirmationValid()) {
                return responseEntityFactory.emailExistsWaiting(locale);
            }

            return responseEntityFactory.emailExists(locale);
        } else {
            // create new user
            final UserLdap newUser = userMapper.userToUserLdap(user);
            return processUserCreation(newUser, request.getDeviceInfo(), locale);
        }
    }

    /**
     * Handles user registration in system with specified social provider
     *
     * @param request - registration request contains social user information and device data for set email language
     * @return - HTTP response with registration error or success details
     */
    public @NotNull ResponseEntity<?> socialRegistration(@NotNull SocialRegisterRequest request,
                                                         Map<String, String> headers) {
        final User user = request.getUser();
        final String email = user.getEmail();
        final String id = request.getId();
        final String provider = request.getSocialProvider();
        final Locale locale = localeResolver.resolveLocale(headers);

        //verify if user exists in db and throw error, else create
        final UserLdap userFound = userService.getUserBySocialId(id, provider);
        if (userFound != null) {
            return responseEntityFactory.socialIdExists(locale);
        } else {
            if (email != null) {
                final UserLdap existedUser = userService.getUserByEmail(email);
                // same emails not allowed
                if (existedUser != null) {
                    // case when confirmation expired and confirm impossible anymore
                    if (existedUser.accountConfirmationIsExpired()) {
                        final UserLdap newUser = userMapper.userToUserLdap(user);
                        newUser.prepareSocialUser(id, provider);
                        newUser.setUserPassword(generateCommonLangPassword());

                        userService.deleteUser(newUser);
                        return processUserCreation(newUser, request.getDeviceInfo(), locale);
                    }
                    if (existedUser.accountConfirmationValid()) {
                        return responseEntityFactory.socialExistsWaiting(locale);
                    }

                    return responseEntityFactory.socialEmailExists(locale);
                }
            }
            // else create new user
            final UserLdap newUser = userMapper.userToUserLdap(user);
            newUser.prepareSocialUser(id, provider);

            return processUserCreation(newUser, request.getDeviceInfo(), locale);
        }
    }

    /**
     * Method creates new user in database and forms a message for account confirmation
     * (additional common registration part)
     *
     * @param newUser    - user to be saved in database
     * @param deviceInfo - user device information for region targeting
     * @return HTTP response with registration error or success details
     */
    private @NotNull ResponseEntity<?> processUserCreation(@NotNull UserLdap newUser, DeviceInfo deviceInfo, Locale locale) {
        userService.createUser(newUser);

        final UserLdap createdUser = userService.findById(newUser.getUid());
        if (createdUser == null) {
            return responseEntityFactory.userNotCreated(locale);
        } else {
            if (createdUser.getEmail() != null) {
                createConfirmation(createdUser, deviceInfo);
            }
            return ResponseEntity.ok(
                new RegistrationSuccessResponse(true, createdUser.getEmail() != null));
        }
    }

    /**
     * Method creates user registration confirmation
     *
     * @param createdUser - target user
     * @param deviceInfo  - user device information for region targeting
     */
    private void createConfirmation(@NotNull UserLdap createdUser, DeviceInfo deviceInfo) {
        // Create token
        @NotNull ConfirmationRecord confirmationRecord = new ConfirmationRecord(createdUser.getUid(),
            AccountOperation.ACCOUNT_CONFIRMATION);
        // Save it
        confirmationTokenService.save(confirmationRecord);
        // Send email
        try {
            emailSenderService.sendRegistrationConfirmMail(createdUser, confirmationRecord, deviceInfo);
        } catch (@NotNull IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
