# tamtour-voting

This repository contains the code for the public voting system used at the "TamTour Trophy Basel 2025" event.
Details about the event can be found at [tamtour.ch](https://tamtour.ch).

## Repository Structure

- `.github/`: Contains GitHub Actions workflows for CI.
- `env/`: Contains environment configuration file templates for Docker Compose and/or IDE setup.
- `voting-frontend/`: Contains an **Angular frontend application** for user interaction.
- `voting-backend/`: Contains a **Spring Boot backend application** that handles authentication, voting logic and data storage.
- `docker-compose-local-db.yml`: Docker Compose file to run a local database for development purposes.
- `docker-compose-local-full.yml`: Docker Compose file to run the full application stack locally.
- `docker-compose-preview.yml`: Docker Compose file for deploying a preview instance, e.g. via Coolify.

## Features

- Login via Google OAuth2
- Rules acceptance
- Multiple voting categories
- Simple voting interface with drag-and-drop functionality to rank candidates
- Time window for voting and submissions
- Admin interface for managing users, categories, candidates and votes
- Result-viewer for displaying results on a big screen, controlled from the admin interface via WebSockets.
- Dockerized for easy deployment

## Architecture notes

Locally, the backend requests are proxied though the Angular development server, which is configured in `voting-frontend/proxy.conf.json`.
In production, the backend requests are proxied through Nginx, which also serves the frontend and which is configured in `voting-frontend/nginx.conf`.
