package com.isd.parking.repository;

import com.isd.parking.models.subjects.StatisticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Statistics database repository
 */
@Repository
public interface StatisticsRepository extends JpaRepository<StatisticsRecord, Long> {

    /**
     * Methods for delete records from statistics database by specified query
     * Used for deleting statistics older than specified date
     *
     * @param date - base date
     * @return - operation status
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM statistics s WHERE s.updatedAt < :date")
    int removeOlderThan(@Param("date") java.sql.Date date);


    /**
     * Get all statistics records from database by parking lot number
     *
     * @param parkingLotNumber - target parking lot number
     * @return - statistics records list
     */
    List<StatisticsRecord> findByLotNumber(int parkingLotNumber);
}
