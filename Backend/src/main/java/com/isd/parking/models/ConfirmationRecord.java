package com.isd.parking.models;

import com.isd.parking.security.AccountConfirmationPeriods;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Entity to store user registration or password reset action in database
 */
@Entity(name = "confirmation_tokens")
@Data
@RequiredArgsConstructor
public class ConfirmationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private long tokenId;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(nullable = false, name = "uid")
    private String uid;

    @Column(name = "operation_type")
    private AccountOperation operationType;

    @Column(name = "claimed")
    private boolean claimed;

    public ConfirmationRecord(String uid, AccountOperation operationType) {
        this.uid = uid;
        createdAt = LocalDateTime.now();
        expirationDate = createdAt.plusMinutes(AccountConfirmationPeriods.CONFIRM_TOKEN_EXP_IN_MINUTES);
        confirmationToken = UUID.randomUUID().toString();
        this.operationType = operationType;
        this.claimed = false;
    }
}
