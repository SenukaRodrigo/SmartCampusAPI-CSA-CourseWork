package com.coursework.smartcampus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getInfo() {

        Map<String, Object> data = new HashMap<>();

        data.put("name", "Smart Campus API");
        data.put("version", "v1");
        data.put("developer", "Senuka");

        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");

        data.put("resources", links);

        return data;
    }
}