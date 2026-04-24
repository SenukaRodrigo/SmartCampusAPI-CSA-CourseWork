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

The JAX-RS resource classes are typically managed by a per request lifecycle. Every new HTTP
request is served by the means of the creation of a new resource object. This is a more effective way
of thread safety as no instance fields are shared by multiple users of the same instance. Request is
given to a single object instance and this reduces the possibility of state leakage.
Singleton lifecycle only allows a single common instance of a resource class to exist within the
application. This can minimize overhead of creating objects but it brings about concurrency issues
where two or more requests can be operating the same object at the same time.
Shared-in-memory stores like the HashMap and the ArrayList are applied in storing application data
in this project. Even in situations where resource instances are per-request, these shared collections
may be accessed by more than a single request at any particular time. By doing so, the lifecycle would
improve the security of the objects, but synchronized structures or database transactions would prove
to be more suitable in a production context.

### Question 2

HATEOAS (Hypermedia as the Engine of Application State) is a principle of the REST architecture,
in which the response of an API contains links to other resources or actions to be taken next. The
client does not need to completely rely on external documentation, but can find legitimate navigation
paths dynamically based on the server response. As an example, a discovery endpoint could give out
links like: 

- /api/v1/rooms
- /api/v1/sensors

This is an advantage to client developers since the integrations are easier to maintain. In case of route
changes or addition of new destinations, the client may use links provided by the server instead of
manually coding all the URIs. It further enhances usability, self-documentation, and API
discoverability over the alternative of having a static documentation, which might be outdated.

### Part 2 - Room Management

### Question 3

Sending back room IDs only minimizes the size of the response and bandwidth usage. It is also
effective where the client is only required to identify and seek the detailed information once it is
necessary. Fully returning room objects can make the payload bigger, but require fewer subsequent
requests, since the client is immediately given all metadata about the room, including its name,
capacity, and any sensors that may be present. The trade-off is on network efficiency and client
convenience. Full room object returning is feasible in this coursework project since the dataset is very
small and makes the process of testing and management very easy.

### Question 4

Yes, DELETE is an idempotent operation since another request with the same request should put the
server in the same final state. For example: 

- The first DELETE /rooms/LIB-301 removes the room successfully and returns 200 OK.
- A second DELETE /rooms/LIB-301 returns 404 Not Found because the room no longer exists.

The status of the response varies on the first and the second calls to the server, but the first successful
deletion leaves the server in the same state. Thus, the operation remains idempotent since multiple
requests of the same nature will not generate any extra side effects to the original deletion.
### Part 3 - Sensors & Filtering

### Question 5

Annotating a JAX-RS method with:
```bash
@Consumes(MediaType.APPLICATION_JSON)
```
the server is expecting a JSON request body. When a client submits a request where the ContentType is text/plain or application/xml, Jersey will seek to find an appropriate message body reader that
can be used with that media type. In case no provider handles that type of media, the framework gives
a 415 Unsupported Media Type response. This is proper REST behavior since the format of the
request is not the contract as described in the endpoint annotation.

### Question 6

Filtering, query parameters: 
```bash
/api/v1/sensors?type=CO2
```
is better since the client continues to request the same collection resource (sensors) but with filter
criteria over it. Some path-based strategy like: 
```bash
/api/v1/sensors?type=CO2
```
is semantically misleading, and means a nested resource hierarchy instead of a search or a filter
operation. The query parameters are also more adaptable as it is easy to combine multiple filters: 
```bash
/api/v1/sensors?type=CO2&status=ACTIVE
```
This causes them to be the standard REST method of searching, sorting and filtering collection
resources without contaminating the URI path structure.

### Part 4 - Sub-Resources

### Question 7

Sub-Resource Locator pattern assigns the nested resource paths to sub-resource locator classes. The
path in this project: are processed by another SensorReadingResource class instead of all logic being
in SensorResource. This offers a number of important architectural advantages:
```bash
/api/v1/sensors/{id}/readings
```
Separation of concerns - a single logical responsibility is handled by each class. Shorter, more
accessible classes - simpler to read and look over. 

- Less maintenance and debugging Faults are contained in individual classes.
- Better unit testability - sub-resources are testable.
- Simpler future expansion - it is possible to add new nested resources without changing existing
classes.

### Part 5 - Error Handling & Logging

### Question 8

A request with a correctly-formed JSON but an invalid roomId is syntactically correct. The endpoint
URL itself is there and the request was properly directed to the right method of the resource.
Hence, it is more semantically correct to send 422 Unprocessable Entity rather than send 404 Not
Found. The 404 status is used to show that the resource in the given URI is not available. Here, the
URI is correct, but the payload provided has a reference to a resource which cannot be resolved, and
is a semantic or business logic error and not a routing error. The HTTP 422 status code conveys the
exact meaning that the request was indeed valid, but that it failed to be processed because of invalid
content.


### Question 9

It is highly insecure to simply give raw Java stack traces to external API end users. A trace with an
exposure to an attacker can be used to extract the following types of sensitive information: 

- Internal package and class names, exposing the structure of the application.
- Versions of frameworks and libraries, which can be cross-referenced with known CVEs.
- Pathnames to server files and directories.
- Name of methods and flow of logic, which allows the business logic to be exploited in a
directed way.
- Signs of third-party dependencies and potential unpatched vulnerabilities. 

This data could be used to back reconnaissance and precise assaults to the application. The appropriate
solution is to send a generic 500 Internal Server Error back to the external consumers and to record
the complete diagnostics information internally, to be looked at by the developers.

### Question 10

It is beneficial to use ContainerRequestFilter and ContainerResponseFilter since they are intended to
deal with cross-cutting concerns, like logging, authentication, metrics, and auditing that must cut
across all API endpoints. Here the logging filter was introduced such that all incoming requests and
outgoing responses are automatically logged in a single point as opposed to manually adding
Logger.info() statements within each resource method.
As an example, a filter can record the HTTP method, requested URL and response status code when
a client requests to create a room or retrieve sensors. This prevents the repetition of the same logging
code in different classes, all the endpoints are logged in a standard format and the resource classes
can only be involved in their business logic like creating a room or controlling sensors.
The filter automatically applies as the API expands and additional endpoints are introduced,
simplifying the overall maintenance of the system, increasing scalability, and improving structure via
separation of concerns.
