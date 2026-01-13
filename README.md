# Energy Optimization API

A RESTful service built with Spring Boot that monitors UK National Grid carbon intensity and calculates optimal Electric Vehicle (EV) charging windows using a sliding window algorithm.

## Author: Michał Ryz

## Tech Stack

* **Java 25**
* **Spring Boot 4.0.1**


## Features

* **Energy Mix Aggregation:** Fetches generation data (Wind, Solar, Gas, etc.) and aggregates it into daily averages.
* **Smart Charging Algorithm:** Calculates the most eco-friendly time slot to charge a vehicle based on forecasted carbon intensity.
* **Error Handling:** Robust validation for user inputs and external API failures.
* **Swagger Ui** for Easy testing the endpoints
## API Endpoints

### 1. Get Daily Energy Mix
Returns the energy generation mix for the current day and forecast for upcoming days.

* **URL:** `/api/energy/mix`
* **Method:** `GET`
* **Response:** JSON array of daily fuel mix percentages.

### 2. Get Optimal Charging Window
Calculates the best time to start charging based on the requested duration.

* **URL:** `/api/energy/optimal-charging`
* **Method:** `GET`
* **Query Param:** `hours` (int, 1-6, default: 1)
* **Response:**
    ```json
    {
      "startTime": "2026-01-13T10:00:00Z",
      "endTime": "2026-01-13T12:00:00Z",
      "cleanEnergyPercentage": 85.5
    }
    ```

## Local Setup

1.  Clone the repository. 
    ```bash
    git clone https://github.com/MajkelRice/codibly-backend.git
    ```
2.  Build the project:
    ```bash
    ./gradlew clean build
    ```
3.  Run the application:
    ```bash
    ./gradlew bootRun
    ```
4.  Run tests:
    ```bash
    ./gradlew test
    ```
    
