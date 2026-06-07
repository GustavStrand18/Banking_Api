# Banking API

A RESTful banking API built with Java 21 and Spring Boot 3, featuring JWT authentication, account management, and transaction processing.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security + JWT
- PostgreSQL
- Maven
- Docker (coming soon)

## Features

- User registration and login with JWT authentication
- Create and manage bank accounts (Checking & Savings)
- Deposits, withdrawals and transfers between accounts
- Transaction history with pagination
- Fraud detection for large transactions
- Global exception handling with clean error responses

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL
- Maven

### Setup

1. Clone the repository
2. Copy `src/main/resources/application.properties.template` to `src/main/resources/application.properties`
3. Fill in your PostgreSQL credentials
4. Run the application

```bash
./mvnw spring-boot:run
```

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and receive JWT token |

### Accounts
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/accounts | Create a new account |
| GET | /api/accounts | Get all accounts for user |
| GET | /api/accounts/{id} | Get a specific account |

### Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/transactions | Process a transaction |
| GET | /api/transactions/{accountId}/history | Get transaction history |

## Transaction Types

- `DEPOSIT` — Add funds to an account
- `WITHDRAWAL` — Remove funds from an account
- `TRANSFER` — Move funds between accounts

## Security

All endpoints except `/api/auth/**` require a valid JWT token in the Authorization header:

```
Authorization: Bearer <token>
```