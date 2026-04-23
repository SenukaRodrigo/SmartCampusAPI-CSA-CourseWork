# Name: Senuka Rodrigo
# Westminster ID: W2120474
# IIT ID: 20231309

# Smart Campus API

Public GitHub repository for the Smart Campus Sensor and Room Management coursework API.

---

## API Design Overview

This project is a Java RESTful web application developed using **JAX-RS (Jersey)** and deployed on **Apache Tomcat** as a WAR file.

The API is designed to manage three core resources:

- **Rooms** – campus locations such as labs, libraries, lecture halls
- **Sensors** – monitoring devices assigned to rooms
- **Sensor Readings** – time-stamped historical readings recorded by sensors

The system follows a hierarchical resource structure:

```text
Room -> Sensor -> SensorReading
````

Meaning:

* A room can contain multiple sensors
* Each sensor belongs to one room
* Each sensor can store multiple readings

---

## API Base Path

All endpoints are available under:

```text
/api/v1
```

If deployed on Tomcat as:

```text
smart-campus-api.war
```

The full base URL becomes:

```text
http://localhost:8080/smart-campus-api/api/v1
```

---

## Main Endpoints

| Method | Endpoint                        | Purpose                |
| ------ | ------------------------------- | ---------------------- |
| GET    | `/api/v1/`                      | Discovery endpoint     |
| GET    | `/api/v1/rooms`                 | Get all rooms          |
| POST   | `/api/v1/rooms`                 | Create room            |
| GET    | `/api/v1/rooms/{id}`            | Get room by ID         |
| DELETE | `/api/v1/rooms/{id}`            | Delete room            |
| GET    | `/api/v1/sensors`               | Get all sensors        |
| GET    | `/api/v1/sensors?type=CO2`      | Filter sensors by type |
| POST   | `/api/v1/sensors`               | Create sensor          |
| GET    | `/api/v1/sensors/{id}`          | Get sensor by ID       |
| GET    | `/api/v1/sensors/{id}/readings` | Get reading history    |
| POST   | `/api/v1/sensors/{id}/readings` | Add reading            |

---

## Error Handling

Custom exception mappers are implemented.

| Status Code | Meaning                            |
| ----------- | ---------------------------------- |
| 404         | Resource not found                 |
| 409         | Room still contains linked sensors |
| 422         | Sensor references missing room     |
| 403         | Sensor under maintenance           |
| 415         | Unsupported media type             |
| 500         | Internal server error              |

---

# Build and Launch Server (Step-by-Step)

## Prerequisites

Install the following:

1. Java 21
2. Maven 3.8+
3. Apache Tomcat 10.1+

---

## Step 1 – Clone Repository

```bash
git clone https://github.com/SenukaRodrigo/SmartCampusAPI-CSA-CourseWork.git
cd SmartCampusApi
```

---

## Step 2 – Build the Project

```bash
mvn clean package
```

Expected output:

```text
target/smart-campus-api.war
```

---

## Step 3 – Deploy WAR to Tomcat

Copy the WAR file into Tomcat's `webapps` folder.

### Windows

```powershell
Copy-Item target\smart-campus-api.war C:\apache-tomcat-10\webapps\
```

### macOS / Linux

```bash
cp target/smart-campus-api.war /path/to/apache-tomcat/webapps/
```

---

## Step 4 – Start Tomcat

### Windows

```powershell
C:\apache-tomcat-10\bin\startup.bat
```

### macOS / Linux

```bash
/path/to/apache-tomcat/bin/startup.sh
```

---

## Step 5 – Verify Server

Open browser:

```text
http://localhost:8080/smart-campus-api/api/v1/
```

You should receive JSON discovery metadata.

---

# Sample curl Commands

Use this base URL:

```bash
BASE_URL="http://localhost:8080/smart-campus-api/api/v1"
```

---

## 1. Discovery Endpoint

```bash
curl -X GET "$BASE_URL/"
```

---

## 2. Create Room

```bash
curl -X POST "$BASE_URL/rooms" \
-H "Content-Type: application/json" \
-d '{"id":"LIB301","name":"Library Quiet Study","capacity":80}'
```

---

## 3. Get All Rooms

```bash
curl -X GET "$BASE_URL/rooms"
```

---

## 4. Create Sensor

```bash
curl -X POST "$BASE_URL/sensors" \
-H "Content-Type: application/json" \
-d '{"id":"CO2-01","type":"CO2","status":"ACTIVE","currentValue":0,"roomId":"LIB301"}'
```

---

## 5. Get Sensors by Type

```bash
curl -X GET "$BASE_URL/sensors?type=CO2"
```

---

## 6. Add Reading

```bash
curl -X POST "$BASE_URL/sensors/CO2-01/readings" \
-H "Content-Type: application/json" \
-d '{"id":"R001","timestamp":1710000000000,"value":650}'
```

---

## 7. Get Reading History

```bash
curl -X GET "$BASE_URL/sensors/CO2-01/readings"
```

---

## 8. Get Sensor by ID

```bash
curl -X GET "$BASE_URL/sensors/CO2-01"
```

---

# Technologies Used

* Java 21
* Maven
* Jersey (JAX-RS)
* Jackson JSON
* Apache Tomcat
* REST API Design

---

# Author

Senuka Rodrigo
Westminster ID: W2120474
IIT ID: 20231309

```
```

## Coursework Questions and Answers

### Part 1 - Setup & Discovery

### Question 1

The JAX-RS resource classes are normally handled by a per-request lifecycle. Each incoming HTTP request is served by creating a new resource object. This is a better approach to thread safety since there is no sharing of instance fields between concurrent users. Request is assigned to an isolated object instance and this minimizes the chances of accidental state leakage.
Singleton lifecycle permits only one common instance of a resource class to exist throughout the application. This may reduce the overhead of object creation but it introduces concurrency problems as two or more requests may be using the same object simultaneously.
In this project, shared in-memory stores such as the HashMap and ArrayList are used to store application data. These shared collections can be accessed by more than one request at a time even in cases where per-request resource instances are used. In this way, the lifecycle would enhance the safety of the objects, whereas synchronized structures or database transactions would be more appropriate in a production environment.

### Question 2

HATEOAS (Hypermedia as the Engine of Application State) is a REST architectural principle where API responses include links to related resources or available next actions. Instead of relying entirely on external documentation, the client can discover valid navigation paths dynamically from the server response.
For example, a discovery endpoint may return links such as:

- /api/v1/rooms
- /api/v1/sensors
  This benefits client developers because integrations become easier to maintain. If routes change or new endpoints are added, the client can follow server-provided links rather than hardcoding every URI. It also improves usability, self-documentation, and API discoverability compared with static documentation that may become outdated.

### Part 2 - Room Management

### Question 3

Returning only room IDs reduces response payload size and bandwidth consumption. It is efficient when the client only needs identifiers and can request full details separately when required.
Returning full room objects increases payload size but reduces the number of additional requests needed, because the client immediately receives all metadata such as room name, capacity, and associated sensors.
The trade-off is between network efficiency and client convenience. In this coursework project, returning full room objects is practical because the dataset is small and it simplifies testing and management operations.

### Question 4

Yes, DELETE is considered idempotent because repeating the same request should leave the server in the same final state.
For example:

- The first DELETE /rooms/LIB-301 removes the room successfully and returns 200 OK.
- A second DELETE /rooms/LIB-301 returns 404 Not Found because the room no longer exists.
  Although the response status changes between the first and subsequent calls, the state of the server remains unchanged after the first successful deletion. Therefore, the operation still satisfies idempotency because repeated identical requests do not create additional side effects beyond the initial deletion.

### Part 3 - Sensors & Filtering

### Question 5

When a JAX-RS method is annotated with:
```bash
@Consumes(MediaType.APPLICATION_JSON)
```
the server expects a JSON request body. If a client sends a request with Content-Type of text/plain or application/xml, Jersey attempts to locate a compatible message body reader for that media type.
If no provider supports that media type, the framework returns to a 415 Unsupported Media Type response. This is correct REST behavior because the request format does not match the contract defined by the endpoint annotation.

### Question 6

Filtering with query parameters such as:
```bash
/api/v1/sensors?type=CO2
```
is preferable because the client is still requesting the same collection resource (sensors) but applying filter criteria on top of it. A path-based approach such as:
```bash
/api/v1/sensors?type=CO2
```
implies a nested resource hierarchy rather than a search or filter operation, which is semantically misleading. Query parameters are also more flexible because multiple filters canbe combined easily:
```bash
/api/v1/sensors?type=CO2&status=ACTIVE
```
This makes them the standard REST approach for searching, sorting, and filtering collection resources without polluting the URI path structure.

### Part 4 - Sub-Resources

### Question 7

The Sub-Resource Locator pattern delegates nested resource paths to dedicated classes. In this project, requests to the path:
are handled by a separate SensorReadingResource class rather than placing all logic inside SensorResource. This provides several key architectural benefits:
```bash
/api/v1/sensors/{id}/readings
```
Separation of concerns - each class manages one logical responsibility.
Smaller, more readable classes - easier to understand and review.

- Easier maintenance and debugging - faults are isolated to specific classes.
- Improved unit testability - sub-resources can be tested independently.
- Simpler future expansion - new nested resources can be added without modifying existing classes.
  In large APIs, this pattern prevents one massive controller class from containing every route and responsibility, which would become increasingly difficult to maintain as the API grows.

### Part 5 - Error Handling & Logging

### Question 8

A request that contains valid JSON but references a non-existent roomId is syntactically correct. The endpoint URI itself exists and the request was successfully routed to the correct resource method.
Therefore, returning 422 Unprocessable Entity is more semantically accurate than 404 Not Found. A 404 status normally indicates that the requested URI resource does not exist. In this case, the URI is valid, but the submitted payload contains a reference to a resource that cannot be resolved - making it a semantic or business logic error rather than a routing failure. HTTP 422 communicates precisely that the request was well-formed but could not be processed due to invalid content.

### Question 9

Returning raw Java stack traces to external API consumers presents significant security risks. An attacker can extract the following types of sensitive information from an exposed trace:

- Internal package and class names, revealing the application's architecture.
- Framework and library versions, which can be cross-referenced with known CVEs.
- Server file paths and directory structures.
- Method names and logic flow, enabling targeted exploitation of business logic.
- Evidence of third-party dependencies and possible unpatched vulnerabilities.
  This information can support reconnaissance and targeted attacks against the application. The correct approach is to return a generic 500 Internal Server Error response to external consumers while logging the full diagnostic details internally for developer review only.

### Question 10

JAX-RS filters such as ContainerRequestFilter and ContainerResponseFilter are specifically designed to handle cross-cutting concerns like logging, authentication, metrics, and auditing - concerns that apply uniformly across all endpoints.
Using filters is preferable to manually writing Logger.info() inside every endpoint because:

- Logging logic is centralized in one place, making it easy to update globally.
- It eliminates repeated code across all resource methods.
- It guarantees consistent logging for every request and response automatically.
- It keeps business logic classes clean and focused on their core responsibility.
- It is easier to maintain as the number of endpoints grows over time.
  This approach follows the principle of separation of concerns and significantly improves the long-term scalability and maintainability of the application.
