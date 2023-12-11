# IWF Calculator API

## Overview
The Calculator API is a RESTful service that allows users to perform mathematical calculations. The service accepts mathematical expressions and returns the results. It also stores the expressions and results in an in-memory H2 database, allowing for caching and retrieval of previously calculated results.

## Demo
You can access a demo of this project here: https://site-azriopdjsa-uc.a.run.app/swagger-ui/index.html

#### Default users:

| username  | password      |
| --------  | --------      |
| spongebob | sponge123     |
| patrick   | patrick@star  |

## Usage
To use the API, send a POST request to ```/v1/calculations/calculate``` with a JSON payload containing the mathematical expression you want to calculate.

Example JSON payload:

```json
{
  "expression": "2+2-1"
}
```


The API will respond with the calculated result in the following format:

```json
{
  "expression": "2+2-1",
  "result": 3.00
}
```


### Security
The API uses token-based authentication (Bearer) for security. You need to include a valid token in the request header to access the API. Unauthorized requests will be denied.

### Database
The API uses an in-memory H2 database for storing expressions and results. Since this is a demo project, the tests also use the same database. Keep in mind that the in-memory database will be cleared each time the application restarts.

## Running the Application
To run the application locally, you can use the following commands:

```shell
mvn clean install
java -jar target/iwf-calculator.jar
```

The application will be accessible at http://localhost:8080.

## Deploy and CI/CD

The application can be deployed to AWS or GCP using the provided GitHub Actions workflows. The CI/CD pipeline includes building and testing the application and deploying it automatically on each commit to the main branch.

### GitHub Actions Workflows

#### Java CI/CD
This workflow builds and tests the application on each push to the main branch. Test results are published as artifacts. Then, it builds a Docker image, pushes it to a container registry, and deploys it to a cloud platform (AWS or GCP).

Make sure to set up the required secrets in your GitHub repository for the CI/CD workflows to work correctly.
