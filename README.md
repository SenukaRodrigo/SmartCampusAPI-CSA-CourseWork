# Smart Campus Sensor & Room Management API

## Module

5COSC022W - Client-Server Architectures

## Coursework Title

Smart Campus Sensor & Room Management API

## Student

Senuka Rodrigo  
W2120474  
20231309

---

## 1. API Design Overview

This project is a RESTful backend service for a Smart Campus environment. It manages campus rooms, linked sensors inside rooms, and historical sensor readings.

### Resource Hierarchy

- Room
  - Sensor (linked to a room with `roomId`)
    - SensorReading (nested under sensor)

```text
Rooms -> Sensors -> SensorReadings
```

### Base URL

```text
http://localhost:8080/api/v1
```

### Implemented Endpoints

| Method | Endpoint                        | Description                                     |
| ------ | ------------------------------- | ----------------------------------------------- |
| GET    | `/api/v1/`                      | Discovery endpoint (API metadata and links)     |
| GET    | `/api/v1/rooms`                 | Get all rooms                                   |
| POST   | `/api/v1/rooms`                 | Create a new room                               |
| GET    | `/api/v1/rooms/{id}`            | Get a room by ID                                |
| DELETE | `/api/v1/rooms/{id}`            | Delete a room (blocked if sensors are assigned) |
| GET    | `/api/v1/sensors`               | Get all sensors (`?type=CO2` supported)         |
| POST   | `/api/v1/sensors`               | Create a new sensor linked to a room            |
| GET    | `/api/v1/sensors/{id}`          | Get a sensor by ID                              |
| GET    | `/api/v1/sensors/{id}/readings` | Get reading history for a sensor                |
| POST   | `/api/v1/sensors/{id}/readings` | Add a sensor reading                            |

---

## 2. Technology Stack

- Java 21
- JAX-RS implementation: Jersey
- Embedded server: Grizzly HTTP server
- Maven as build tool
- No database (in-memory `HashMap` and `ArrayList` collections)

---

## 3. Project Structure

```text
src/
	main/
		java/
			com/coursework/smartcampus/
				model/           (Room, Sensor, SensorReading)
				exception/       (custom exceptions + mappers)
				filter/          (logging filter)
				repository/      (in-memory DataStore)
				DiscoveryResource.java
				RoomResource.java
				SensorResource.java
				SensorReadingResource.java
				Main.java        (main entry point)
```

---

## 4. How to Build & Run (Step-by-Step)

### Prerequisites

- Java 21 installed
- Maven installed (recommended Maven 3.8+)

### Steps

```bash
# Clone the repo
git clone <your-repo-url>

# Navigate into the project
cd SmartCampusApi

# Build with Maven
mvn clean install

# Run the server
mvn exec:java

# Optional: run as jar if you package an executable jar
java -jar target/smart-campus-api-1.0-SNAPSHOT.jar
```

Server starts on:

```text
http://localhost:8080/api/v1/
```

---

## 5. Sample curl Commands (At Least 5)

```bash
# 1. Discovery endpoint
curl -X GET http://localhost:8080/api/v1/

# 2. Create a room
curl -X POST http://localhost:8080/api/v1/rooms \
	-H "Content-Type: application/json" \
	-d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'

# 3. Create a sensor linked to a room
curl -X POST http://localhost:8080/api/v1/sensors \
	-H "Content-Type: application/json" \
	-d '{"id":"CO2-301","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"LIB-301"}'

# 4. Get all sensors filtered by type
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"

# 5. Post a sensor reading
curl -X POST http://localhost:8080/api/v1/sensors/CO2-301/readings \
	-H "Content-Type: application/json" \
	-d '{"id":"R-001","timestamp":1713620000,"value":420}'

# 6. Error case: create sensor with non-existent room (triggers 422)
curl -X POST http://localhost:8080/api/v1/sensors \
	-H "Content-Type: application/json" \
	-d '{"id":"TEMP-999","type":"TEMPERATURE","status":"ACTIVE","currentValue":22.5,"roomId":"UNKNOWN-ROOM"}'

# 7. Error case: delete room with sensors assigned (triggers 409)
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

---

## 6. Answers to Report Questions

Note: Include these answers in README and in your submitted PDF report for safety if both formats are requested.

### Part 1 Questions

#### Q1: JAX-RS Resource Lifecycle

[Write your answer here]

#### Q2: HATEOAS

[Write your answer here]

### Part 2 Questions

#### Q1: [Insert question title]

[Write your answer here]

#### Q2: [Insert question title]

[Write your answer here]

### Part 3 Questions

#### Q1: [Insert question title]

[Write your answer here]

#### Q2: [Insert question title]

[Write your answer here]

### Part 4 Questions

#### Q1: [Insert question title]

[Write your answer here]

#### Q2: [Insert question title]

[Write your answer here]

### Part 5 Questions

#### Q1: [Insert question title]

[Write your answer here]

#### Q2: [Insert question title]

[Write your answer here]

---

## Quick Checklist

- [ ] API overview written
- [ ] Tech stack listed
- [ ] Project structure shown
- [ ] Build and run steps are clear and accurate
- [ ] At least 5 curl commands included
- [ ] All question answers included, organized by part
