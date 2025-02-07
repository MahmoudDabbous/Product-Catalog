# Product Catalog Project

A RESTful API for managing a product catalog. This project leverages Java Servlets and JDBC for database interaction and is designed to run on Apache Tomcat. It employs Maven for build and dependency management.

## Table of Contents

- [Overview](#overview)
- [Environment Requirements](#environment-requirements)
- [Dependencies](#dependencies)
- [Project Setup](#project-setup)
- [Building and Deploying](#building-and-deploying)
- [Usage](#usage)
- [Future Enhancements](#future-enhancements)

## Overview

The Product Catalog Project provides a RESTful API for creating, reading, updating, and deleting product and its categories entries. It is designed to be lightweight and efficient, making use of the Jakarta Servlet API and JDBC for backend operations and Gson for JSON processing.

## Environment Requirements

To run the application, ensure you have the following installed:

- **Tomcat 10 or higher:** For deploying and running the web application.
- **MySQL 8.1:** As the primary relational database.
- **Maven:** For managing project dependencies and building the application.

## Dependencies

The project uses the following libraries:

- **Jakarta Servlet API:** For building RESTful endpoints.
- **Gson:** For converting Java objects to JSON and vice versa.
- **MySQL Java Connector:** For establishing JDBC connections with the MySQL database.

## Project Setup

1. **Database Initialization:**
   - Execute the provided `init.sql` script on your MySQL server. This script will create the necessary database and tables.

2. **Building the Project:**
   - Navigate to the project directory and execute:

     ```sh
     mvn clean install
     ```

## Building and Deploying

1. **Build:**
   - The above Maven command will compile and package the project into a WAR file.

2. **Deploy:**
   - Copy the generated WAR file (typically located in the `target` directory) to the Tomcat `webapps` folder.
   - Start or restart Tomcat. The application will be deployed automatically.

3. **Configuration:**
   - Ensure that the database connection parameters (URL, username, password) are correctly set in the project's configuration files `dbconfig.properties`.

## Usage

- **For testing APIs endpoints**
  - Please take a look at the postman collection I've attached.

## Future Enhancements

- **Security:** Implementation of authentication and authorization mechanisms.
- **Scalability:** Exploration of connection pooling and caching mechanisms to improve performance under load.
- **Testing:** Integration and unit tests to ensure reliability and maintainability.
