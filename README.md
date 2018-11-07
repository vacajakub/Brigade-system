# EAR e-shop

EAR e-shop is a demonstration application for the purpose of teaching the Enterprise Architectures (EAR) course.

It is a simplified e-shop, concentrating on the backend part of the application. The following technologies are used/required
for development and running:

* JDK 8
* Apache Tomcat 9 or any Servlet API 4-compatible application server
* PostgreSQL 9 (a different database can be configured)
* Apache Maven 3.x

The application is developed using:
* Spring 5
* Spring Security 5
* JPA 2.2
* Jackson 2.9
* Spring-based REST API
* JUnit 4, Mockito 2, Spring test, H2 database (for tests)
* SLF4J + Logback

The following technologies can simplify application development, but it was decided not to use them as they would obscure
the underlying principles important for the students. However, they can be useful for developing semester works.

* Spring Boot
* Spring JPA repositories

### Frontend

Application front end is written is **React**. It exists only to provide a showcase for the implemented backend, 
i.e., so that the students can see effects of the backend implementation. Since the students
will not be learning frontend technologies in EAR 2018, it is not necessary for them to understand how the UI works.

They should also not need to install Node or any JS-building technology, as the application will use a pre-build bundle.

The frontend will be written in plain JS (instead of TypeScript) to simplify understanding for those who will be interested in it.

### Data

The system generates an admin user on first start. Its credentials are `ear-admin@kbss.felk.cvut.cz/adm1n`. See `SystemInitializer`
for details.

`data.sql` contains some sample data which can be inserted into the database. It contain an addition user - *Sam Fisher* - who is a
guest, so he cannot modify categories or products. His credentials are **fisher@example.org/a**.