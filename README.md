# Smart Campus API

Public repository for the Smart Campus Sensor and Room Management coursework API.

## API Design Overview

This project exposes a REST API for managing:

- Rooms in the campus
- Sensors assigned to rooms
- Time-stamped readings recorded by each sensor

### Design Model

The API follows a hierarchical resource model:

- Room is a top-level resource
- Sensor belongs to one Room (linked by `roomId`)
- SensorReading is nested under a Sensor

Resource flow:

```text
Room -> Sensor -> SensorReading
```

### API Base Path

All endpoints are mounted under:

```text
/api/v1
```

If deployed as `smart-campus-api-1.0-SNAPSHOT.war` on Tomcat, the full base URL is typically:

```text
http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1
```

### Main Endpoints

| Method | Endpoint                        | Purpose                                        |
| ------ | ------------------------------- | ---------------------------------------------- |
| GET    | `/api/v1/`                      | Discovery endpoint with API metadata and links |
| GET    | `/api/v1/rooms`                 | List all rooms                                 |
| POST   | `/api/v1/rooms`                 | Create a room                                  |
| GET    | `/api/v1/rooms/{id}`            | Get room by ID                                 |
| DELETE | `/api/v1/rooms/{id}`            | Delete room (blocked if sensors still linked)  |
| GET    | `/api/v1/sensors`               | List sensors (supports `?type=...`)            |
| POST   | `/api/v1/sensors`               | Create sensor linked to room                   |
| GET    | `/api/v1/sensors/{id}`          | Get sensor by ID                               |
| GET    | `/api/v1/sensors/{id}/readings` | List readings for a sensor                     |
| POST   | `/api/v1/sensors/{id}/readings` | Add reading for a sensor                       |

### Error Handling

Custom exception mappers provide clear HTTP responses for key constraints:

- `422 Unprocessable Entity`: sensor references a room that does not exist
- `409 Conflict`: deleting a room that still has sensors
- `403 Forbidden`: posting a reading to a sensor in MAINTENANCE state

## Build and Launch Server (Step-by-Step)

### Prerequisites

1. Java 21
2. Maven 3.8+
3. Apache Tomcat 10.1+ (Jakarta Servlet compatible)

### 1. Clone Repository

```bash
git clone <your-public-repo-url>
cd SmartCampusApi
```

### 2. Build the Project

```bash
mvn clean package
```

Expected output artifact:

```text
target/smart-campus-api-1.0-SNAPSHOT.war
```

### 3. Deploy the WAR to Tomcat

Copy the WAR file to Tomcat's `webapps` directory.

Windows example:

```powershell
Copy-Item target/smart-campus-api-1.0-SNAPSHOT.war C:\path\to\apache-tomcat\webapps\
```

### 4. Start Tomcat

Windows:

```powershell
C:\path\to\apache-tomcat\bin\startup.bat
```

macOS/Linux:

```bash
/path/to/apache-tomcat/bin/startup.sh
```

### 5. Verify Server is Running

Open:

```text
http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/
```

You should receive JSON metadata for the API discovery endpoint.

## Sample curl Commands (Successful Interactions)

Set a base URL first:

```bash
BASE_URL="http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1"
```

For PowerShell, use this equivalent:

```powershell
$BASE_URL = "http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1"
```

1. Discovery endpoint

```bash
curl -X GET "$BASE_URL/"
```

2. Create a room

```bash
curl -X POST "$BASE_URL/rooms" \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'
```

3. Get all rooms

```bash
curl -X GET "$BASE_URL/rooms"
```

4. Create a sensor linked to the room

```bash
curl -X POST "$BASE_URL/sensors" \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-301","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"LIB-301"}'
```

5. Get sensors filtered by type

```bash
curl -X GET "$BASE_URL/sensors?type=CO2"
```

6. Add a reading to that sensor

```bash
curl -X POST "$BASE_URL/sensors/CO2-301/readings" \
  -H "Content-Type: application/json" \
  -d '{"id":"R-001","timestamp":1713620000,"value":420.5}'
```

7. Retrieve reading history for the sensor

```bash
curl -X GET "$BASE_URL/sensors/CO2-301/readings"
```

8. Retrieve one room by ID

```bash
curl -X GET "$BASE_URL/rooms/LIB-301"
```

Note for PowerShell users: If `curl` maps to `Invoke-WebRequest`, use `curl.exe` instead.
