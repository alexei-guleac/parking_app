package com.company.public_.parking.public_.parking_lots.generated;

import com.company.public_.parking.public_.parking_lots.ParkingLots;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.manager.Manager;
import com.speedment.runtime.field.Field;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * The generated base interface for the manager of every {@link
 * com.company.public_.parking.public_.parking_lots.ParkingLots} entity.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public interface GeneratedParkingLotsManager extends Manager<ParkingLots> {
    
    TableIdentifier<ParkingLots> IDENTIFIER = TableIdentifier.of("parking", "public", "parking_lots");
    List<Field<ParkingLots>> FIELDS = unmodifiableList(asList(
        ParkingLots.ID,
        ParkingLots.LOT_NUMBER,
        ParkingLots.STATUS,
        ParkingLots.UPDATED_AT
    ));
    
    @Override
    default Class<ParkingLots> getEntityClass() {
        return ParkingLots.class;
    }
}