[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Puneethkumarck_crypto-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_crypto-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Puneethkumarck_crypto-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_crypto-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Puneethkumarck_crypto-api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_crypto-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Puneethkumarck_crypto-api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Puneethkumarck_crypto-api)

# Crypto API

This API provides operations for interacting with the Solana blockchain, allowing users to check account balances and transfer SOL tokens between wallets. Designed with simplicity and performance in mind, the API includes key features like balance validation and automatic SOL airdrops in case of insufficient funds.

## Key Features:
- **Get Balance:** Retrieve the SOL balance for any given Solana wallet address.
- **Transfer SOL:** Initiate a transfer between two Solana wallets with built-in validation for both sender and receiver accounts.
    - If either the sender or receiver has insufficient funds, the system automatically airdrops SOL before completing the transaction.

## Technologies Used:
- **Java 21**
- **Spring Boot 3.3.3**
- **Hexagonal Architecture:** Ensures a clean separation between core business logic and infrastructure concerns, enabling maintainability and flexibility.
- **WireMock:** Used for mocking external API calls during testing.
- **SpringDoc OpenAPI:** Automatically generates OpenAPI documentation for the API, ensuring clear and accurate API specifications.
- **GitHub Actions:** Used for automating tasks like builds, tests, and SonarCloud analysis.
- **SonarCloud:** Ensures code quality, performs static code analysis, and helps maintain high coding standards.
- **Codacy:** Provides additional code quality analysis and enforces coding standards.

This API is REST-based, and it follows the OpenAPI v3.0.1 specification, ensuring interoperability and ease of integration.

## Running the Application

To run the Crypto API locally, use the following Gradle command:

```bash
./gradlew :app:bootRun
```

# Testing the API

You can test the API functionality using curl commands as shown below.

## 1. Get Balance
To fetch the balance of a Solana wallet, use the following command:

```bash
curl --location 'http://localhost:8080/api/v1/balances/p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75'
```

## 2. Transfer SOL

To transfer SOL between two wallets, use the following command:

```bash
curl --location 'http://localhost:8080/api/v1/transfers' \
--header 'Content-Type: application/json' \
--data '{
"to" : "81e58SU8EHdBmSnETExsCwurbfWeCbNg9UoAFmKPyyj3",
"amount" : "1000"
}'
```
This will initiate a transfer of 1000 lamports to the specified wallet address.

