package com.coursework.smartcampus;

import com.coursework.smartcampus.exception.SensorUnavailableException;
import com.coursework.smartcampus.model.Sensor;
import com.coursework.smartcampus.model.SensorReading;
import com.coursework.smartcampus.repository.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {

        List<SensorReading> list =
                DataStore.readings.getOrDefault(sensorId, new ArrayList<>());

        return Response.ok(list).build();
    }

    @POST
    public Response addReading(SensorReading reading) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(404)
                    .entity("Sensor not found")
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor is under maintenance and cannot accept readings"
            );
        }
        List<SensorReading> list =
                DataStore.readings.getOrDefault(sensorId, new ArrayList<>());

        list.add(reading);
        DataStore.readings.put(sensorId, list);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(201)
                .entity(reading)
                .build();
    }
}