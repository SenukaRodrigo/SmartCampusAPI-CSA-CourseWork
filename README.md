Smart Campus Sensor / Room Management API.

## Module

5COSC022W - Client-Server Architectures

## Coursework Title

Smart Campus Sensor & Room Management API.

## Student

Senuka Rodrigo
W2120474
20231309

---

## Project Overview

The project is a RESTful web service written in \*\*Java, Maven and JAX-RS (Jersey).

The API is a simulation of a university Smart Campus system, which handles:

- Rooms across campus
- Room sensors.
- Historical sensor readings
- Operational logging and error handling.

The system is based on the principles of REST, meaningful endpoints, responses in the form of JSON, status codes, filtering, nested resources, and mapping exceptions.

---

## Technology Stack

- Java 21
- Maven
- JAX-RS (Jersey)
- Simple HTTP Server with Grizzly Service.
- IntelliJ IDEA
- HashMap /ArrayList (No database)

---

## How to Run

IntelliJ IDEA Open project.

Run:

Main.java

Server starts at:

http://localhost:8080/api/v1/

---

## API Endpoints

## Discovery

### GET

/api/v1/

Gets API metadata and resource links.

---

## Rooms

### GET all rooms

/api/v1/rooms

### POST create room

/api/v1/rooms

### GET room by id

/api/v1/rooms/{id}

### DELETE room

/api/v1/rooms/{id}

---

## Sensors

### GET all sensors

/api/v1/sensors

### GET filtered sensors

/api/v1/sensors?type=CO2

### POST create sensor

/api/v1/sensors

### GET sensor by id

/api/v1/sensors/{id}

---

## Sensor Readings

### GET reading history

/api/v1/sensors/{id}/readings

### POST add reading

/api/v1/sensors/{id}/readings

---

## Sample curl Commands

## 1. Discovery

curl http://localhost:8080/api/v1/

## 2. Create Room

curl -X POST localhost:8080/api/v1/rooms
-H "Content-Type: application/json" \
-d "{\"id\":\"LAB-101\",\"name\":\"Engineering Lab\",\"capacity\":30}"

## 3. Get Rooms

curl http://localhost:8080/api/v1/rooms

## 4. Create Sensor

The command looks like: curl -X POST http://localhost:8080/api/v1/sensors
-H "Content-Type: application/json" \
-d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":400,\"roomId\":\"LAB-101\"}"

## 5. Add Reading

curl -X POST localhost:8080/api/v1/sensors/CO2-001/readings
-H "Content-Type: application/json" \
-d "{\"id\":\"R1\",\"timestamp\":1713620000,\"value\":420}"

---

## Error Handling Implemented

### 409 Conflict

When sensors are allocated, room can not be deleted.

### 422 Unprocessable Entity

Sensor refers to a non-existent room.

### 403 Forbidden

Sensor in MAINTENANCE mode is not open to accept reading.

### 500 Internal Server Error

Fallback exception mapper of the world.

---

## Logging

All requests received:

- HTTP method
- URI

Log of all outgoing responses:

- Final status code

---

## REST Features Demonstrated

- Resource-based URI design
- Proper HTTP verbs
- JSON media types
- Query parameters
- Nested sub-resources
- Exception mappers
- Stateless communication

---

## Notes

The collections used in this project are in-memory collections only (`HashMap,ArrayList) as specified by the coursework specification.

---

## End
