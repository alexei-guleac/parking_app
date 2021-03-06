package com.company.public_.parking.public_.confirmation_tokens.generated;

import com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokens;
import com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokensManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.common.json.Json;
import com.speedment.enterprise.plugins.json.JsonCollectors;
import com.speedment.enterprise.plugins.json.JsonComponent;
import com.speedment.enterprise.plugins.json.JsonEncoder;
import com.speedment.enterprise.plugins.spring.runtime.AbstractFilter;
import com.speedment.enterprise.plugins.spring.runtime.AbstractSort;
import com.speedment.enterprise.plugins.spring.runtime.ControllerUtil;
import com.speedment.runtime.core.manager.FieldSet;
import com.speedment.runtime.core.manager.Persister;
import com.speedment.runtime.core.manager.Updater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;

import static java.util.stream.Collectors.toList;

/**
 * The default REST controller logic for ConfirmationTokens entities.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/public")
public abstract class GeneratedConfirmationTokensController {
    
    protected @Autowired JsonComponent jsonComponent;
    protected @Autowired ConfirmationTokensManager manager;
    protected JsonEncoder<ConfirmationTokens> encoder;
    
    @PostConstruct
    void createConfirmationTokensEncoder() {
        encoder = jsonComponent.<ConfirmationTokens>emptyEncoder()
            .put("tokenId", ConfirmationTokens.TOKEN_ID)
            .put("claimed", ConfirmationTokens.CLAIMED)
            .put("confirmationToken", ConfirmationTokens.CONFIRMATION_TOKEN)
            .put("createdAt", ConfirmationTokens.CREATED_AT)
            .put("expirationDate", ConfirmationTokens.EXPIRATION_DATE)
            .put("operationType", ConfirmationTokens.OPERATION_TYPE)
            .put("uid", ConfirmationTokens.UID)
            .build();
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/confirmation_tokens", produces = "application/json")
    public String list(
            @RequestParam(name = "filter", defaultValue = "[]") String filters,
            @RequestParam(name = "sort", defaultValue = "[]") String sorters,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "limit", defaultValue = "25") long limit) {
        
        return listHelper(
            ControllerUtil.parseFilters(filters, ConfirmationTokensFilter::new).collect(toList()),
            ControllerUtil.parseSorts(sorters, ConfirmationTokensSort::new).collect(toList()),
            start,
            limit
        );
    }
    
    protected String listHelper(
            List<Predicate<ConfirmationTokens>> predicates,
            List<Comparator<ConfirmationTokens>> sorters,
            long start,
            long limit) {
        
        Stream<ConfirmationTokens> stream      = manager.stream();
        Stream<ConfirmationTokens> totalStream = manager.stream();
        
        for (final Predicate<ConfirmationTokens> predicate : predicates) {
            stream      = stream.filter(predicate);
            totalStream = totalStream.filter(predicate);
        }
        
        if (!sorters.isEmpty()) {
            final Optional<Comparator<ConfirmationTokens>> comparator = sorters.stream()
                .reduce(Comparator::thenComparing);
            
            stream = stream.sorted(comparator.get());
        }
        
        final String total = String.valueOf(totalStream.count());
        return stream
            .skip(start)
            .limit(limit)
            .collect(jsonComponent.collector(ConfirmationTokens.class)
                .put("total", () -> total)
                .put("data", JsonCollectors.toList(encoder))
                .build()
            );
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/confirmation_tokens/{token_id}", produces = "application/json")
    public String get(
            @PathVariable(name = "token_id") long tokenId) {
        return encoder.apply(
            manager.stream()
                .filter(ConfirmationTokens.TOKEN_ID.equal(tokenId))
                .findFirst()
                .orElseThrow(() -> new ConfirmationTokensNotFoundException(tokenId))
        );
    }
    
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "/confirmation_tokens", consumes = "application/json")
    public void create(
            @RequestBody @Validated ConfirmationTokensCreateBody createBody) {
        final Persister<ConfirmationTokens> persister = manager.persister();
        final ConfirmationTokens confirmationTokens = manager.create()
            .setTokenId(createBody.getTokenId())
            .setClaimed(createBody.getClaimed())
            .setConfirmationToken(createBody.getConfirmationToken())
            .setCreatedAt(createBody.getCreatedAt())
            .setExpirationDate(createBody.getExpirationDate())
            .setOperationType(createBody.getOperationType())
            .setUid(createBody.getUid())
        ;
        
        persister.accept(confirmationTokens);
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping(path = "/confirmation_tokens/{token_id}", consumes = "application/json")
    public void update(
            @PathVariable(name = "token_id") long tokenId,
            @RequestBody @Validated ConfirmationTokensUpdateBody updateBody) {
        final FieldSet<ConfirmationTokens> excluded = FieldSet.allExcept(
            ConfirmationTokens.TOKEN_ID
        );
        
        final Updater<ConfirmationTokens> updater = manager.updater(excluded);
        final ConfirmationTokens confirmationTokens = manager.stream()
            .filter(ConfirmationTokens.TOKEN_ID.equal(tokenId))
            .findFirst()
            .orElseThrow(() -> new ConfirmationTokensNotFoundException(tokenId));
        
        confirmationTokens.setClaimed(updateBody.getClaimed());
        confirmationTokens.setConfirmationToken(updateBody.getConfirmationToken());
        confirmationTokens.setCreatedAt(updateBody.getCreatedAt());
        confirmationTokens.setExpirationDate(updateBody.getExpirationDate());
        confirmationTokens.setOperationType(updateBody.getOperationType());
        confirmationTokens.setUid(updateBody.getUid());
        
        updater.accept(confirmationTokens);
    }
    
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/confirmation_tokens/{token_id}")
    public void delete(
            @PathVariable(name = "token_id") long tokenId) {
        manager.stream()
            .filter(ConfirmationTokens.TOKEN_ID.equal(tokenId))
            .forEach(manager.remover());
    }
    
    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingValueError() {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("status", 400);
        error.put("message", "Invalid request body: missing required fields");
        
        return Json.toJson(error, true);
    }
    
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidJsonError() {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("status", 400);
        error.put("message", "Invalid request body: invalid JSON syntax");
        
        return Json.toJson(error, true);
    }
    
    /**
     * How to filter the results from the controller. This class is designed to
     * be deserialized automatically from JSON.
     */
    public static final class ConfirmationTokensFilter extends AbstractFilter<ConfirmationTokens> {
        
        public ConfirmationTokensFilter(
                String operator,
                String property,
                String value) {
            super(operator, property, value);
        }
        
        @Override
        public Predicate<ConfirmationTokens> toPredicate() {
            switch (property()) {
                case "tokenId" : {
                    final long v = Long.parseLong(value());
                    switch (operator()) {
                        case "eq"   : return ConfirmationTokens.TOKEN_ID.equal(v);
                        case "ne"   : return ConfirmationTokens.TOKEN_ID.notEqual(v);
                        case "lt"   : return ConfirmationTokens.TOKEN_ID.lessThan(v);
                        case "le"   : return ConfirmationTokens.TOKEN_ID.lessOrEqual(v);
                        case "gt"   : return ConfirmationTokens.TOKEN_ID.greaterThan(v);
                        case "ge"   : return ConfirmationTokens.TOKEN_ID.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "ConfirmationTokens.tokenId."
                        );
                    }
                }
                case "claimed" : {
                    final Boolean v = Boolean.parseBoolean(value());
                    switch (operator()) {
                        case "eq"   : return ConfirmationTokens.CLAIMED.equal(v);
                        case "ne"   : return ConfirmationTokens.CLAIMED.notEqual(v);
                        case "lt"   : return ConfirmationTokens.CLAIMED.lessThan(v);
                        case "le"   : return ConfirmationTokens.CLAIMED.lessOrEqual(v);
                        case "gt"   : return ConfirmationTokens.CLAIMED.greaterThan(v);
                        case "ge"   : return ConfirmationTokens.CLAIMED.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "ConfirmationTokens.claimed."
                        );
                    }
                }
                case "confirmationToken" : {
                    final String v = value();
                    switch (operator()) {
                        case "eq"   : return ConfirmationTokens.CONFIRMATION_TOKEN.equal(v);
                        case "ne"   : return ConfirmationTokens.CONFIRMATION_TOKEN.notEqual(v);
                        case "lt"   : return ConfirmationTokens.CONFIRMATION_TOKEN.lessThan(v);
                        case "le"   : return ConfirmationTokens.CONFIRMATION_TOKEN.lessOrEqual(v);
                        case "gt"   : return ConfirmationTokens.CONFIRMATION_TOKEN.greaterThan(v);
                        case "ge"   : return ConfirmationTokens.CONFIRMATION_TOKEN.greaterOrEqual(v);
                        case "like" : return ConfirmationTokens.CONFIRMATION_TOKEN.contains(v);
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "ConfirmationTokens.confirmationToken."
                        );
                    }
                }
                case "createdAt" : {
                    final Timestamp v = Timestamp.valueOf(value());
                    switch (operator()) {
                        case "eq"   : return ConfirmationTokens.CREATED_AT.equal(v);
                        case "ne"   : return ConfirmationTokens.CREATED_AT.notEqual(v);
                        case "lt"   : return ConfirmationTokens.CREATED_AT.lessThan(v);
                        case "le"   : return ConfirmationTokens.CREATED_AT.lessOrEqual(v);
                        case "gt"   : return ConfirmationTokens.CREATED_AT.greaterThan(v);
                        case "ge"   : return ConfirmationTokens.CREATED_AT.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "ConfirmationTokens.createdAt."
                        );
                    }
                }
                case "expirationDate" : {
                    final Timestamp v = Timestamp.valueOf(value());
                    switch (operator()) {
                        case "eq"   : return ConfirmationTokens.EXPIRATION_DATE.equal(v);
                        case "ne"   : return ConfirmationTokens.EXPIRATION_DATE.notEqual(v);
                        case "lt"   : return ConfirmationTokens.EXPIRATION_DATE.lessThan(v);
                        case "le"   : return ConfirmationTokens.EXPIRATION_DATE.lessOrEqual(v);
                        case "gt"   : return ConfirmationTokens.EXPIRATION_DATE.greaterThan(v);
                        case "ge"   : return ConfirmationTokens.EXPIRATION_DATE.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "ConfirmationTokens.expirationDate."
                        );
                    }
                }
                case "operationType" : {
                    final Integer v = Integer.parseInt(value());
                    switch (operator()) {
                        case "eq"   : return ConfirmationTokens.OPERATION_TYPE.equal(v);
                        case "ne"   : return ConfirmationTokens.OPERATION_TYPE.notEqual(v);
                        case "lt"   : return ConfirmationTokens.OPERATION_TYPE.lessThan(v);
                        case "le"   : return ConfirmationTokens.OPERATION_TYPE.lessOrEqual(v);
                        case "gt"   : return ConfirmationTokens.OPERATION_TYPE.greaterThan(v);
                        case "ge"   : return ConfirmationTokens.OPERATION_TYPE.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "ConfirmationTokens.operationType."
                        );
                    }
                }
                case "uid" : {
                    final String v = value();
                    switch (operator()) {
                        case "eq"   : return ConfirmationTokens.UID.equal(v);
                        case "ne"   : return ConfirmationTokens.UID.notEqual(v);
                        case "lt"   : return ConfirmationTokens.UID.lessThan(v);
                        case "le"   : return ConfirmationTokens.UID.lessOrEqual(v);
                        case "gt"   : return ConfirmationTokens.UID.greaterThan(v);
                        case "ge"   : return ConfirmationTokens.UID.greaterOrEqual(v);
                        case "like" : return ConfirmationTokens.UID.contains(v);
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "ConfirmationTokens.uid."
                        );
                    }
                }
                default : throw new IllegalArgumentException(
                    "'" + property() + "' is not a valid ConfirmationTokens property."
                );
            }
        }
    }
    
    /**
     * How to sort the results from the controller. This class is designed to be
     * deserialized automatically from JSON.
     */
    public static final class ConfirmationTokensSort extends AbstractSort<ConfirmationTokens> {
        
        public ConfirmationTokensSort(String property, String direction) {
            super(property, direction);
        }
        
        @Override
        public Comparator<ConfirmationTokens> toComparator() {
            final Comparator<ConfirmationTokens> comparator;
            switch (property()) {
                case "tokenId"           : comparator = ConfirmationTokens.TOKEN_ID.comparator();           break;
                case "claimed"           : comparator = ConfirmationTokens.CLAIMED.comparator();            break;
                case "confirmationToken" : comparator = ConfirmationTokens.CONFIRMATION_TOKEN.comparator(); break;
                case "createdAt"         : comparator = ConfirmationTokens.CREATED_AT.comparator();         break;
                case "expirationDate"    : comparator = ConfirmationTokens.EXPIRATION_DATE.comparator();    break;
                case "operationType"     : comparator = ConfirmationTokens.OPERATION_TYPE.comparator();     break;
                case "uid"               : comparator = ConfirmationTokens.UID.comparator();                break;
                default : throw new IllegalArgumentException(
                    "'" + property() + "' is not a valid ConfirmationTokens property."
                );
            }
            
            switch (direction()) {
                case "ASC"  : return comparator;
                case "DESC" : return comparator.reversed();
                default : throw new IllegalArgumentException(
                    "'" + direction() + "' is not a valid sort direction. " +
                    "Use either 'ASC' or 'DESC', or leave out."
                );
            }
        }
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class ConfirmationTokensNotFoundException extends RuntimeException {
        
        public ConfirmationTokensNotFoundException(long tokenId) {
            super("ConfirmationTokens with tokenId '" + tokenId + "' not found");
        }
    }
    
    @JsonInclude(Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ConfirmationTokensCreateBody {
        
        private final long tokenId;
        private final Boolean claimed;
        private final String confirmationToken;
        private final Timestamp createdAt;
        private final Timestamp expirationDate;
        private final Integer operationType;
        private final String uid;
        
        @JsonCreator
        public ConfirmationTokensCreateBody(
                @JsonProperty("tokenId") Long tokenId,
                @JsonProperty("claimed") Boolean claimed,
                @JsonProperty("confirmationToken") String confirmationToken,
                @JsonProperty("createdAt") Timestamp createdAt,
                @JsonProperty("expirationDate") Timestamp expirationDate,
                @JsonProperty("operationType") Integer operationType,
                @JsonProperty("uid") String uid) {
            this.tokenId = Objects.requireNonNull(tokenId, "`tokenId` is required");
            this.claimed = Objects.requireNonNull(claimed, "`claimed` is required");
            this.confirmationToken = Objects.requireNonNull(confirmationToken, "`confirmationToken` is required");
            this.createdAt = Objects.requireNonNull(createdAt, "`createdAt` is required");
            this.expirationDate = Objects.requireNonNull(expirationDate, "`expirationDate` is required");
            this.operationType = Objects.requireNonNull(operationType, "`operationType` is required");
            this.uid = Objects.requireNonNull(uid, "`uid` is required");
        }
        
        public long getTokenId() {
            return this.tokenId;
        }
        
        public Boolean getClaimed() {
            return this.claimed;
        }
        
        public String getConfirmationToken() {
            return this.confirmationToken;
        }
        
        public Timestamp getCreatedAt() {
            return this.createdAt;
        }
        
        public Timestamp getExpirationDate() {
            return this.expirationDate;
        }
        
        public Integer getOperationType() {
            return this.operationType;
        }
        
        public String getUid() {
            return this.uid;
        }
    }
    
    @JsonInclude(Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ConfirmationTokensUpdateBody {
        
        private final Boolean claimed;
        private final String confirmationToken;
        private final Timestamp createdAt;
        private final Timestamp expirationDate;
        private final Integer operationType;
        private final String uid;
        
        @JsonCreator
        public ConfirmationTokensUpdateBody(
                @JsonProperty("claimed") Boolean claimed,
                @JsonProperty("confirmationToken") String confirmationToken,
                @JsonProperty("createdAt") Timestamp createdAt,
                @JsonProperty("expirationDate") Timestamp expirationDate,
                @JsonProperty("operationType") Integer operationType,
                @JsonProperty("uid") String uid) {
            this.claimed = Objects.requireNonNull(claimed, "`claimed` is required");
            this.confirmationToken = Objects.requireNonNull(confirmationToken, "`confirmationToken` is required");
            this.createdAt = Objects.requireNonNull(createdAt, "`createdAt` is required");
            this.expirationDate = Objects.requireNonNull(expirationDate, "`expirationDate` is required");
            this.operationType = Objects.requireNonNull(operationType, "`operationType` is required");
            this.uid = Objects.requireNonNull(uid, "`uid` is required");
        }
        
        public Boolean getClaimed() {
            return this.claimed;
        }
        
        public String getConfirmationToken() {
            return this.confirmationToken;
        }
        
        public Timestamp getCreatedAt() {
            return this.createdAt;
        }
        
        public Timestamp getExpirationDate() {
            return this.expirationDate;
        }
        
        public Integer getOperationType() {
            return this.operationType;
        }
        
        public String getUid() {
            return this.uid;
        }
    }
}