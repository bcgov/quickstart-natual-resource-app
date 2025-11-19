# Template for Natural Resources Application

Look for app-name everywhere and replace it with the name of your application repository.
This is a template repository for creating applications related to natural resources. It provides a basic structure and guidelines to help you get started quickly.

## Frontend

The frontend app is built using React. It provides a user-friendly interface for interacting with the natural resources data. Is already set to use FAM for authentication, and has basically everything needed to get started. More information can be found on the Wiki.

## Backend

The backend app, connects to postgres database and provides RESTful APIs for the frontend to interact with. It was built using Java and Spring Boot 3. When it needs access to legacy data, it calls the legacy application service.

## Legacy

The legacy application is built using Java and Spring Boot 2. It connects to the older Oracle database and provides APIs for accessing legacy data. This application is usually not exposed directly to the frontend, but is accessed through the backend application.

## LegacyDB

The legacy database is an Oracle database that contains historical data related to natural resources. It is accessed by the legacy application to retrieve and manipulate data as needed. This legacydb is only for local and development use, and should not be used in production. Ideally it should be deployed on the tools namespace.
