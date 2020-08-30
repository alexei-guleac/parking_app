package com.company.public_.parking.public_.statistics.generated;

import com.company.public_.parking.public_.statistics.Statistics;
import com.company.public_.parking.public_.statistics.StatisticsManager;
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
 * The default REST controller logic for Statistics entities.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/public")
public abstract class GeneratedStatisticsController {
    
    protected @Autowired JsonComponent jsonComponent;
    protected @Autowired StatisticsManager manager;
    protected JsonEncoder<Statistics> encoder;
    
    @PostConstruct
    void createStatisticsEncoder() {
        encoder = jsonComponent.<Statistics>emptyEncoder()
            .put("id", Statistics.ID)
            .put("lotNumber", Statistics.LOT_NUMBER)
            .put("parkingLotStatus", Statistics.PARKING_LOT_STATUS)
            .put("updatedAt", Statistics.UPDATED_AT)
            .build();
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/statistics", produces = "application/json")
    public String list(
            @RequestParam(name = "filter", defaultValue = "[]") String filters,
            @RequestParam(name = "sort", defaultValue = "[]") String sorters,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "limit", defaultValue = "25") long limit) {
        
        return listHelper(
            ControllerUtil.parseFilters(filters, StatisticsFilter::new).collect(toList()),
            ControllerUtil.parseSorts(sorters, StatisticsSort::new).collect(toList()),
            start,
            limit
        );
    }
    
