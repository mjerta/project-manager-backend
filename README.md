# Project Management

A single-user project management dashboard built with Java Spring Boot backend.

## Functional requirements
- The system must allow the admin to log in securely via the API gateway (OAuth/JWT are terminated there).
- The admin must be able to create new projects with a title, description, and publish flag.
- The admin must be able to update project details.
- The admin must be able to delete projects.
- The admin must be able to set projects as published or unpublished.
- The system must display a list of all projects.
- The system must fetch and display the last commit message from GitHub/Gitlab/ bare branch. (future)
- The system must allow filtering of tasks by status (e.g., To Do, In Progress, Blocked, Completed).
- The system must allow uploading and displaying an image for each project.
- This Micro-service should be agnostic for whatever it is being called by.

## Technical requirements
- Technical is not too much of an concern. Since this is a learn-by-doing project. I will however keep some standards that I learn along the way.
- So basically I'm using Java and SpringBoot
- Here in there I'm using Docker and some actions for some CI/CD pipelines.
- Later on also will be automatically adding tags. So I can really see the progress and its easy to revert.
- Eventually I want to deploy this in my home-lab of which I'm currently learning Kubernetes.

## Technical overview

### Project Management
- Create and edit projects (title, description, publish flag)
- Publish/unpublish functionality
- Manage task backlogs with per-task status tracking
- GitHub integration for latest commit messages
- Project image display

### User & Access Control
- Authentication, token minting, and role enforcement now live inside the `mpdev-api-gateway` service.
- This microservice trusts the gateway’s JWTs and focuses purely on domain logic.

### Backend
- Java Spring Boot
- Spring Data JPA / Hibernate
- GitHub API Integration
- Delivered as a focused microservice behind the gateway

## Future Enhancements
- Database schema implementation
- Project tagging system
- Search and filtering capabilities


## Learning objectives
- Catch up with Java skills
- Testing out more CI/CD practices
- Designing microservices that sit behind an API gateway
- Learning to develop in Neovim in combination with Java/Spring

```mermaid
classDiagram
    class Project {
        +Long id
        +String title
        +String description
        +Boolean published
    }

    class Task {
        +Long id
        +String name
        +String description
        +Long projectId
        +Long statusId
    }

    class Status {
        +Long id
        +String name
        +String description
    }

    class Image {
        +Long id
        +Blob imageData
        +Long projectId
    }

    Task "0..*" --> "1" Status
    Project "1" --> "0..*" Task : contains
    Project "1" --> "0..*" Image
```
