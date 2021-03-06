package com.company.public_.parking.public_.parking_lots.generated;

import com.company.public_.parking.public_.parking_lots.ParkingLots;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.enterprise.datastore.runtime.field.DatastoreFields;
import com.speedment.runtime.config.identifier.ColumnIdentifier;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.util.OptionalUtil;
import com.speedment.runtime.field.ComparableField;
import com.speedment.runtime.field.LongField;
import com.speedment.runtime.typemapper.TypeMapper;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * The generated base for the {@link
 * com.company.public_.parking.public_.parking_lots.ParkingLots}-interface
 * representing entities of the {@code parking_lots}-table in the database.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public interface GeneratedParkingLots {
    
    /**
     * This Field corresponds to the {@link ParkingLots} field that can be
     * obtained using the {@link ParkingLots#getId()} method.
     */
    LongField<ParkingLots, Long> ID = DatastoreFields.createLongField(
        Identifier.ID,
        ParkingLots::getId,
        ParkingLots::setId,
        TypeMapper.primitive(),
        true
    );
    /**
     * This Field corresponds to the {@link ParkingLots} field that can be
     * obtained using the {@link ParkingLots#getLotNumber()} method.
     */
    ComparableField<ParkingLots, Integer, Integer> LOT_NUMBER = DatastoreFields.createComparableField(
        Identifier.LOT_NUMBER,
        o -> OptionalUtil.unwrap(o.getLotNumber()),
        ParkingLots::setLotNumber,
        TypeMapper.identity(),
        false
    );
    /**
     * This Field corresponds to the {@link ParkingLots} field that can be
     * obtained using the {@link ParkingLots#getStatus()} method.
     */
    ComparableField<ParkingLots, Integer, Integer> STATUS = DatastoreFields.createComparableField(
        Identifier.STATUS,
        o -> OptionalUtil.unwrap(o.getStatus()),
        ParkingLots::setStatus,
        TypeMapper.identity(),
        false
    );
    /**
     * This Field corresponds to the {@link ParkingLots} field that can be
     * obtained using the {@link ParkingLots#getUpdatedAt()} method.
     */
    ComparableField<ParkingLots, Timestamp, Timestamp> UPDATED_AT = DatastoreFields.createComparableField(
        Identifier.UPDATED_AT,
        o -> OptionalUtil.unwrap(o.getUpdatedAt()),
        ParkingLots::setUpdatedAt,
        TypeMapper.identity(),
        false
    );
    
    /**
     * Returns the id of this ParkingLots. The id field corresponds to the
     * database column parking.public.parking_lots.id.
     * 
     * @return the id of this ParkingLots
     */
    long getId();
    
    /**
     * Returns the lotNumber of this ParkingLots. The lotNumber field
     * corresponds to the database column
     * parking.public.parking_lots.lot_number.
     * 
     * @return the lotNumber of this ParkingLots
     */
    OptionalInt getLotNumber();
    
    /**
     * Returns the status of this ParkingLots. The status field corresponds to
     * the database column parking.public.parking_lots.status.
     * 
     * @return the status of this ParkingLots
     */
    OptionalInt getStatus();
    
    /**
     * Returns the updatedAt of this ParkingLots. The updatedAt field
     * corresponds to the database column
     * parking.public.parking_lots.updated_at.
     * 
     * @return the updatedAt of this ParkingLots
     */
    Optional<Timestamp> getUpdatedAt();
    
    /**
     * Sets the id of this ParkingLots. The id field corresponds to the database
     * column parking.public.parking_lots.id.
     * 
     * @param id to set of this ParkingLots
     * @return   this ParkingLots instance
     */
    ParkingLots setId(long id);
    
    /**
     * Sets the lotNumber of this ParkingLots. The lotNumber field corresponds
     * to the database column parking.public.parking_lots.lot_number.
     * 
     * @param lotNumber to set of this ParkingLots
     * @return          this ParkingLots instance
     */
    ParkingLots setLotNumber(Integer lotNumber);
    
    /**
     * Sets the status of this ParkingLots. The status field corresponds to the
     * database column parking.public.parking_lots.status.
     * 
     * @param status to set of this ParkingLots
     * @return       this ParkingLots instance
     */
    ParkingLots setStatus(Integer status);
    
    /**
     * Sets the updatedAt of this ParkingLots. The updatedAt field corresponds
     * to the database column parking.public.parking_lots.updated_at.
     * 
     * @param updatedAt to set of this ParkingLots
     * @return          this ParkingLots instance
     */
    ParkingLots setUpdatedAt(Timestamp updatedAt);
    
    enum Identifier implements ColumnIdentifier<ParkingLots> {
        
        ID         ("id"),
        LOT_NUMBER ("lot_number"),
        STATUS     ("status"),
        UPDATED_AT ("updated_at");
        
        private final String columnId;
        private final TableIdentifier<ParkingLots> tableIdentifier;
        
        Identifier(String columnId) {
            this.columnId        = columnId;
            this.tableIdentifier = TableIdentifier.of(    getDbmsId(), 
                getSchemaId(), 
                getTableId());
        }
        
        @Override
        public String getDbmsId() {
            return "parking";
        }
        
        @Override
        public String getSchemaId() {
            return "public";
        }
        
        @Override
        public String getTableId() {
            return "parking_lots";
        }
        
        @Override
        public String getColumnId() {
            return this.columnId;
        }
        
        @Override
        public TableIdentifier<ParkingLots> asTableIdentifier() {
            return this.tableIdentifier;
        }
    }
}