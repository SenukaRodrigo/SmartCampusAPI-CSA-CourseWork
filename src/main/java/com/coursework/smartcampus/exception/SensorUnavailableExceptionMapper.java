package com.coursework.smartcampus.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class SensorUnavailableExceptionMapper
        implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return Response.status(403)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}