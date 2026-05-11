# REST Assured Fundamentals

A Java-based API test project using the **REST Assured** library.  
Supporting code for the [Rest Assured Fundamentals](https://www.udemy.com/course/rest-assured-fundamentals/?referralCode=2A76479D71A62609414D) course on Udemy.

[![Allure Report](https://img.shields.io/badge/Allure_Report-view%20results-orange)](https://magdau.github.io/restAssured-tutorial/)
[![CI](https://github.com/magdaU/restAssured-tutorial/actions/workflows/allure-report.yml/badge.svg)](https://github.com/magdaU/restAssured-tutorial/actions/workflows/allure-report.yml)

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
| Allure Report         | 2.27.0   |

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

| Test | Description | Typical result | Failure reason |
|---|---|---|---|
| `getDetailsOneAre` | GET `/areas?areas=2076` | ✅ Pass | HTTP 500 – transient server error |
| `getDetailsOfMultipleArea` | GET `/areas` with multiple IDs | ✅ Pass | HTTP 500 – transient server error |
| `getDataFounded` | Assert Arsenal founded year = 1886 | ✅ Pass | HTTP 500 – transient server error |
| `getFirstTeamName` | Assert Arsenal FC is in Premier League team list | ✅ Pass | HTTP 500 – transient server error |
| `getAllTeamData` | Print full team JSON | ✅ Pass | HTTP 500 – transient server error |
| `getAllTeamData_DoCheckFirst` | Extract response with status code check | ✅ Pass | HTTP 500 – transient server error |
| `extractHeaders` | Print response headers | ✅ Pass | HTTP 500 – transient server error |
| `extractHeadersTeamName` | Extract team name via `jsonPath()` | ✅ Pass | HTTP 500 – transient server error |
| `extractAllTeams` | List all Premier League teams | ✅ Pass | HTTP 500 – transient server error |
| `getCompetitions` | Assert competitions list is not empty | ✅ Pass | HTTP 500 – transient server error |
| `getTopScorersForPremierLeague` | Assert scorers list is not empty | ✅ Pass | HTTP 500 – transient server error |
| `getStandingsForPremierLeague` | Assert standings list is not empty | ✅ Pass | HTTP 500 – transient server error |

> ⚠️ All Football tests depend on an external live API. Failures with HTTP 500 indicate a transient server-side issue at football-data.org — rerunning after a short wait usually resolves them. Failures with HTTP 403 mean the token is missing. Failures with HTTP 429 mean the rate limit was exceeded (run too quickly in succession).

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

## 🚀 CI/CD & Allure Report on GitHub Pages

Every push to `main` automatically:
1. Runs `VideoGameTests`, `GpathJSONTest`, `GpathXMLTests`, `MyFirstVideoGame` via GitHub Actions
2. Generates the Allure HTML report
3. Publishes it to **GitHub Pages**

The **Allure Report** badge at the top of this file links directly to the live report.

### First-time GitHub Pages setup

Go to **Settings → Pages** in this repository and set:

| Setting | Value |
|---------|-------|
| Source  | **GitHub Actions** |

Save — no branch selection needed. The workflow handles deployment automatically on every push.

### Re-run manually

Go to **Actions → Allure Report → Run workflow** to trigger a fresh run without pushing code.

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

### Allure report

```powershell
# Run tests (results saved to target/allure-results/)
mvn -Dtest=VideoGameTests test

# Generate static HTML report → target/site/allure-maven-plugin/index.html
mvn allure:report

# Open live report in browser (auto-starts a local server)
mvn allure:serve
```

---

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

### Test suite summary

| Test class | Tests | Stable | Known failure causes |
|---|---|---|---|
| `VideoGameTests` | 14 | ✅ All pass | None – read-only sandbox API, always available |
| `FootbalTests` | 12 | ⚠️ Usually pass | HTTP 403 (missing token), HTTP 429 (rate limit), HTTP 500 (server error) |
| `GpathJSONTest` | 5 | ✅ All pass | None – read-only sandbox API, always available |
| `GpathXMLTests` | 5 | ✅ All pass | None – read-only sandbox API, always available |
| **Total** | **36** | | |

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

<!-- Allure JUnit 4 adapter -->
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-junit4</artifactId>
    <version>2.27.0</version>
    <scope>test</scope>
</dependency>

<!-- Allure REST Assured filter -->
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-rest-assured</artifactId>
    <version>2.27.0</version>
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

---

## ✅ To Do

| # | Improvement | Status |
|---|-------------|--------|
| 1 | Runtime API token injection for Football API | ✅ Done |
| 2 | Rate limit throttling for Football API (free tier 10 req/min) | ✅ Done |
| 3 | Position-independent assertion in `getFirstTeamName` | ✅ Done |
| 4 | Allure reporting integration (JUnit 4 + REST Assured filter) | ✅ Done |
| 5 | Automated Allure report published to GitHub Pages via CI | ✅ Done |

---

## 🛠️ Improvements

### 1. Runtime API token injection (`FootballConfig`)

**Problem:** The `X-Auth-Token` header was hardcoded and commented out in `FootballConfig`, meaning the token was never sent to the API. All Football tests returned HTTP 403 Forbidden regardless of what was passed on the command line.

**Fix:** `FootballConfig.setup()` now reads the token at runtime from two sources (in priority order):
1. JVM system property: `-Dfootball.api.token=<token>`
2. Environment variable: `FOOTBALL_DATA_API_TOKEN`

This keeps the token out of source control and allows flexible CI/local usage.

---

### 2. Rate limit throttling (`FootballConfig`)

**Problem:** The football-data.org free plan allows **10 requests per minute**. With 12 test methods running back-to-back, the last 2 tests consistently received HTTP 429 Too Many Requests.

**Fix:** Added a `@Before` method (`rateLimitDelay`) in `FootballConfig` that sleeps 6 seconds before each test. At 10 requests/minute the safe inter-request interval is 6 s — this keeps the suite within the free-tier limit without requiring any changes to individual test classes.

---

### 3. Position-independent assertion in `getFirstTeamName` (`FootbalTests`)

**Problem:** The test asserted `teams.name[1] == "Arsenal FC"`, relying on Arsenal being at a specific index in the API response. The football-data.org API does not guarantee a stable team order, so the assertion broke when the order changed.

**Fix:** Replaced the index-based check with `hasItem("Arsenal FC")`, which verifies that Arsenal FC is present anywhere in the list. The test now passes regardless of the ordering returned by the API.

---

### 4. Allure reporting integration

**Problem:** Test results were only visible in Maven console output — no structured report, no request/response details, no historical trend.

**Fix:** Added `allure-junit4` and `allure-rest-assured` dependencies (v2.27.0). The `AllureRestAssured` filter is registered in both `VideoGameConfig` and `FootballConfig`, which automatically attaches the full HTTP request and response to every test in the report. All test methods are annotated with `@Feature`, `@Story`, and `@Description` to produce a structured, readable report.

Reports can be generated locally:
```powershell
mvn -Dtest=VideoGameTests test   # run tests
mvn allure:report                # generate HTML → target/site/allure-maven-plugin/
mvn allure:serve                 # open in browser
```

---

### 5. Automated Allure report published to GitHub Pages

**Problem:** The Allure report existed only on the developer's machine and was not accessible to others without running the tests locally.

**Fix:** Added a GitHub Actions workflow (`.github/workflows/allure-report.yml`) that triggers on every push to `main`. It runs all non-Football tests, generates the Allure report, and deploys it to GitHub Pages using the official `actions/deploy-pages` action.

The live report is always available at:  
👉 **https://magdau.github.io/restAssured-tutorial/**

The workflow uses the modern `actions/upload-pages-artifact` + `actions/deploy-pages` approach (source: **GitHub Actions** in Pages settings), which is more reliable than the legacy branch-based deployment.
