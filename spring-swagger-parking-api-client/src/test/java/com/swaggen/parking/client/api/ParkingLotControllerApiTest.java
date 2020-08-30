/*
 * Parking application Backend API
 * Parking IoT application project API reference for developers
 *
 * OpenAPI spec version: 0.2
 * Contact: parking_app_service@gmail.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.swaggen.parking.client.api;

import com.swaggen.parking.client.model.ParkingLot;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ParkingLotControllerApi
 */
@Ignore
public class ParkingLotControllerApiTest {

    private final ParkingLotControllerApi api = new ParkingLotControllerApi();

    /**
     * Used to unreserve parking lot
     *
     * Sets status of parking lot to unreserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelReservationUsingDELETETest() {
        Long id = null;
        Boolean response = api.cancelReservationUsingDELETE(id);

        // TODO: test validations
    }
    /**
     * Used to unreserve parking lot
     *
     * Sets status of parking lot to unreserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelReservationUsingGETTest() {
        Long id = null;
        Boolean response = api.cancelReservationUsingGET(id);

        // TODO: test validations
    }
    /**
     * Used to unreserve parking lot
     *
     * Sets status of parking lot to unreserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelReservationUsingHEADTest() {
        Long id = null;
        Boolean response = api.cancelReservationUsingHEAD(id);

        // TODO: test validations
    }
    /**
     * Used to unreserve parking lot
     *
     * Sets status of parking lot to unreserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelReservationUsingOPTIONSTest() {
        Long id = null;
        Boolean response = api.cancelReservationUsingOPTIONS(id);

        // TODO: test validations
    }
    /**
     * Used to unreserve parking lot
     *
     * Sets status of parking lot to unreserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelReservationUsingPATCHTest() {
        Long id = null;
        Boolean response = api.cancelReservationUsingPATCH(id);

        // TODO: test validations
    }
    /**
     * Used to unreserve parking lot
     *
     * Sets status of parking lot to unreserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelReservationUsingPOSTTest() {
        Long id = null;
        Boolean response = api.cancelReservationUsingPOST(id);

        // TODO: test validations
    }
    /**
     * Used to unreserve parking lot
     *
     * Sets status of parking lot to unreserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelReservationUsingPUTTest() {
        Long id = null;
        Boolean response = api.cancelReservationUsingPUT(id);

        // TODO: test validations
    }
    /**
     * Get all parking lots
     *
     * Returns all parking lots from parking lots storage
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAllParkingLotsUsingGETTest() {
        List<ParkingLot> response = api.getAllParkingLotsUsingGET();

        // TODO: test validations
    }
    /**
     * Get parking lot by id
     *
     * Returns parking lot from storage by given id
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getParkingLotByIdUsingGETTest() {
        Object headers = null;
        Long id = null;
        ParkingLot response = api.getParkingLotByIdUsingGET(headers, id);

        // TODO: test validations
    }
    /**
     * Used to reserve parking lot
     *
     * Sets status of parking lot to reserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void reservationUsingDELETETest() {
        Long id = null;
        Boolean response = api.reservationUsingDELETE(id);

        // TODO: test validations
    }
    /**
     * Used to reserve parking lot
     *
     * Sets status of parking lot to reserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void reservationUsingGETTest() {
        Long id = null;
        Boolean response = api.reservationUsingGET(id);

        // TODO: test validations
    }
    /**
     * Used to reserve parking lot
     *
     * Sets status of parking lot to reserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void reservationUsingHEADTest() {
        Long id = null;
        Boolean response = api.reservationUsingHEAD(id);

        // TODO: test validations
    }
    /**
     * Used to reserve parking lot
     *
     * Sets status of parking lot to reserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void reservationUsingOPTIONSTest() {
        Long id = null;
        Boolean response = api.reservationUsingOPTIONS(id);

        // TODO: test validations
    }
    /**
     * Used to reserve parking lot
     *
     * Sets status of parking lot to reserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void reservationUsingPATCHTest() {
        Long id = null;
        Boolean response = api.reservationUsingPATCH(id);

        // TODO: test validations
    }
    /**
     * Used to reserve parking lot
     *
     * Sets status of parking lot to reserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void reservationUsingPOSTTest() {
        Long id = null;
        Boolean response = api.reservationUsingPOST(id);

        // TODO: test validations
    }
    /**
     * Used to reserve parking lot
     *
     * Sets status of parking lot to reserved
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void reservationUsingPUTTest() {
        Long id = null;
        Boolean response = api.reservationUsingPUT(id);

        // TODO: test validations
    }
}