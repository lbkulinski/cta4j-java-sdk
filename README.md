# cta4j Java SDK

A lightweight Java SDK for interacting with the [Chicago Transit Authority (CTA)](https://www.transitchicago.com/) APIs — both Train Tracker and Bus Tracker.  
Built for simplicity, reliability, and zero external dependencies beyond the Java standard library.

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
    <version>1.2.0</version>
</dependency>
```

### Gradle (Kotlin DSL)
```kotlin
implementation("com.cta4j:cta4j-java-sdk:1.2.0")
```

---

## 🧩 Example Usage

### Fetch upcoming train arrivals for a station

```java
import com.cta4j.client.BusClient;
import com.cta4j.client.TrainClient;

void main() {
    TrainClient trainClient = new TrainClient("TRAIN_API_KEY");

    trainClient.getStationArrivals("41320")
               .stream()
               .map(arrival -> String.format(
                   "%s-bound %s Line train is arriving at %s in %d minutes",
                   arrival.destinationName(),
                   arrival.route(),
                   arrival.stationName(),
                   arrival.etaMinutes()
               ))
               .forEach(System.out::println);

    // Example output:
    // Howard-bound RED Line train is arriving at Belmont in 1 minutes
    // 95th/Dan Ryan-bound RED Line train is arriving at Belmont in 2 minutes
    // Kimball-bound BROWN Line train is arriving at Belmont in 4 minutes
    // Loop-bound BROWN Line train is arriving at Belmont in 5 minutes

    BusClient busClient = new BusClient("BUS_API_KEY");

    busClient.getStopArrivals("22", "1828")
             .stream()
             .map(arrival -> String.format(
                 "%s-bound bus is arriving at %s in %d minutes",
                 arrival.destination(),
                 arrival.stopName(),
                 arrival.etaMinutes()
             ))
             .forEach(System.out::println);

    // Example output:
    // Harrison-bound bus is arriving at Clark & Belmont in 1 minutes
    // Harrison-bound bus is arriving at Clark & Belmont in 26 minutes
}

```

---

## 🧠 Design Goals

- **Dependency-light**: no Spring, Feign, or Lombok
- **Modern Java**: uses records and Apache HttpClient 5
- **Safe by default**: retries transient failures, handles rate limits
- **Framework-agnostic**: works in any Java 21+ project

---

## 🧾 License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).  
Copyright © 2025 Logan Bailey Kulinski.

---

## 🗺️ Links

- [Train Tracker API key request](https://www.transitchicago.com/developers/traintrackerapply/)
- [Bus Tracker API key request](https://www.transitchicago.com/developers/bustracker/)
- [CTA Developer Resources](https://www.transitchicago.com/developers/)
- [Project Website](https://cta4j.app)

---

*Built with ❤️ by [Logan Bailey Kulinski](https://lbku.net)*
