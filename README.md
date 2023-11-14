# Classroom Manager REST API

The challenge consisted in the creation of a classroom manager backend using the Spring framework and Java. It is a REST API that has multiple endpoints allowing the CRUD operations for different users and persisting data with the MySQL database.

### Functionalities
The application has authentication functionalities for different types of users (students, scrum masters, instructors and coordinators).<br>
It also allows the CRUD operations for the users, squads and classrooms. All the endpoints are documented with the swagger dependency.<br>
You can assign grades to students, can assign students to squads and can assign any type of user to a classroom.

## Documentation

#### Swagger

```http
  GET http://localhost:8080/swagger-ui/index.html
```

### Prerequisites

What softwares you need to install to run the application

```
Git
MySQL 8.0
Java Development Kit 17 LTS
IntelliJ IDE 2023 or equivalent
```

## Running Locally

Clone the project

```bash
  git clone https://github.com/MakVinicius/cspc-api.git
```

## First Access

First, create an admin user in the database through the endpoint below. This is necessary in order to interact with the backend.

```http
  POST http://localhost:8080/auth/register
```

| Parameter   | Type       | Descryption                                   |
| :---------- | :--------- | :------------------------------------------ |
| `firstName`      | `string` | **Mandatory**. user`s first name |
| `lastName`      | `string` | **Mandatory**. user`s last name |
| `email`      | `string` | **Mandatory**. user`s email/username |
| `password`      | `string` | **Mandatory**. user`s password |

---

The next step is to login in the app with the endpoint below. Input the email and password from the admin user created above. The app will return a temporary access token (JWT) that you can use to authenticate in the backend.

```http
  POST http://localhost:8080/auth/signin
```

| Parameter   | Type       | Descryption                                   |
| :---------- | :--------- | :------------------------------------------ |
| `username`      | `string` | **Mandatory**. user`s email/username |
| `password`      | `string` | **Mandatory**. user`s password |

---

After this you can access all the endpoints of the application to manage the classrooms. The endpoints are documented with swagger.

## Authors

- [@DaviVerissimo](https://www.github.com/DaviVerissimo)
- [@Gablier-R](https://www.github.com/Gablier-R)
- [@MakVinicius](https://www.github.com/MakVinicius)
- [@ThiagoHenriqueFP](https://www.github.com/ThiagoHenriqueFP)