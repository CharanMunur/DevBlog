# Devblog

A full-stack blog application featuring a Spring Boot backend and a React/Vite frontend.

## Project Structure

This repository is organized as a monorepo containing both the frontend and backend applications:

- [`/server`](./server/) - The backend REST API built with Java, Spring Boot, and Spring Security.
- [`/client`](./client/) - The frontend application built with React, Vite, Tailwind CSS v4, and shadcn/ui.

## Prerequisites

Before you begin, ensure you have the following installed:
- [Java 17](https://adoptium.net/) or higher
- [Node.js](https://nodejs.org/) (v20+ recommended)
- [Bun](https://bun.sh/) (or npm/yarn/pnpm)

## Getting Started

### 1. Start the Backend API
Navigate to the `server` directory and run the Spring Boot application using Gradle:

```bash
cd server
./gradlew bootRun
```
The API will start on `http://localhost:8080`.

### 2. Start the Frontend Client
Open a new terminal, navigate to the `client` directory, install dependencies, and start the development server:

```bash
cd client
bun install
bun run dev
```
The frontend will be available at `http://localhost:5173`.