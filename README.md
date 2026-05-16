# SmartBadge ADL Management Service

**SMARTBADGE ADL SERVICE** — Spring Modulith + MongoDB + Docker

## Architecture

```
com.smartbadge.adl
├── SmartBadgeApplication.java
├── shared/          [OPEN MODULE]  ApiResponse, GlobalExceptionHandler, OpenApiConfig
├── staff/           [MODULE]       Staff CRUD, Address management, Profile aggregation
│   └── dto/         [INTERNAL]    CreateStaffRequest, UpdateStaffRequest
├── leave/           [MODULE]       Leave documents & entries
│   └── dto/         [INTERNAL]    CreateLeaveEntryRequest, UpdateLeaveEntryRequest
└── team/            [MODULE]       Team documents & members
    └── dto/         [INTERNAL]    AddTeamMemberRequest, UpdateTeamMemberRequest
```

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 17 |
| Spring Boot | 3.3.5 |
| Spring Modulith | 1.2.5 |
| Spring Data MongoDB | (via Boot BOM) |
| SpringDoc OpenAPI | 2.6.0 |
| MongoDB | 7.0 |
| Docker | 24+ |

## Quick Start

### Option A — Docker Compose (recommended)

```bash
cp .env.example .env
docker-compose up -d
```

App: http://localhost:8080  
Swagger UI: http://localhost:8080/swagger-ui.html

### Option B — Local (Maven + MongoDB)

```bash
# Start MongoDB locally
docker run -d -p 27017:27017 --name mongo mongo:7.0

# Run the app
mvn spring-boot:run
```

## Key API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/staff/{id}/profile` | **Full aggregated profile** (staff + leave + team) |
| GET | `/api/v1/staff` | List staff (paginated) |
| POST | `/api/v1/staff` | Create staff |
| PUT | `/api/v1/staff/{id}` | Update staff |
| DELETE | `/api/v1/staff/{id}` | Delete staff |
| POST/PUT/DELETE | `/api/v1/staff/{id}/addresses/{type}` | Manage addresses |
| GET | `/api/v1/leave/{id}` | Leave document |
| POST | `/api/v1/leave/{id}/entries` | Add leave entry |
| PUT | `/api/v1/leave/{id}/entries/{ref}` | Update leave entry |
| GET | `/api/v1/team/{id}` | Team document |
| POST | `/api/v1/team/{id}/members` | Add team member |

## Module Architecture Test

```bash
mvn test -Dtest=ModularityTests
# Output: target/modulith-docs/  (PlantUML diagrams + docs)
```

## Sample Request: Create Staff

```json
POST /api/v1/staff
{
  "staffId": "E4861934",
  "name": "Sivaraj Thavamani",
  "email": "stsivaraj@gmail.com",
  "title": "MR",
  "jobTitle": "Senior Software Engineer",
  "grade": "IT.07",
  "businessCardType": "SMARTBADGE",
  "addresses": [
    {
      "address": "RESIDENCE",
      "street": "1st street",
      "city": "Pisanathur",
      "country": "India",
      "addressType": "RESIDENCE"
    }
  ]
}
```

## Health & Monitoring

- Health: http://localhost:8080/actuator/health
- Info:   http://localhost:8080/actuator/info
- Metrics: http://localhost:8080/actuator/metrics

## Production Notes

- `SPRING_PROFILES_ACTIVE=prod` activates `application-prod.yml`
- Set `MONGODB_URI` environment variable with credentials
- JVM flags: `-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0`
- Container runs as non-root user `appuser`
