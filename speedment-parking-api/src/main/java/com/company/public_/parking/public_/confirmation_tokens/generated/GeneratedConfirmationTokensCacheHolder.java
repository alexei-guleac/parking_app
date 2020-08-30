package com.company.public_.parking.public_.confirmation_tokens.generated;

import com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokens;
import com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokensEntityStoreSerializerImpl;
import com.company.public_.parking.public_.confirmation_tokens.ConfirmationTokensManager;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.common.tuple.Tuple2;
import com.speedment.common.tuple.Tuples;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStore;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStoreHolder;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache.OfBoolean;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache.OfComparable;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache.OfInt;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache.OfLong;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache.OfString;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache;
import com.speedment.enterprise.datastore.runtime.fieldcache.MultiFieldCache;
import com.speedment.enterprise.datastore.runtime.statistic.Statistics;
import com.speedment.enterprise.datastore.runtime.util.DataStoreHolderUtil;
import com.speedment.enterprise.datastore.runtime.util.StatisticsUtil;
import com.speedment.runtime.bulk.PersistOperation;
import com.speedment.runtime.bulk.RemoveOperation;
import com.speedment.runtime.bulk.UpdateOperation;
import com.speedment.runtime.config.identifier.ColumnIdentifier;
import com.speedment.runtime.config.identifier.ColumnLabel;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.component.StreamSupplierComponent;
import com.speedment.runtime.field.Field;
import com.speedment.runtime.field.trait.HasIdentifier;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * A holder class for the various caches that are used to speed up the {@link
 * ConfirmationTokensManager}.
 * 
 * Generated by
 * com.speedment.enterprise.datastore.generator.internal.code.GeneratedEntityCacheHolderTranslator
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public final class GeneratedConfirmationTokensCacheHolder implements EntityStoreHolder<ConfirmationTokens> {
    
    private final EntityStore<ConfirmationTokens> entityStore;
    private final OfLong fieldTokenIdCache;
    private final OfBoolean fieldClaimedCache;
    private final OfString fieldConfirmationTokenCache;
    private final OfComparable<Timestamp> fieldCreatedAtCache;
    private final OfComparable<Timestamp> fieldExpirationDateCache;
    private final OfInt fieldOperationTypeCache;
    private final OfString fieldUidCache;
    
    public GeneratedConfirmationTokensCacheHolder(
            EntityStore<ConfirmationTokens> entityStore,
            OfLong fieldTokenIdCache,
            OfBoolean fieldClaimedCache,
            OfString fieldConfirmationTokenCache,
            OfComparable<Timestamp> fieldCreatedAtCache,
            OfComparable<Timestamp> fieldExpirationDateCache,
            OfInt fieldOperationTypeCache,
            OfString fieldUidCache) {
        
        this.entityStore                 = requireNonNull(entityStore);
        this.fieldTokenIdCache           = requireNonNull(fieldTokenIdCache);
        this.fieldClaimedCache           = requireNonNull(fieldClaimedCache);
        this.fieldConfirmationTokenCache = requireNonNull(fieldConfirmationTokenCache);
        this.fieldCreatedAtCache         = requireNonNull(fieldCreatedAtCache);
        this.fieldExpirationDateCache    = requireNonNull(fieldExpirationDateCache);
        this.fieldOperationTypeCache     = requireNonNull(fieldOperationTypeCache);
        this.fieldUidCache               = requireNonNull(fieldUidCache);
    }
    
    @Override
    public EntityStore<ConfirmationTokens> getEntityStore() {
        return entityStore;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <CACHE extends FieldCache<CACHE>> CACHE getFieldCache(ColumnIdentifier<ConfirmationTokens> columnId) {
        if (columnId instanceof ConfirmationTokens.Identifier) {
            final ConfirmationTokens.Identifier _id = (ConfirmationTokens.Identifier) columnId;
            switch (_id) {
                case TOKEN_ID           : return (CACHE) fieldTokenIdCache;
                case CLAIMED            : return (CACHE) fieldClaimedCache;
                case CONFIRMATION_TOKEN : return (CACHE) fieldConfirmationTokenCache;
                case CREATED_AT         : return (CACHE) fieldCreatedAtCache;
                case EXPIRATION_DATE    : return (CACHE) fieldExpirationDateCache;
                case OPERATION_TYPE     : return (CACHE) fieldOperationTypeCache;
                case UID                : return (CACHE) fieldUidCache;
                default : {
                    throw new UnsupportedOperationException(
                        String.format("Unknown enum constant '%s'.", _id)
                    );
                }
            }
        } else {
            final String _colName = columnId.getColumnId();
            switch (_colName) {
                case "token_id"           : return (CACHE) fieldTokenIdCache;
                case "claimed"            : return (CACHE) fieldClaimedCache;
                case "confirmation_token" : return (CACHE) fieldConfirmationTokenCache;
                case "created_at"         : return (CACHE) fieldCreatedAtCache;
                case "expiration_date"    : return (CACHE) fieldExpirationDateCache;
                case "operation_type"     : return (CACHE) fieldOperationTypeCache;
                case "uid"                : return (CACHE) fieldUidCache;
                default : {
                    throw new UnsupportedOperationException(
                        String.format("Unknown column name '%s'.", _colName)
                    );
                }
            }
        }
    }
    
    @Override
    public boolean isHavingMultiFieldCache(ColumnIdentifier<ConfirmationTokens> columnId) {
        return false;
    }
    
    public static CompletableFuture<GeneratedConfirmationTokensCacheHolder> reload(StreamSupplierComponent streamSupplier, ExecutorService executor) {
        return reload(DataStoreHolderUtil.buildEntityStore(
            streamSupplier,
            executor,
            ConfirmationTokensEntityStoreSerializerImpl::new,
            TableIdentifier.of("parking", "public", "confirmation_tokens")
        ), executor);
    }
    
    @Override
    public EntityStoreHolder<ConfirmationTokens> recycleAndPersist(PersistOperation<ConfirmationTokens> persistOperation) {
        return wrapped().recycleAndPersist(persistOperation);
    }
    
    @Override
    public EntityStoreHolder<ConfirmationTokens> recycleAndRemove(RemoveOperation<ConfirmationTokens> removeOperation) {
        return wrapped().recycleAndRemove(removeOperation);
    }
    
    @Override
    public EntityStoreHolder<ConfirmationTokens> recycleAndUpdate(UpdateOperation<ConfirmationTokens> updateOperation) {
        return wrapped().recycleAndUpdate(updateOperation);
    }
    
    private EntityStoreHolder<ConfirmationTokens> wrapped() {
        // Use explicit type for Stream to improve compilation time.
        final Map<ColumnLabel, FieldCache<?>> fieldCaches = Stream.<Tuple2<HasIdentifier<ConfirmationTokens>, FieldCache<?>>>of(
            Tuples.of(ConfirmationTokens.TOKEN_ID,          fieldTokenIdCache),
            Tuples.of(ConfirmationTokens.CLAIMED,           fieldClaimedCache),
            Tuples.of(ConfirmationTokens.CONFIRMATION_TOKEN,fieldConfirmationTokenCache),
            Tuples.of(ConfirmationTokens.CREATED_AT,        fieldCreatedAtCache),
            Tuples.of(ConfirmationTokens.EXPIRATION_DATE,   fieldExpirationDateCache),
            Tuples.of(ConfirmationTokens.OPERATION_TYPE,    fieldOperationTypeCache),
            Tuples.of(ConfirmationTokens.UID,               fieldUidCache)
        )
            .collect(toMap(t2 -> t2.get0().identifier().label(), Tuple2::get1));
        final Map<ColumnLabel,  Map<ColumnLabel, MultiFieldCache<?, ?, ?>>>  multiFieldCaches = createMultiCacheMap();
        final Set<ColumnIdentifier<ConfirmationTokens>> columnIdentifiers = Stream.<HasIdentifier<ConfirmationTokens>>of(
            ConfirmationTokens.TOKEN_ID,
            ConfirmationTokens.CLAIMED,
            ConfirmationTokens.CONFIRMATION_TOKEN,
            ConfirmationTokens.CREATED_AT,
            ConfirmationTokens.EXPIRATION_DATE,
            ConfirmationTokens.OPERATION_TYPE,
            ConfirmationTokens.UID
        )
            .map(HasIdentifier::identifier)
            .collect(toSet());
        return EntityStoreHolder.of(
            entityStore,
            fieldCaches,
            multiFieldCaches,
            columnIdentifiers
        );
    }
    
    public static CompletableFuture<GeneratedConfirmationTokensCacheHolder> reload(CompletableFuture<EntityStore<ConfirmationTokens>> entityStoreFuture, ExecutorService executor) {
        final CompletableFuture<FieldCache.OfLong> fieldTokenIdCacheFuture =
            DataStoreHolderUtil.buildLongCache(entityStoreFuture, executor, ConfirmationTokens.TOKEN_ID, FieldCache.DISTINCT);
        
        final CompletableFuture<FieldCache.OfBoolean> fieldClaimedCacheFuture =
            DataStoreHolderUtil.buildBooleanCache(entityStoreFuture, executor, ConfirmationTokens.CLAIMED, 0);
        
        final CompletableFuture<FieldCache.OfString> fieldConfirmationTokenCacheFuture =
            DataStoreHolderUtil.buildStringCache(entityStoreFuture, executor, ConfirmationTokens.CONFIRMATION_TOKEN, 0);
        
        final CompletableFuture<FieldCache.OfComparable<Timestamp>> fieldCreatedAtCacheFuture =
            DataStoreHolderUtil.buildComparableCache(entityStoreFuture, executor, ConfirmationTokens.CREATED_AT, 0);
        
        final CompletableFuture<FieldCache.OfComparable<Timestamp>> fieldExpirationDateCacheFuture =
            DataStoreHolderUtil.buildComparableCache(entityStoreFuture, executor, ConfirmationTokens.EXPIRATION_DATE, 0);
        
        final CompletableFuture<FieldCache.OfInt> fieldOperationTypeCacheFuture =
            DataStoreHolderUtil.buildIntCache(entityStoreFuture, executor, ConfirmationTokens.OPERATION_TYPE, 0);
        
        final CompletableFuture<FieldCache.OfString> fieldUidCacheFuture =
            DataStoreHolderUtil.buildStringCache(entityStoreFuture, executor, ConfirmationTokens.UID, 0);
        
        return entityStoreFuture.thenApplyAsync(entityStore -> {
            try {
                return new GeneratedConfirmationTokensCacheHolder(
                    entityStore,
                    fieldTokenIdCacheFuture.get(),
                    fieldClaimedCacheFuture.get(),
                    fieldConfirmationTokenCacheFuture.get(),
                    fieldCreatedAtCacheFuture.get(),
                    fieldExpirationDateCacheFuture.get(),
                    fieldOperationTypeCacheFuture.get(),
                    fieldUidCacheFuture.get()
                );
            } catch (final ExecutionException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    
    @Override
    public void close() {
        entityStore.close();
        fieldTokenIdCache.close();
        fieldClaimedCache.close();
        fieldConfirmationTokenCache.close();
        fieldCreatedAtCache.close();
        fieldExpirationDateCache.close();
        fieldOperationTypeCache.close();
        fieldUidCache.close();
    }
    
    @Override
    public Statistics getStatistics() {
        return StatisticsUtil.getStatistics(    
            this,
            entityStore.identifier(),
            Arrays.asList(
                ConfirmationTokens.Identifier.TOKEN_ID,
                ConfirmationTokens.Identifier.CLAIMED,
                ConfirmationTokens.Identifier.CONFIRMATION_TOKEN,
                ConfirmationTokens.Identifier.CREATED_AT,
                ConfirmationTokens.Identifier.EXPIRATION_DATE,
                ConfirmationTokens.Identifier.OPERATION_TYPE,
                ConfirmationTokens.Identifier.UID
            )
        
        );
    }
    
    private Map<ColumnLabel, Map<ColumnLabel, MultiFieldCache<?, ?, ?>>> createMultiCacheMap() {
        return Collections.emptyMap();
    }
}