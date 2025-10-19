# tamtour-voting

This repository contains the code for the public voting system used at the "TamTour Trophy Basel 2025" event.
Details about the event can be found at [tamtour.ch](https://tamtour.ch).

> [!IMPORTANT]  
> This project is no longer in active development because the TamTour Trophy ended.
> See [retrospective](#retrospective) for my thoughts about this project.

## Repository Structure

- `.github/`: Contains GitHub Actions workflows for CI.
- `env/`: Contains environment configuration file templates for Docker Compose and/or IDE setup.
- `voting-frontend/`: Contains an **Angular frontend application** for user interaction.
    - Also includes the static **result viewer** that can be displayed on a big screen to show voting results.
- `voting-backend/`: Contains a **Spring Boot backend application** that handles authentication, voting logic and data
  storage.
- `docker-compose-local-db.yml`: Docker Compose file to run a local database for development purposes.
- `docker-compose-local-full.yml`: Docker Compose file to run the full application stack locally.
- `docker-compose-preview.yml`: Docker Compose file for deploying a preview instance, e.g. via Coolify.

## Features

- Login via Google OAuth2
- Rules acceptance
- Multiple voting categories
- Simple voting interface with drag-and-drop functionality to rank candidates
- Time window for voting and submissions
- Admin interface:
    - List and block/unblock users
    - Create, edit and delete categories and candidates
    - View submitted votes
    - Disqualify and unsubmit/submit valid votes
    - Control the result viewer
- Result-viewer for displaying results on a big screen, controlled from the admin interface via WebSockets.
- Dockerized for easy deployment

Users can create their voting order during the voting period and submission period. During the submission period, users
can submit their votes. After the submission period ends or after a vote is submitted, no further changes are possible.
Results are not made public until the voting ends. The viewer does not support real-time updates during the voting period.

Timeline: Voting period opens → Submission period opens → Submission & voting period ends

## Architecture notes

Locally, the backend requests are proxied though the Angular development server, which is configured in
`voting-frontend/proxy.conf.json`.
In production, the backend requests are proxied through Nginx, which also serves the frontend and which is configured in
`voting-frontend/nginx.conf`.

## Retrospective

The goal of this project was to create a public voting system for a specific event, the TamTour Trophy Basel 2025. I
wanted it to be as simple as possible with all necessary features, but without any unnecessary complexity. Overall, I am
satisfied with the outcome. The system worked well during the event, and users were able to vote without
any issues.

### What went well

- Easy hosting and deployment using Docker and Docker Compose.
- The Angular frontend provided a smooth user experience.
- The Spring Boot backend was robust and handled the voting logic effectively.
- The use of Google OAuth2 for authentication simplified user management.
- Low resource consumption.

### What could be improved

- Currently, the viewer requires an internet connection to stay connected with the WebSocket. A local fallback mechanism
  would be beneficial in case of network issues.
- The login via Google was implemented a bit hacky due to the setup with an Angular frontend and Spring Boot backend. A
  more elegant solution would be preferable, maybe using tools like Keycloak, Better-Auth, Auth0 or Clerk.
