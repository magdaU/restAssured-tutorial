# REST Assured Fundamentals

A Java-based API test project using the **REST Assured** library.  
Supporting code for the [Rest Assured Fundamentals](https://www.udemy.com/course/rest-assured-fundamentals/?referralCode=2A76479D71A62609414D) course on Udemy.

---

## 📋 Technology Stack

| Technology            | Version  |
|-----------------------|----------|
| Java                  | 18       |
| Maven                 | 3.x      |
| REST Assured          | 5.3.0    |
| JUnit                 | 4.13.2   |
| Jackson Databind      | 2.14.2   |
| JSON Schema Validator | 5.3.0    |

---

## 🏗️ Project Structure

```
src/
└── test/
    └── java/
        ├── config/
        │   ├── FootballConfig.java       # Base configuration for Football API
        │   ├── VideoGameConfig.java      # Base configuration for VideoGame API
        │   ├── VideoGameEndpoints.java   # VideoGame API endpoint constants
        │   └── VideoGameTests.java       # VideoGame API tests (CRUD, serialization, schemas)
        ├── objects/
        │   └── VideoGame.java            # VideoGame POJO model
        ├── FootbalTests.java             # Football API tests
        ├── GpathJSONTest.java            # GPath tests on JSON responses
        ├── GpathXMLTests.java            # GPath tests on XML responses
        └── MyFirstVideoGame.java         # Basic examples
src/
└── main/
    └── java/
        └── resources/
            ├── VideoGameJsonSchema.json  # JSON Schema for response validation
            └── VideoGameXSD.xsd          # XSD Schema for XML response validation
```

---

## 🎮 Video Game API

Tested API: **Video Game DB** – a simple, fictional video game database.

- Swagger UI: https://videogamedb.uk/swagger-ui/index.html
- Base URL: `https://videogamedb.uk/api/v2/`

> ⚠️ The API runs in **read-only mode** – create, update, and delete operations are not persisted.

### Endpoints

| Constant               | Path                         | Description                  |
|------------------------|------------------------------|------------------------------|
| `ALL_VIDEO_GAMES`      | `/videogame`                 | List all video games          |
| `SINGLE_VIDEO_GAME`    | `/videogame/{videoGameId}`   | Get details of a single game  |

### Tests (`VideoGameTests`)

- `getAllGames` – retrieve all games
- `createNewGameByJSON` – create a game via JSON body
- `createNewGameByXML` – create a game via XML body
- `updateGame` – update a game (PUT)
- `deleteGame` – delete a game (DELETE)
- `getSingleGame` – retrieve a single game by ID
- `testVideoGameSerializationJSON` – serialize a POJO object to JSON
- `testVideoGameSerializationXML` – validate XML response against XSD
- `testVideoGameSchemaJSON` – validate response against JSON Schema
- `convertJsonToPojo` – deserialize response into a `VideoGame` object
- `catureResponseTime` – measure response time
- `assertOnResponseTime` – assert response time is under 1000 ms

---

## ⚽ Football API

Tested API: **football-data.org** – football competition data.

- Documentation: https://www.football-data.org/
- Base URL: `https://api.football-data.org/v4/`

### Authentication – API token required

Football tests require an API token from [football-data.org](https://www.football-data.org/).  
Without a token, tests will be automatically skipped (`Assume`).

Provide the token in one of two ways:

**Environment variable (PowerShell):**
```powershell
$env:FOOTBALL_DATA_API_TOKEN="your-token"
mvn -Dtest=FootbalTests test
```

**Maven/JVM property:**
```powershell
mvn -Dfootball.api.token="your-token" -Dtest=FootbalTests test
```

### Tests (`FootbalTests`)

- `getDetailsOneAre` – retrieve data for a single area
- `getDetailsOfMultipleArea` – retrieve data for multiple areas
- `getDataFounded` – verify a team's founding year
- `getFirstTeamName` – get the name of the first team in a league
- `getAllTeamData` – display full team data
- `getAllTeamData_DoCheckFirst` – extract response with status code validation
- `extractHeaders` – read response headers
- `extractHeadersTeamName` – extract team name via `jsonPath()`
- `extractAllTeams` – list all teams in a league

---

## 🔍 GPath Tests

### GpathJSONTest (JSON)
Advanced querying of JSON responses using **GPath** (Groovy Path) syntax:

- `extractMapOfElementsWithFind` – find a team by name (`find`)
- `extractSingleValueWithFind` – find a player by ID
- `extractListOfValueWithFindAll` – list players matching a condition
- `extractSingleValueWithHighestNumber` – player with the highest ID (`max`)
- `extractMutlipleValuesAndSumThem` – sum of all player IDs (`collect + sum`)

### GpathXMLTests (XML)
Querying XML responses using **GPath** and **XmlPath**:

- `getFirstGameInList` – name of the first game in the list
- `getAttribute` – read an XML attribute
- `getListOfXMlNodes` – list of XML nodes (`findAll`)
- `getListOfXMLNodesByFindAllOnAttribute` – filter by category attribute
- `getSingleNode` – find a node by game name

---

## ▶️ Running Tests

### All tests
```powershell
mvn test
```

### VideoGame tests only
```powershell
mvn -Dtest=VideoGameTests test
```

### Football tests only (with token)
```powershell
$env:FOOTBALL_DATA_API_TOKEN="your-token"
mvn -Dtest=FootbalTests test
```

### GPath JSON / XML tests only
```powershell
mvn -Dtest=GpathJSONTest test
mvn -Dtest=GpathXMLTests test
```

---

## 📦 Maven Dependencies

```xml
<!-- REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.0</version>
    <scope>test</scope>
</dependency>

<!-- JSON Schema Validator -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-schema-validator</artifactId>
    <version>5.3.0</version>
    <scope>test</scope>
</dependency>

<!-- Jackson Databind (POJO serialization/deserialization) -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.14.2</version>
    <scope>test</scope>
</dependency>

<!-- JUnit -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>
```

---

## 🔧 Useful Tools

| Tool                | Description                             | Link                                    |
|---------------------|-----------------------------------------|-----------------------------------------|
| jsonschema2pojo     | Generate POJOs from JSON / JSON Schema  | https://www.jsonschema2pojo.org/        |
| freeformatter       | Formatters, validators, minifiers       | https://freeformatter.com/              |
| jsonschemavalidator | Interactive JSON Schema validator       | https://www.jsonschemavalidator.net/    |
