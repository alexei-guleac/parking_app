package com.company.public_.generated;

import com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokens;
import com.company.public_.parking.public_.confirmation_tokens.generated.GeneratedConfirmationTokensCacheHolder;
import com.company.public_.parking.public_.parking_lots.ParkingLots;
import com.company.public_.parking.public_.parking_lots.generated.GeneratedParkingLotsCacheHolder;
import com.company.public_.parking.public_.statistics.generated.GeneratedStatisticsCacheHolder;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.enterprise.datastore.runtime.DataStoreHolder;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStore;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStoreHolder;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache;
import com.speedment.enterprise.datastore.runtime.fieldcache.MultiFieldCache;
import com.speedment.enterprise.datastore.runtime.statistic.Statistics;
import com.speedment.enterprise.datastore.runtime.util.StatisticsUtil;
import com.speedment.runtime.config.identifier.ColumnIdentifier;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A holder class for the various caches that are used to speed up the project.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 *
 * @author Speedment
 */
@GeneratedCode("Speedment")
public final class GeneratedPublic_DataStoreHolder implements DataStoreHolder {

    private final GeneratedConfirmationTokensCacheHolder confirmationTokensHolder;
    private final GeneratedParkingLotsCacheHolder parkingLotsHolder;
    private final GeneratedStatisticsCacheHolder statisticsHolder;

    public GeneratedPublic_DataStoreHolder(
            GeneratedConfirmationTokensCacheHolder confirmationTokensHolder,
            GeneratedParkingLotsCacheHolder parkingLotsHolder,
            GeneratedStatisticsCacheHolder statisticsHolder) {
        this.confirmationTokensHolder = requireNonNull(confirmationTokensHolder);
        this.parkingLotsHolder        = requireNonNull(parkingLotsHolder);
        this.statisticsHolder         = requireNonNull(statisticsHolder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ENTITY> EntityStore<ENTITY> getEntityStore(
            String dbmsName,
            String schemaName,
            String tableName) {
        switch (tableName) {
            case "confirmation_tokens" : return (EntityStore<ENTITY>) confirmationTokensHolder.getEntityStore();
            case "parking_lots"        : return (EntityStore<ENTITY>) parkingLotsHolder.getEntityStore();
            case "statistics"          : return (EntityStore<ENTITY>) statisticsHolder.getEntityStore();
            default : throw new UnsupportedOperationException(
                String.format("Could not find '%s' in database model.", tableName)
            );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ENTITY, CACHE extends FieldCache<CACHE>> CACHE getFieldCache(ColumnIdentifier<ENTITY> columnId) {
        switch (columnId.getTableId()) {
            case "confirmation_tokens" : return (CACHE) confirmationTokensHolder.getFieldCache((ColumnIdentifier<ConfirmationTokens>) columnId);
            case "parking_lots"        : return (CACHE) parkingLotsHolder.getFieldCache((ColumnIdentifier<ParkingLots>) columnId);
            case "statistics"          : return (CACHE) statisticsHolder.getFieldCache((ColumnIdentifier<com.company.public_.parking.public_.statistics.Statistics>) columnId);
            default : throw new UnsupportedOperationException(
                String.format("Could not find '%s' in database model.", columnId.getTableId())
            );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ENTITY, T0, T1, CACHE extends MultiFieldCache<T0, T1, CACHE>> Optional<CACHE> getMultiFieldCache(ColumnIdentifier<ENTITY> firstColumnId, ColumnIdentifier<ENTITY> secondColumnId) {
        return Optional.empty(); // No multi-indexes are used in the project
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ENTITY> boolean isHavingMultiFieldCache(ColumnIdentifier<ENTITY> columnId) {
        return false; // No multi-indexes are used in the project
    }

    @Override
    public void close() {
        holders().forEach(EntityStoreHolder::close);
    }

    @Override
    public Statistics getStatistics() {
        return StatisticsUtil.create(
            holders()
                .toArray(EntityStoreHolder[]::new)
        );
    }

    @Override
    public Stream<EntityStoreHolder<?>> holders() {
        return Stream.of(
            confirmationTokensHolder,
            parkingLotsHolder,
            statisticsHolder
        );
    }
}