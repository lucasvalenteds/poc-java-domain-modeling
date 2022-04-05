# POC: Java Domain Modeling

Demonstrating one approach to implement a web service using business concepts.

We want to be able to implement the application domain using the same jargon the business team uses, so we can communicate using the same vocabulary and avoid misunderstanding requirements. We also want to consider the code the source as the reference for specification as much as possible instead of using external documentation such as Wiki pages, User Stories or PDF files.

The application is a Web service capable of managing students and courses. It should provide endpoints that communicate using HTTP protocol for basic operations, to allow a student to enroll in a course and rate it to share their experience with the institution. It is implemented following JAX-RS standard implemented by Jersey Framework, inversion of control managed by HK2, validations implemented using Jakarta Validations (JSR 380). All the data is persisted in H2 relational database provisioned by Flyway and Spring Data JDBC as the query library. The source code is tested using JUnit Jupiter, AssertJ and Jersey Test Framework. High level tests must target both Grizzly and in-memory containers.

## How to run

| Description | Command |
| :--- | :--- |
| Run tests | `mvn test` |
| Run application | `mvn exec:java` |