    protected String listHelper(
            List<Predicate<Statistics>> predicates,
            List<Comparator<Statistics>> sorters,
            long start,
            long limit) {
        
        Stream<Statistics> stream      = manager.stream();
        Stream<Statistics> totalStream = manager.stream();
        
        for (final Predicate<Statistics> predicate : predicates) {
            stream      = stream.filter(predicate);
            totalStream = totalStream.filter(predicate);
        }
        
        if (!sorters.isEmpty()) {
            final Optional<Comparator<Statistics>> comparator = sorters.stream()
                .reduce(Comparator::thenComparing);
            
            stream = stream.sorted(comparator.get());
        }
        
        final String total = String.valueOf(totalStream.count());
        return stream
            .skip(start)
            .limit(limit)
            .collect(jsonComponent.collector(Statistics.class)
                .put("total", () -> total)
                .put("data", JsonCollectors.toList(encoder))
                .build()
            );
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/statistics/{id}", produces = "application/json")
    public String get(
            @PathVariable(name = "id") long id) {
        return encoder.apply(
            manager.stream()
                .filter(Statistics.ID.equal(id))
                .findFirst()
                .orElseThrow(() -> new StatisticsNotFoundException(id))
        );
    }
    
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "/statistics", consumes = "application/json")
    public void create(
            @RequestBody @Validated StatisticsCreateBody createBody) {
        final Persister<Statistics> persister = manager.persister();
        final Statistics statistics = manager.create()
            .setId(createBody.getId())
            .setLotNumber(createBody.getLotNumber())
            .setParkingLotStatus(createBody.getParkingLotStatus())
            .setUpdatedAt(createBody.getUpdatedAt())
        ;
        
        persister.accept(statistics);
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping(path = "/statistics/{id}", consumes = "application/json")
    public void update(
            @PathVariable(name = "id") long id,
            @RequestBody @Validated StatisticsUpdateBody updateBody) {
        final FieldSet<Statistics> excluded = FieldSet.allExcept(
            Statistics.ID
        );
        
        final Updater<Statistics> updater = manager.updater(excluded);
        final Statistics statistics = manager.stream()
            .filter(Statistics.ID.equal(id))
            .findFirst()
            .orElseThrow(() -> new StatisticsNotFoundException(id));
        
        statistics.setLotNumber(updateBody.getLotNumber());
        statistics.setParkingLotStatus(updateBody.getParkingLotStatus());
        statistics.setUpdatedAt(updateBody.getUpdatedAt());
        
        updater.accept(statistics);
    }
    
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/statistics/{id}")
    public void delete(
            @PathVariable(name = "id") long id) {
        manager.stream()
            .filter(Statistics.ID.equal(id))
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
    public static final class StatisticsFilter extends AbstractFilter<Statistics> {
        
        public StatisticsFilter(
                String operator,
                String property,
                String value) {
            super(operator, property, value);
        }
        
        @Override
        public Predicate<Statistics> toPredicate() {
            switch (property()) {
                case "id" : {
                    final long v = Long.parseLong(value());
                    switch (operator()) {
                        case "eq"   : return Statistics.ID.equal(v);
                        case "ne"   : return Statistics.ID.notEqual(v);
                        case "lt"   : return Statistics.ID.lessThan(v);
                        case "le"   : return Statistics.ID.lessOrEqual(v);
                        case "gt"   : return Statistics.ID.greaterThan(v);
                        case "ge"   : return Statistics.ID.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "Statistics.id."
                        );
                    }
                }
                case "lotNumber" : {
                    final Integer v = Integer.parseInt(value());
                    switch (operator()) {
                        case "eq"   : return Statistics.LOT_NUMBER.equal(v);
                        case "ne"   : return Statistics.LOT_NUMBER.notEqual(v);
                        case "lt"   : return Statistics.LOT_NUMBER.lessThan(v);
                        case "le"   : return Statistics.LOT_NUMBER.lessOrEqual(v);
                        case "gt"   : return Statistics.LOT_NUMBER.greaterThan(v);
                        case "ge"   : return Statistics.LOT_NUMBER.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "Statistics.lotNumber."
                        );
                    }
                }
                case "parkingLotStatus" : {
                    final Integer v = Integer.parseInt(value());
                    switch (operator()) {
                        case "eq"   : return Statistics.PARKING_LOT_STATUS.equal(v);
                        case "ne"   : return Statistics.PARKING_LOT_STATUS.notEqual(v);
                        case "lt"   : return Statistics.PARKING_LOT_STATUS.lessThan(v);
                        case "le"   : return Statistics.PARKING_LOT_STATUS.lessOrEqual(v);
                        case "gt"   : return Statistics.PARKING_LOT_STATUS.greaterThan(v);
                        case "ge"   : return Statistics.PARKING_LOT_STATUS.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "Statistics.parkingLotStatus."
                        );
                    }
                }
                case "updatedAt" : {
                    final Timestamp v = Timestamp.valueOf(value());
                    switch (operator()) {
                        case "eq"   : return Statistics.UPDATED_AT.equal(v);
                        case "ne"   : return Statistics.UPDATED_AT.notEqual(v);
                        case "lt"   : return Statistics.UPDATED_AT.lessThan(v);
                        case "le"   : return Statistics.UPDATED_AT.lessOrEqual(v);
                        case "gt"   : return Statistics.UPDATED_AT.greaterThan(v);
                        case "ge"   : return Statistics.UPDATED_AT.greaterOrEqual(v);
                        case "like" : // Fallthrough
                        default : throw new IllegalArgumentException(
                            "'" + operator() + "' is not a valid operator for " +
                            "Statistics.updatedAt."
                        );
                    }
                }
                default : throw new IllegalArgumentException(
                    "'" + property() + "' is not a valid Statistics property."
                );
            }
        }
    }
    
    /**
     * How to sort the results from the controller. This class is designed to be
     * deserialized automatically from JSON.
     */
    public static final class StatisticsSort extends AbstractSort<Statistics> {
        
        public StatisticsSort(String property, String direction) {
            super(property, direction);
        }
        
        @Override
        public Comparator<Statistics> toComparator() {
            final Comparator<Statistics> comparator;
            switch (property()) {
                case "id"               : comparator = Statistics.ID.comparator();                 break;
                case "lotNumber"        : comparator = Statistics.LOT_NUMBER.comparator();         break;
                case "parkingLotStatus" : comparator = Statistics.PARKING_LOT_STATUS.comparator(); break;
                case "updatedAt"        : comparator = Statistics.UPDATED_AT.comparator();         break;
                default : throw new IllegalArgumentException(
                    "'" + property() + "' is not a valid Statistics property."
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
    private static class StatisticsNotFoundException extends RuntimeException {
        
        public StatisticsNotFoundException(long id) {
            super("Statistics with id '" + id + "' not found");
        }
    }
    
    @JsonInclude(Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class StatisticsCreateBody {
        
        private final long id;
        private final Integer lotNumber;
        private final Integer parkingLotStatus;
        private final Timestamp updatedAt;
        
        @JsonCreator
        public StatisticsCreateBody(
                @JsonProperty("id") Long id,
                @JsonProperty("lotNumber") Integer lotNumber,
                @JsonProperty("parkingLotStatus") Integer parkingLotStatus,
                @JsonProperty("updatedAt") Timestamp updatedAt) {
            this.id = Objects.requireNonNull(id, "`id` is required");
            this.lotNumber = Objects.requireNonNull(lotNumber, "`lotNumber` is required");
            this.parkingLotStatus = Objects.requireNonNull(parkingLotStatus, "`parkingLotStatus` is required");
            this.updatedAt = Objects.requireNonNull(updatedAt, "`updatedAt` is required");
        }
        
        public long getId() {
            return this.id;
        }
        
        public Integer getLotNumber() {
            return this.lotNumber;
        }
        
        public Integer getParkingLotStatus() {
            return this.parkingLotStatus;
        }
        
        public Timestamp getUpdatedAt() {
            return this.updatedAt;
        }
    }
    
    @JsonInclude(Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class StatisticsUpdateBody {
        
        private final Integer lotNumber;
        private final Integer parkingLotStatus;
        private final Timestamp updatedAt;
        
        @JsonCreator
        public StatisticsUpdateBody(
                @JsonProperty("lotNumber") Integer lotNumber,
                @JsonProperty("parkingLotStatus") Integer parkingLotStatus,
                @JsonProperty("updatedAt") Timestamp updatedAt) {
            this.lotNumber = Objects.requireNonNull(lotNumber, "`lotNumber` is required");
            this.parkingLotStatus = Objects.requireNonNull(parkingLotStatus, "`parkingLotStatus` is required");
            this.updatedAt = Objects.requireNonNull(updatedAt, "`updatedAt` is required");
        }
        
        public Integer getLotNumber() {
            return this.lotNumber;
        }
        
        public Integer getParkingLotStatus() {
            return this.parkingLotStatus;
        }
        
        public Timestamp getUpdatedAt() {
            return this.updatedAt;
        }
    }
}