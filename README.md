# Banking API

A RESTful banking API built with Java 21 and Spring Boot, featuring JWT authentication, account management and transaction processing.

## Tech Stack

- Java 21
- Spring Boot
- Spring Security + JWT
- PostgreSQL
- Maven
- Docker
- GitHub Actions (CI)
- JUnit 5 and Mockito

## Features

- User registration and login with JWT authentication
- Create and manage bank accounts (Checking and Savings)
- Deposits, withdrawals and transfers between accounts
- Transaction history per account
- Fraud detection: transactions above 10,000 are flagged
- Input validation (for example, amounts must be positive)
- Global exception handling with clean error responses

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL
- Maven (or use the included Maven wrapper)

### Run locally

1. Clone the repository
2. Copy `src/main/resources/application.properties.template` to `src/main/resources/application.properties`
3. Fill in your PostgreSQL credentials
4. Start the application:

```
./mvnw spring-boot:run
```

### Run with Docker

The repository includes a `Dockerfile` and a `docker-compose.yml`:

```
docker compose up --build
```

## API Endpoints

### Auth

| Method | Endpoint           | Description                 |
| ------ | ------------------ | --------------------------- |
| POST   | /api/auth/register | Register a new user         |
| POST   | /api/auth/login    | Login and receive JWT token |

### Accounts

| Method | Endpoint           | Description               |
| ------ | ------------------ | ------------------------- |
| POST   | /api/accounts      | Create a new account      |
| GET    | /api/accounts      | Get all accounts for user |
| GET    | /api/accounts/{id} | Get a specific account    |

### Transactions

| Method | Endpoint                              | Description             |
| ------ | ------------------------------------- | ----------------------- |
| POST   | /api/transactions                     | Process a transaction   |
| GET    | /api/transactions/{accountId}/history | Get transaction history |

## Transaction Types

- `DEPOSIT`: add funds to an account
- `WITHDRAWAL`: remove funds from an account (rejected if the balance is too low)
- `TRANSFER`: move funds between two accounts (requires `toAccountId`)

Transactions are processed inside a database transaction, so a transfer either updates both accounts or neither. Users can only access their own accounts.

## Security

All endpoints except `/api/auth/**` require a valid JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Tests

Run the test suite with:

```
./mvnw test
```

- Unit tests (JUnit 5 and Mockito) for `TransactionService`: deposits, withdrawals, insufficient funds, fraud flagging and unauthorized account access
- Integration tests (Spring Boot Test and MockMvc) for registration and login

The integration tests use the `test` profile and need a PostgreSQL database.

## CI

A GitHub Actions workflow runs on every push and pull request to `main`. It starts a PostgreSQL container, sets up JDK 21, runs the tests and builds the project.

## Possible Improvements

- Pagination for transaction history
- Protection against concurrent updates to the same account (optimistic or pessimistic locking)
- Unit tests for transfers
