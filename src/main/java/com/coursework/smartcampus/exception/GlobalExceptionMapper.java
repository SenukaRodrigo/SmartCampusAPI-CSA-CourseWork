package com.coursework.smartcampus.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");

        return Response.status(500)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}