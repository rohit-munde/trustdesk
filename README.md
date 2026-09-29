# TrustDesk

TrustDesk is a Spring Boot backend for an AI-assisted customer support workflow.

<img width="1672" height="941" alt="image" src="https://github.com/user-attachments/assets/a1f8e4a1-1b25-4c6c-8260-e210790d65f7" />

The core workflow is:

```text
Ticket
-> customer/order context
-> markdown knowledge-base retrieval
-> AI triage
-> cited draft reply
-> recommended action
-> human approval
-> idempotent action execution
-> AI operation trace
-> evaluation runner
```

## Features

- Seeded customers, orders, order items, and tickets.
- Flyway-managed MySQL schema and seed data.
- Ticket list, detail, status update, and priority update APIs.
- Markdown knowledge-base loading and relevant policy retrieval.
- AI provider abstraction with mock and OpenAI-backed providers.
- AI triage with category, priority, sentiment, escalation, citations, draft reply, and recommended action.
- Human approval gate for recommended actions.
- Idempotent action execution with execution reference and timestamp.
- AI operation trace persistence and trace retrieval API.
- Triage evaluation runner for seeded tickets.
- Angular frontend for ticket triage, draft reply, citations, recommended action, approval, and execution.

## Requirements

- Java 21
- Maven wrapper included
- MySQL running locally
- Node/npm for the Angular frontend

## Environment

Create a local `.env` file in the project root. Do not commit it.

```env
DB_URL=jdbc:mysql://localhost:3306/trustdesk
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
APP_SECURITY_USER_NAME=your_basic_auth_username
APP_SECURITY_USER_PASSWORD=your_basic_auth_password
TRUSTDESK_AI_PROVIDER=mock
OPENAI_API_KEY=your_openai_api_key_here
OPENAI_MODEL=gpt-4o-mini
```

Use mock AI:

```env
TRUSTDESK_AI_PROVIDER=mock
```

Use OpenAI:

```env
TRUSTDESK_AI_PROVIDER=openai
OPENAI_API_KEY=your_openai_api_key_here
OPENAI_MODEL=gpt-4o-mini
```

## Run Backend

From the project root:

```bash
set -a
source .env
set +a

./mvnw spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

## Run Frontend

In a second terminal:

```bash
cd frontend/trustdesk
npm start
```

The frontend runs on:

```text
http://localhost:4200
```

The Angular dev server proxies `/tickets` and `/evaluations` to the backend.

## API Contracts

Ticket APIs:

```http
GET /tickets
GET /tickets/{ticketId}
PATCH /tickets/{ticketId}/status
PATCH /tickets/{ticketId}/priority
POST /tickets/{ticketId}/triage
```

Action APIs:

```http
POST /tickets/{ticketId}/actions/approve
POST /tickets/{ticketId}/actions/reject
POST /tickets/{ticketId}/actions/execute
```

Trace and evaluation APIs:

```http
GET /tickets/{ticketId}/traces
POST /evaluations/triage
```

Example triage response:

```json
{
  "success": true,
  "message": "Ticket triaged successfully",
  "payload": {
    "category": "REFUND",
    "priority": "MEDIUM",
    "sentiment": "FRUSTRATED",
    "escalationRequired": false,
    "citations": ["refund_policy.md"],
    "draftReply": "Hi, I'm sorry to hear that your left earbud arrived cracked...",
    "recommendedAction": "CREATE_REPLACEMENT_ORDER"
  }
}
```

Example execution response:

```json
{
  "success": true,
  "message": "Ticket updated successfully",
  "payload": {
    "ticketId": "tkt_9001",
    "actionApprovalStatus": "APPROVED",
    "actionExecutionStatus": "EXECUTED",
    "actionExecutionReference": "replacement_order:tkt_9001"
  }
}
```

## Demo Flow

1. Start MySQL.
2. Start the backend with `.env` loaded.
3. Start the frontend.
4. Open `http://localhost:4200`.
5. Select `Received damaged earbuds`.
6. Click `Run triage`.
7. Confirm category, priority, sentiment, citations, draft reply, and recommended action.
8. Click `Approve`.
9. Click `Execute action`.
10. Confirm `EXECUTED` status and execution reference.
11. Call `GET /tickets/tkt_9001/traces` to show the AI trace.
12. Call `POST /evaluations/triage` to run seeded triage evaluation.

## Verification

Backend:

```bash
./mvnw clean test
./mvnw clean package
```

Frontend:

```bash
cd frontend/trustdesk
npm test -- --watch=false
npm run build
```

## Notes

- Hibernate uses `ddl-auto=validate`; Flyway owns schema creation.
- `.env`, local logs, and local config should stay untracked.
- The mock provider is deterministic and useful for local tests.
- The OpenAI provider is selected only when `TRUSTDESK_AI_PROVIDER=openai`.
