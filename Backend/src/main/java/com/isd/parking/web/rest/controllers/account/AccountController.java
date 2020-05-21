package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.models.AccountOperation;
import com.isd.parking.models.ConfirmationRecord;
import com.isd.parking.models.enums.AccountState;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import com.isd.parking.services.RestService;
import com.isd.parking.services.implementations.ConfirmationServiceImpl;
import com.isd.parking.services.implementations.EmailSenderServiceImpl;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.utilities.AppDateUtils;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.ActionSuccessResponse;
import com.isd.parking.web.rest.payload.account.ForgotPassRequest;
import com.isd.parking.web.rest.payload.account.RecaptchaResponse;
import com.isd.parking.web.rest.payload.account.ResetPasswordRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.isd.parking.storage.ldap.LdapConstants.USER_ACCOUNT_STATE_ATTRIBUTE;
import static com.isd.parking.utilities.AppDateUtils.isDateBeforeNow;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Provides methods for
 * - Google captcha processing,
 * - user account confirmation
 * - user account forgot and reset password handle
 */
@Api(value = "Account Controller",
    description = "Operations pertaining to user account")
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class AccountController {

    private final UserServiceImpl userService;

    private final RestService restService;

    private final ConfirmationServiceImpl confirmationTokenService;

    private final EmailSenderServiceImpl emailSenderService;

    @Autowired
    public AccountController(UserServiceImpl userService,
                             RestService restService,
                             ConfirmationServiceImpl confirmationTokenService,
                             EmailSenderServiceImpl emailSenderService) {
        this.userService = userService;
        this.restService = restService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
    }

    /**
     * Handles Google re-captcha flow
     *
     * @param gRecaptcha - captcha data from client web application
     * @return HTTP response with user validation error or success details
     * sample message
     * {
     * "success": true,
     * "challenge_ts": "2020-03-07T13:40:25Z",
     * "hostname": "localhost"
     * }
     */
    @ApiOperation(value = "${AccountController.gRecaptcha.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.gRecaptcha.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Token is empty or invalid")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "gRecaptcha",
            value = "${AccountController.gRecaptcha.gRecaptcha}",
            required = true, dataType = "string")
    )
    @ResponseBody
    @RequestMapping(ApiEndpoints.validateCaptcha)
    public @NotNull ResponseEntity<?> gRecaptcha(@RequestBody String gRecaptcha) {
        final String grecaptchaToken = new JSONObject(gRecaptcha)
            .getJSONObject("grecaptcha")
            .getString("token");

        if (grecaptchaToken == null || grecaptchaToken.equals("")) {
            return ResponseEntity.ok(RecaptchaResponse.builder()
                .success(false)
                .challenge_ts(new Date(System.currentTimeMillis()))
                .hostname("localhost")
                .message("Token is empty or invalid").build());
        } else {
            @NotNull final String secretKey = RecaptchaResponse.GoogleRecaptchaConstants.SECRET_KEY;
            final String url = String.format(RecaptchaResponse.GoogleRecaptchaConstants.GRECAPTCHA_API_URL,
                secretKey, grecaptchaToken);
            // send captcha data to Google service for validation
            String response = restService.getPlainJSON(url);

            assert response != null;
            return ResponseEntity.ok(response);
        }
    }

    /**
     * User account confirmation handler
     *
     * @param confirmRequest - user account confirmation token
     * @return HTTP response with user validation error or success details
     */
    @ApiOperation(value = "${AccountController.confirmUserAccount.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.confirmUserAccount.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Confirmation token is expired. Register again")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "confirmRequest",
            value = "${AccountController.confirmUserAccount.confirmationToken}",
            required = true, dataType = "string")
    )
    @RequestMapping(value = ApiEndpoints.confirmAction, method = POST)
    public @NotNull ResponseEntity<?> confirmUserAccount(@RequestBody String confirmRequest) {

        final String confirmationToken = new JSONObject(confirmRequest).getString("confirmationToken");
        Optional<ConfirmationRecord> optionalConfirmationToken =
            confirmationTokenService.findByConfirmationToken(confirmationToken);

        // if this confirmation token exists in database
        if (optionalConfirmationToken.isPresent()) {
            @NotNull final ConfirmationRecord token = optionalConfirmationToken.get();

            // if token was not expired
            if (confirmationTokenService.assertNotExpired(token)) {
                // if token was not used
                if (!token.isClaimed()) {
                    final UserLdap user = userService.findById(optionalConfirmationToken.get().getUid());
                    if (token.getOperationType() == AccountOperation.ACCOUNT_CONFIRMATION) {
                        assert user != null;
                        userService.updateUser(user.getUid(), USER_ACCOUNT_STATE_ATTRIBUTE,
                            String.valueOf(AccountState.ENABLED));
                    }
                    token.setClaimed(true);
                    confirmationTokenService.save(token);

                    return ResponseEntity.ok(new ActionSuccessResponse(true));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Confirmation token is already used. Login or register again");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Confirmation token is expired. Register again");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Confirmation token not found. Register again");
        }
    }

    /**
     * User forgot password handler receives the user address and send an reset password email
     *
     * @param request - forgot password initial request contains user email and device data for set email language
     * @return HTTP response with user forgot password initial processing error or success email send details
     * @throws IOException
     * @throws MessagingException
     */
    @ApiOperation(value = "${AccountController.forgotUserPassword.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.forgotUserPassword.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Password reset request is allowed only in specified period")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "request",
            value = "${AccountController.forgotUserPassword.forgotPassRequest}",
            required = true, dataType = "ForgotPassRequest")
    )
    @RequestMapping(value = ApiEndpoints.forgotPassword, method = POST)
    public @NotNull ResponseEntity<?> forgotUserPassword(@RequestBody @NotNull ForgotPassRequest request)
        throws IOException, MessagingException {

        final String userEmail = request.getEmail();
        final UserLdap existingUser = userService.getUserByEmail(userEmail);

        if (existingUser != null) {
            Optional<ConfirmationRecord> lastConfirmationToken =
                confirmationTokenService.findLastByUsername(existingUser.getUid());

            if (lastConfirmationToken.isPresent()) {
                @NotNull ConfirmationRecord token = lastConfirmationToken.get();

                // check if user password reset is allowed
                if (!confirmationTokenService.assertValidForRepeat(token)) {
                    @NotNull String periodEnding = AppDateUtils.getPeriodEnding(
                        AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Password reset request is allowed only " + periodEnding);
                }
            }
            // Create token
            @NotNull ConfirmationRecord confirmationRecord = new ConfirmationRecord(existingUser.getUid(),
                AccountOperation.PASSWORD_RESET);
            // Save it
            confirmationTokenService.save(confirmationRecord);
            // Send email
            emailSenderService.sendPassResetMail(existingUser, confirmationRecord, request.getDeviceInfo());

            return ResponseEntity.ok(new ActionSuccessResponse(true));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User with this email doesn't exists on the server");
        }
    }

    /**
     * Endpoint to reset a user's password
     *
     * @param resetPasswordRequest - request contains all necessary user data - confirmation token
     *                             for retrieve user uid and check validation period,
     *                             new password and device information
     * @return HTTP response with user password reset processing error or success details
     */
    @ApiOperation(value = "${AccountController.resetUserPassword.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.resetUserPassword.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Password change is allowed only in specified period")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "resetPasswordRequest",
            value = "${AccountController.resetUserPassword.forgotPassRequest}",
            required = true, dataType = "ResetPasswordRequest")
    )
    @RequestMapping(value = ApiEndpoints.resetPassword, method = POST)
    public @NotNull ResponseEntity<?> resetUserPassword(@RequestBody @NotNull ResetPasswordRequest resetPasswordRequest) {

        final String confirmationToken = resetPasswordRequest.getResetDetails().getConfirmationToken();
        final String password = resetPasswordRequest.getResetDetails().getPassword();

        Optional<ConfirmationRecord> optionalConfirmationToken =
            confirmationTokenService.findByConfirmationToken(confirmationToken);

        if (optionalConfirmationToken.isPresent()) {
            @NotNull ConfirmationRecord token = optionalConfirmationToken.get();

            if (confirmationTokenService.assertNotExpired(token)) {
                final String username = token.getUid();
                if (username != null) {
                    // Use username to find user
                    final UserLdap user = userService.findById(username);
                    if (user != null) {
                        LocalDateTime passwordUpdatedAt = userService.getPasswordUpdateAt(user.getUid());
                        // check if password change is allowed
                        assert passwordUpdatedAt != null;
                        if (assertPassValidForChange(passwordUpdatedAt)) {
                            userService.updateUserPassword(user, new CustomBcryptPasswordEncoder().encode(
                                password));

                            return ResponseEntity.ok(new ActionSuccessResponse(true));
                        } else {
                            @NotNull final String periodEnding = AppDateUtils.getPeriodEnding(
                                AccountConfirmationPeriods.RESET_PASSWORD_PERIOD_IN_DAYS);
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Password change is allowed only " + periodEnding);
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("User with this username doesn't exists on the server");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Confirmation token with username empty parameter");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Confirmation token is expired. Request again");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Confirmation token not found. Request again");
        }
    }


    /**
     * Checks if password is allowed for changing conform password reset period in days
     *
     * @param passwordUpdatedAt - password lats updated date
     * @return operation result
     */
    private boolean assertPassValidForChange(@NotNull LocalDateTime passwordUpdatedAt) {
        return isDateBeforeNow(passwordUpdatedAt.plusDays(
            AccountConfirmationPeriods.RESET_PASSWORD_PERIOD_IN_DAYS),
            AccountConfirmationPeriods.MAX_CLOCK_SKEW_MINUTES);
    }
}
