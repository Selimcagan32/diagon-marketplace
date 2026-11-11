# Diagon

[Türkçe](README.md)

A multi-vendor e-commerce marketplace built with Java 21 and Spring Boot. A customer collects products from different sellers in one cart and pays once; the system splits the order by seller.

This is a portfolio and learning project. No real payments are taken and no real products are sold.

## Status

The project is under development and currently in Phase 0 (design and skeleton). The live demo, screenshots and setup steps will be added as Phase 1 progresses.

## Goal

To apply topics that come up often in backend work in a single application, each with a written rationale:

- Splitting the domain into modules and keeping module boundaries
- Stock consistency under concurrent requests
- Double-payment protection
- Caching and short-lived data
- Event-driven communication
- Container-based deployment and scaling

## Phase 1 scope

- **Guest:** search, list and filter products, add to cart
- **Customer:** sign up and sign in, guest cart merged into the account cart, order, simulated payment, order tracking and cancellation
- **Seller:** seller application, product, variant and stock management, updating the status of incoming sub-orders
- **Admin:** approving seller applications, category management

## Architecture

![Architecture diagram](docs/architecture.png)

The diagram shows the target architecture. Components are added phase by phase, following the roadmap.

- The system is a modular monolith: a single Spring Boot application with separated business modules.
- Each module has its own PostgreSQL schema. Modules reach each other through public interfaces and by ID.
- Authentication lives in Keycloak. The backend validates tokens with Keycloak's public keys.
- The cart and the cache are in Redis.
- Order events are relayed to Kafka with the outbox pattern, and the notification module consumes them.

## Tech stack

| Layer | Technology | Phase |
| --- | --- | --- |
| Backend | Java 21, Spring Boot 4.1 | Phase 0–1 |
| Data | PostgreSQL, Flyway, Hibernate / JPA | Phase 0 |
| Identity | Keycloak (OIDC, PKCE) | Phase 0–1 |
| Cart and cache | Redis | Phase 1 |
| Frontend | React, Vite | Phase 1 |
| Deployment | Docker, Docker Compose, GitHub Actions | Phase 0–1 |
| Orchestration | Kubernetes (kind), Kustomize | Phase 2 |
| Messaging | Apache Kafka | Phase 3 |
| Monitoring and load testing | Prometheus, Grafana, k6 | Phase 4 |
| Image search | Python, FastAPI, pgvector | Phase 5 |
| Testing | JUnit 5, Mockito, Testcontainers, Spring Modulith | From Phase 0 |

## Roles and permissions

| Role | Who | How it is obtained |
| --- | --- | --- |
| Guest | Anyone using the site without signing in | Not being signed in is enough |
| Customer | A registered user | Given automatically on sign-up |
| Seller | A user who has a store and sells products | The customer applies, an admin approves |
| Admin | The person who runs the platform | Cannot be obtained by signing up, the account is created manually |

Role rules:

- A user can have more than one role. A seller is also a customer and can shop.
- A seller cannot add their own products to the cart or buy them.
- A user whose application is rejected remains a customer.
- An admin does not shop and cannot become a seller.

In the permission table ✓ means allowed, ✗ means not allowed, and "Own" means allowed only for the user's own records.

| Module | Action | Guest | Customer | Seller | Admin |
| --- | --- | --- | --- | --- | --- |
| Identity | Views and edits own profile | ✗ | Own | Own | Own |
| Identity | Manages addresses | ✗ | Own | Own | ✗ |
| Seller | Applies to become a seller | ✗ | ✓ | ✗ | ✗ |
| Seller | Views a store profile | ✓ | ✓ | ✓ | ✓ |
| Seller | Edits the store profile | ✗ | ✗ | Own | ✗ |
| Catalog | Searches, lists and views products | ✓ | ✓ | ✓ | ✓ |
| Catalog | Adds and edits products, variants and images | ✗ | ✗ | Own | ✗ |
| Catalog | Manages the category tree | ✗ | ✗ | ✗ | ✓ |
| Stock | Updates variant stock | ✗ | ✗ | Own | ✗ |
| Cart | Views and edits the cart | Own | Own | Own | ✗ |
| Order | Places an order | ✗ | ✓ | ✓ | ✗ |
| Order | Views and tracks orders | ✗ | Own | Own | ✗ |
| Order | Cancels an order | ✗ | Own | Own | ✗ |
| Order | Views incoming sub-orders | ✗ | ✗ | Own | ✗ |
| Order | Updates sub-order status | ✗ | ✗ | Own | ✗ |
| Payment | Pays for an order | ✗ | Own | Own | ✗ |
| Admin | Approves or rejects seller applications | ✗ | ✗ | ✗ | ✓ |

## Design decisions

Important decisions are kept as short architecture decision records (ADRs). Each record covers the context, the options considered, the decision and its consequences. The records are written in Turkish.

| No | Decision |
| --- | --- |
| [ADR-001](docs/adr/0001-moduler-monolit.md) | Modular monolith architecture |
| [ADR-002](docs/adr/0002-keycloak.md) | Keycloak for identity management |
| [ADR-003](docs/adr/0003-kategori-ozellikleri-jsonb.md) | JSONB for category-specific attributes |
| [ADR-004](docs/adr/0004-stokta-kotumser-kilitleme.md) | Pessimistic locking for stock |
| [ADR-005](docs/adr/0005-stok-rezervasyonu.md) | Stock reservation and transaction boundary |
| [ADR-006](docs/adr/0006-sepet-redis.md) | Keeping the cart in Redis |
| [ADR-007](docs/adr/0007-kafka-ve-outbox.md) | Kafka and the outbox pattern |

## Roadmap

- [ ] **Phase 0 – Design and skeleton:** data model, decision records, project skeleton, CI
- [ ] **Phase 1 – Core shopping flow:** identity, catalog, stock, cart, order, payment simulator, live demo
- [ ] **Phase 2 – Kubernetes and CD:** running on a local cluster, autoscaling, approved deployment
- [ ] **Phase 3 – Kafka and notifications:** order events, outbox pattern, e-mail notification
- [ ] **Phase 4 – Monitoring and load testing:** metrics, Grafana dashboard, flash-sale load test
- [ ] **Phase 5 – Image search:** finding products by visual similarity
