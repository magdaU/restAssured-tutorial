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
├── test/
│   ├── java/
│   │   ├── config/
│   │   │   ├── FootballConfig.java       # Base configuration for Football API
│   │   │   ├── VideoGameConfig.java      # Base configuration for VideoGame API
│   │   │   ├── VideoGameEndpoints.java   # VideoGame API endpoint constants
│   │   │   └── VideoGameTests.java       # VideoGame API tests (CRUD, serialization, schemas)
│   │   ├── objects/
│   │   │   └── VideoGame.java            # VideoGame POJO model
│   │   ├── FootbalTests.java             # Football API tests
│   │   ├── GpathJSONTest.java            # GPath tests on JSON responses
│   │   ├── GpathXMLTests.java            # GPath tests on XML responses
│   │   └── MyFirstVideoGame.java         # Basic examples
│   └── resources/
│       ├── VideoGameJsonSchema.json      # JSON Schema for response validation
│       └── VideoGameXSD.xsd              # XSD Schema for XML response validation
└── main/
    └── java/
        └── resources/                    # (legacy location, not used by Maven)
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
- `testVideoGameSerializationXML` – validate XML response against XSD (`VideoGameXSD.xsd`)
- `testVideoGameSchemaJSON` – validate response against JSON Schema (`VideoGameJsonSchema.json`)
- `convertJsonToPojo` – deserialize response into a `VideoGame` object
- `catureResponseTime` – capture and print response time in milliseconds
- `assertOnResponseTime` – assert response time is under 1000 ms
- `getAllGamesVerifyListNotEmpty` – assert the returned game list is not empty
- `getSingleGameVerifyFields` – assert `id`, `name` and `category` fields on a single game response

---

## ⚽ Football API

Tested API: **football-data.org** – football competition data.

- Documentation: https://www.football-data.org/
- Base URL: `https://api.football-data.org/v4/`

### Authentication – API token required

