package com.coursework.smartcampus;

import com.coursework.smartcampus.exception.LinkedResourceNotFoundException;
import com.coursework.smartcampus.model.Room;
import com.coursework.smartcampus.model.Sensor;
import com.coursework.smartcampus.repository.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @POST
    public Response createSensor(Sensor sensor) {

        Room room = DataStore.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "Referenced room does not exist"
            );
        }

        DataStore.sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());

        return Response.status(201)
                .entity(sensor)
                .build();
    }

    @GET
    public Response getSensors(@QueryParam("type") String type) {

        List<Sensor> result = new ArrayList<>();

        for (Sensor sensor : DataStore.sensors.values()) {

            if (type == null || type.isEmpty()) {
                result.add(sensor);
            } else if (sensor.getType().equalsIgnoreCase(type)) {
                result.add(sensor);
            }
        }

        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}")
    public Response getSensorById(@PathParam("id") String id) {

        Sensor sensor = DataStore.sensors.get(id);

        if (sensor == null) {
            return Response.status(404)
                    .entity("Sensor not found")
                    .build();
        }

        return Response.ok(sensor).build();
    }

    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }
}