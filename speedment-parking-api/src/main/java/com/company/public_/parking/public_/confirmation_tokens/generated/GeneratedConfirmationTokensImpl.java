package com.company.public_.parking.public_.confirmation_tokens.generated;

import com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokens;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.common.function.OptionalBoolean;
import com.speedment.runtime.core.util.OptionalUtil;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.StringJoiner;

/**
 * The generated base implementation of the {@link
 * com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokens}-interface.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedConfirmationTokensImpl implements ConfirmationTokens {
    
    private long tokenId;
    private Boolean claimed;
    private String confirmationToken;
    private Timestamp createdAt;
    private Timestamp expirationDate;
    private Integer operationType;
    private String uid;
    
    protected GeneratedConfirmationTokensImpl() {}
    
    @Override
    public long getTokenId() {
        return tokenId;
    }
    
    @Override
    public OptionalBoolean getClaimed() {
        return OptionalUtil.ofNullable(claimed);
    }
    
    @Override
    public Optional<String> getConfirmationToken() {
        return Optional.ofNullable(confirmationToken);
    }
    
    @Override
    public Optional<Timestamp> getCreatedAt() {
        return Optional.ofNullable(createdAt);
    }
    
    @Override
    public Optional<Timestamp> getExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }
    
    @Override
    public OptionalInt getOperationType() {
        return OptionalUtil.ofNullable(operationType);
    }
    
    @Override
    public String getUid() {
        return uid;
    }
    
    @Override
    public ConfirmationTokens setTokenId(long tokenId) {
        this.tokenId = tokenId;
        return this;
    }
    
    @Override
    public ConfirmationTokens setClaimed(Boolean claimed) {
        this.claimed = claimed;
        return this;
    }
    
    @Override
    public ConfirmationTokens setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
        return this;
    }
    
    @Override
    public ConfirmationTokens setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
    public ConfirmationTokens setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }
    
    @Override
    public ConfirmationTokens setOperationType(Integer operationType) {
        this.operationType = operationType;
        return this;
    }
    
    @Override
    public ConfirmationTokens setUid(String uid) {
        this.uid = uid;
        return this;
    }
    
    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(", ", "{ ", " }");
        sj.add("tokenId = "           + Objects.toString(getTokenId()));
        sj.add("claimed = "           + Objects.toString(OptionalUtil.unwrap(getClaimed())));
        sj.add("confirmationToken = " + Objects.toString(OptionalUtil.unwrap(getConfirmationToken())));
        sj.add("createdAt = "         + Objects.toString(OptionalUtil.unwrap(getCreatedAt())));
        sj.add("expirationDate = "    + Objects.toString(OptionalUtil.unwrap(getExpirationDate())));
        sj.add("operationType = "     + Objects.toString(OptionalUtil.unwrap(getOperationType())));
        sj.add("uid = "               + Objects.toString(getUid()));
        return "ConfirmationTokensImpl " + sj.toString();
    }
    
    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (!(that instanceof ConfirmationTokens)) { return false; }
        final ConfirmationTokens thatConfirmationTokens = (ConfirmationTokens)that;
        if (this.getTokenId() != thatConfirmationTokens.getTokenId()) { return false; }
        if (!Objects.equals(this.getClaimed(), thatConfirmationTokens.getClaimed())) { return false; }
        if (!Objects.equals(this.getConfirmationToken(), thatConfirmationTokens.getConfirmationToken())) { return false; }
        if (!Objects.equals(this.getCreatedAt(), thatConfirmationTokens.getCreatedAt())) { return false; }
        if (!Objects.equals(this.getExpirationDate(), thatConfirmationTokens.getExpirationDate())) { return false; }
        if (!Objects.equals(this.getOperationType(), thatConfirmationTokens.getOperationType())) { return false; }
        if (!Objects.equals(this.getUid(), thatConfirmationTokens.getUid())) { return false; }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Long.hashCode(getTokenId());
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getClaimed()));
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getConfirmationToken()));
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getCreatedAt()));
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getExpirationDate()));
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getOperationType()));
        hash = 31 * hash + Objects.hashCode(getUid());
        return hash;
    }
}