Football tests require an API token from [football-data.org](https://www.football-data.org/).  
Without a token all Football tests return **HTTP 403 Forbidden**.

> ⚠️ The free plan allows **10 requests per minute**. Running the full test suite twice in quick succession returns **HTTP 429 Too Many Requests**. Wait ~1 minute between runs if you hit this limit.

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
- `getCompetitions` – retrieve all competitions and assert list is not empty
- `getTopScorersForPremierLeague` – retrieve top scorers for Premier League (competition `2021`)
- `getStandingsForPremierLeague` – retrieve standings table for Premier League

---

## 🔍 GPath Tests

### GpathJSONTest (JSON)
Advanced querying of JSON responses using **GPath** (Groovy Path) syntax against the VideoGame API:

- `extractMapOfElementsWithFind` – find a game by name using `find { it.name == '...' }`
- `extractSingleValueWithFind` – find a game by ID using `find { it.id == 1 }`
- `extractListOfValueWithFindAll` – list games with `reviewScore > 70` using `findAll`
- `extractSingleValueWithHighestNumber` – game with the highest ID using `max { it.id }`
- `extractMutlipleValuesAndSumThem` – sum of all game IDs using `collect { it.id }.sum()`

### GpathXMLTests (XML)
Querying XML responses using **GPath** and **XmlPath** against the VideoGame API.

> XML response structure: `<List><item category="..."><id/><name/><releaseDate/><reviewScore/><rating/></item></List>`

- `getFirstGameInList` – name of the first game: `List.item[0].name`
- `getAttribute` – read the `category` attribute: `List.item[0].@category`
- `getListOfXMlNodes` – all game names as a list: `List.item.name`
- `getListOfXMLNodesByFindAllOnAttribute` – filter games by category: `findAll { it.@category == 'Shooter' }`
- `getSingleNode` – find a specific game by name: `find { it.name == 'Resident Evil 4' }`

---

## 🧪 Test Cases

Detailed business-level descriptions of each test with step-by-step scenarios.

---

### 🎮 VideoGameTests

---

#### `getAllGames`
| | |
|---|---|
| **Step 1** | Set base URI to `https://videogamedb.uk/api/v2/` |
| **Step 2** | Send a `GET` request to `/videogame` |
| **Step 3** | Verify response status code is `200 OK` |
| **Expected** | A JSON list of all available video games is returned |

---

#### `getSingleGame`
| | |
|---|---|
| **Step 1** | Set base URI to `https://videogamedb.uk/api/v2/` |
| **Step 2** | Send a `GET` request to `/videogame/1` |
| **Step 3** | Verify response status code is `200 OK` |
| **Step 4** | Assert the returned game ID equals `1` |
| **Expected** | A single video game object is returned with correct ID |

---

#### `createNewGameByJSON`
| | |
|---|---|
| **Step 1** | Authenticate with `POST /authenticate` to obtain a JWT token |
| **Step 2** | Build a JSON request body with game name, category, rating, and release year |
| **Step 3** | Send a `POST` request to `/videogame` with `Content-Type: application/json` |
| **Step 4** | Verify response status code is `200 OK` |
| **Expected** | API confirms the new game has been received (not persisted in read-only mode) |

---

#### `createNewGameByXML`
| | |
|---|---|
| **Step 1** | Authenticate and obtain a JWT token |
| **Step 2** | Build an XML request body with game attributes |
| **Step 3** | Send a `POST` request to `/videogame` with `Content-Type: application/xml` |
| **Step 4** | Verify response status code is `200 OK` |
| **Expected** | API accepts the XML payload and confirms the operation |

---

#### `updateGame`
| | |
|---|---|
| **Step 1** | Authenticate and obtain a JWT token |
| **Step 2** | Build a JSON body with updated game data |
| **Step 3** | Send a `PUT` request to `/videogame/3` |
| **Step 4** | Verify response status code is `200 OK` |
| **Expected** | API confirms the update (not persisted in read-only mode) |

---

#### `deleteGame`
| | |
|---|---|
| **Step 1** | Authenticate and obtain a JWT token |
| **Step 2** | Send a `DELETE` request to `/videogame/3` |
| **Step 3** | Verify response status code is `200 OK` |
| **Expected** | API confirms the deletion (not persisted in read-only mode) |

---

#### `testVideoGameSchemaJSON`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame` |
| **Step 2** | Load `VideoGameJsonSchema.json` from resources |
| **Step 3** | Validate the full response body against the JSON Schema |
| **Expected** | Response structure matches the defined schema without validation errors |

---

#### `testVideoGameSerializationXML`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame` with `Accept: application/xml` |
| **Step 2** | Load `VideoGameXSD.xsd` from resources |
| **Step 3** | Validate the XML response against the XSD schema |
| **Expected** | XML response is valid according to the XSD definition |

---

#### `convertJsonToPojo`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame/1` |
| **Step 2** | Deserialize the JSON response body into a `VideoGame` POJO |
| **Step 3** | Assert that the object fields (id, name, category) are not null |
| **Expected** | JSON successfully maps to a `VideoGame` Java object |

---

#### `assertOnResponseTime`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame` |
| **Step 2** | Capture the total response time |
| **Step 3** | Assert that response time is less than `1000 ms` |
| **Expected** | API responds within the defined SLA threshold |

---

### ⚽ FootbalTests

---

#### `getDetailsOneAre`
| | |
|---|---|
| **Step 1** | Set base URI to `https://api.football-data.org/v4/` |
| **Step 2** | Add header `X-Auth-Token` with the API token |
| **Step 3** | Send a `GET` request to `/areas/2072` |
| **Step 4** | Verify response status code is `200 OK` |
| **Expected** | Area details object is returned with correct ID and name |

---

#### `getDetailsOfMultipleArea`
| | |
|---|---|
| **Step 1** | Authenticate with the API token header |
| **Step 2** | Send a `GET` request to `/areas` |
| **Step 3** | Verify response status code is `200 OK` |
| **Step 4** | Assert the response contains multiple area entries |
| **Expected** | A list of multiple geographic areas is returned |

---

#### `getDataFounded`
| | |
|---|---|
| **Step 1** | Authenticate with the API token header |
| **Step 2** | Send a `GET` request to `/teams/{teamId}` |
| **Step 3** | Verify response status code is `200 OK` |
| **Step 4** | Extract the `founded` field from the response |
| **Step 5** | Assert the founding year equals the expected value |
| **Expected** | Correct founding year is returned for the team |

---

#### `getFirstTeamName`
| | |
|---|---|
| **Step 1** | Authenticate with the API token header |
| **Step 2** | Send a `GET` request to `/competitions/PL/teams` |
| **Step 3** | Verify response status code is `200 OK` |
| **Step 4** | Extract the first team name from the `teams[0].name` path |
| **Expected** | The first team name from the Premier League is returned |

---

#### `getAllTeamData_DoCheckFirst`
| | |
|---|---|
| **Step 1** | Authenticate with the API token header |
| **Step 2** | Send a `GET` request to `/competitions/PL/teams` |
| **Step 3** | Extract the full `Response` object |
| **Step 4** | Assert response status code is `200 OK` |
| **Step 5** | Print all team names from the response |
| **Expected** | Status is validated before extracting and displaying team data |

---

#### `extractHeaders`
| | |
|---|---|
| **Step 1** | Authenticate with the API token header |
| **Step 2** | Send a `GET` request to `/competitions/PL/teams` |
| **Step 3** | Extract all response headers |
| **Step 4** | Print each header name and value |
| **Expected** | All HTTP response headers are accessible and printable |

---

#### `extractAllTeams`
| | |
|---|---|
| **Step 1** | Authenticate with the API token header |
| **Step 2** | Send a `GET` request to `/competitions/PL/teams` |
| **Step 3** | Verify response status code is `200 OK` |
| **Step 4** | Use `jsonPath()` to extract the full list of team names |
| **Step 5** | Assert the list is not empty and print all team names |
| **Expected** | All Premier League team names are returned in a list |

---

### 🔍 GpathJSONTest

---

#### `extractMapOfElementsWithFind`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/competitions/PL/teams` |
| **Step 2** | Apply GPath expression: `teams.find { it.name == 'Arsenal FC' }` |
| **Step 3** | Extract the matching team as a Map |
| **Expected** | A single team map with Arsenal FC data is returned |

---

#### `extractListOfValueWithFindAll`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/competitions/PL/teams` |
| **Step 2** | Apply GPath `findAll` to find teams founded after year 1900 |
| **Step 3** | Extract the list of matching team names |
| **Expected** | A filtered list of team names satisfying the condition is returned |

---

#### `extractSingleValueWithHighestNumber`
| | |
|---|---|
| **Step 1** | Send a `GET` request to the video game endpoint |
| **Step 2** | Apply GPath `max { it.id }` to find the game with the highest ID |
| **Step 3** | Extract the game name |
| **Expected** | The name of the game with the maximum ID is returned |

---

#### `extractMutlipleValuesAndSumThem`
| | |
|---|---|
| **Step 1** | Send a `GET` request to the video game endpoint |
| **Step 2** | Apply GPath `collect { it.id }` to extract all IDs |
| **Step 3** | Call `.sum()` on the collected list |
| **Expected** | The total sum of all game IDs is returned as a number |

---

### 🔍 GpathXMLTests

---

#### `getFirstGameInList`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame` with `Accept: application/xml` |
| **Step 2** | Parse the XML response with `XmlPath` |
| **Step 3** | Extract `videoGames.videoGame[0].name` |
| **Expected** | The name of the first game in the XML list is returned |

---

#### `getAttribute`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame` with `Accept: application/xml` |
| **Step 2** | Parse the XML response |
| **Step 3** | Extract an attribute value from a specific XML element |
| **Expected** | The correct XML attribute value is returned |

---

#### `getListOfXMLNodesByFindAllOnAttribute`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame` with `Accept: application/xml` |
| **Step 2** | Apply GPath `findAll { it.@category == 'Shooter' }` on the XML response |
| **Step 3** | Extract the list of matching game nodes |
| **Expected** | All games belonging to the 'Shooter' category are returned |

---

#### `getSingleNode`
| | |
|---|---|
| **Step 1** | Send a `GET` request to `/videogame` with `Accept: application/xml` |
| **Step 2** | Apply GPath `find { it.name == 'Resident Evil 4' }` |
| **Step 3** | Extract the matching XML node |
| **Expected** | The XML node for 'Resident Evil 4' is returned with all its attributes |

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

### GPath tests only
```powershell
mvn -Dtest=GpathJSONTest test
mvn -Dtest=GpathXMLTests test
```

### All tests except Football (no token required)
```powershell
mvn "-Dtest=VideoGameTests,GpathJSONTest,GpathXMLTests,MyFirstVideoGame" test
```

### Football tests only (API token required)
```powershell
$env:FOOTBALL_DATA_API_TOKEN="your-token"
mvn -Dtest=FootbalTests test
```

> **Note:** Football tests require a free API token from [football-data.org](https://www.football-data.org/). Without a token all Football tests return HTTP 403. The free plan allows 10 requests/min — HTTP 429 is returned if the limit is exceeded.

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
