package com.coursework.smartcampus;

import com.coursework.smartcampus.model.Room;
import com.coursework.smartcampus.repository.DataStore;
import com.coursework.smartcampus.exception.RoomNotEmptyException;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Response getAllRooms() {
        return Response.ok(new ArrayList<>(DataStore.rooms.values())).build();
    }

    @POST
    public Response createRoom(Room room) {

        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(400)
                    .entity("Room ID required")
                    .build();
        }

        DataStore.rooms.put(room.getId(), room);

        return Response.status(201)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getRoomById(@PathParam("id") String id) {

        Room room = DataStore.rooms.get(id);

        if (room == null) {
            return Response.status(404)
                    .entity("Room not found")
                    .build();
        }

        return Response.ok(room).build();
    }
    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {

        Room room = DataStore.rooms.get(id);

        if (room == null) {
            return Response.status(404)
                    .entity("Room not found")
                    .build();
        }

        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room cannot be deleted because sensors are assigned"
            );
        }

        DataStore.rooms.remove(id);

        return Response.ok("Room deleted").build();
    }
}