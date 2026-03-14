# cta4j Java SDK

A lightweight Java SDK for interacting with the [Chicago Transit Authority (CTA)](https://www.transitchicago.com/) APIs — both Train Tracker and Bus Tracker.  
Built for simplicity, reliability, and minimal external dependencies.

---

## 🚆 Overview

`cta4j-java-sdk` provides a clean, type-safe interface for accessing CTA's public transit data.  
It wraps the official Train and Bus Tracker APIs with intuitive Java models and error handling.

**Features:**
- Simple, dependency-light HTTP client (uses Apache HttpClient 5)
- DTOs modeled as Java records
- Works with both **Train Tracker** and **Bus Tracker** APIs

---

## 🔑 Getting API Keys

You'll need a free API key from CTA to use the SDK.

- **Train Tracker API** → [Apply here](https://www.transitchicago.com/developers/traintrackerapply/)
- **Bus Tracker API** → [Apply here](https://www.transitchicago.com/developers/bustracker/)

After applying, you'll receive an API key by email. Keep it safe — you'll use it when initializing the client.

---

## ⚙️ Installation

### Maven
```xml
<dependency>
    <groupId>com.cta4j</groupId>
    <artifactId>cta4j-java-sdk</artifactId>
    <version>4.0.0</version>
</dependency>
```

### Gradle (Kotlin DSL)
```kotlin
implementation("com.cta4j:cta4j-java-sdk:4.0.0")
```

---

## 🧩 Example Usage

### Fetch upcoming train arrivals for a station

```java
import com.cta4j.train.TrainApi;

public final class Application {
    static void main(String[] args) {
        TrainApi trainApi = TrainApi.builder("TRAIN_API_KEY")
                                    .build();

        trainApi.arrivals()
                .findByMapId("41320")
                .forEach(arrival -> System.out.printf(
                    "%s-bound %s Line train is arriving at %s in %d minutes%n",
                    arrival.destinationName(),
                    arrival.line(),
                    arrival.stationName(),
                    Duration.between(arrival.predictionTime(), arrival.arrivalTime())
                            .toMinutes()
                ));

        // Example output:
        // Loop-bound BROWN Line train is arriving at Belmont in 2 minutes
        // Howard-bound RED Line train is arriving at Belmont in 2 minutes
        // Howard-bound RED Line train is arriving at Belmont in 4 minutes
        // Kimball-bound BROWN Line train is arriving at Belmont in 9 minutes
        // 95th/Dan Ryan-bound RED Line train is arriving at Belmont in 10 minutes
        // ...
    }
}
```

### Fetch upcoming bus arrivals for a stop

```java
import com.cta4j.bus.BusApi;

public final class Application {
    static void main(String[] args) {
        BusApi busApi = BusApi.builder("BUS_API_KEY")
                              .build();

        busApi.predictions()
              .findByRouteIdAndStopId("22", "1828")
              .forEach(prediction -> System.out.printf(
                  "%s-bound bus is arriving at %s in %d minutes",
                  prediction.destination(),
                  prediction.stopName(),
                  Duration.between(Instant.now(), prediction.arrivalTime())
                          .toMinutes()
              ));

        // Example output:
        // Harrison-bound bus is arriving at Clark & Belmont in 1 minutes
        // Harrison-bound bus is arriving at Clark & Belmont in 26 minutes
    }
}
```

---

## 🧠 Design Goals

- **Dependency-light**: no Spring, Feign, or Lombok
- **Modern Java**: uses records and Apache HttpClient 5
- **Framework-agnostic**: works in any Java 21+ project

---

## 🛠️ Planned Improvements

- Add test coverage
- Add support for more API endpoints, like service alerts
- Implement caching for frequently requested data
- Add asynchronous request support

Have ideas? Feel free to open an issue or submit a PR!

---

## 🧾 License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).  
Copyright © 2026 Logan Bailey Kulinski.

---

## 🗺️ Links

- [Train Tracker API key request](https://www.transitchicago.com/developers/traintrackerapply/)
- [Bus Tracker API key request](https://www.transitchicago.com/developers/bustracker/)
- [CTA Developer Resources](https://www.transitchicago.com/developers/)
- [Project Website](https://cta4j.app)

---

*Built with ❤️ by [Logan Kulinski](https://lbku.net)*
