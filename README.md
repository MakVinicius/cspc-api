
# Compass Classrooms - API
### Extra Point Activity
1. Implement the Record Class within the project.
2. Add Flyway and utilize migrations for Database manipulation and make some changes.

### Mak Vinicius's Solution
Most of the project's DTOs have been modified to implement the Java Record class. The implementations can be viewed within the directory ".../application/api". Inside each of the directories, there are folders called DTOs. <br></br>
Several changes have been made to the project's database for Flyway.

- For the "users" table, a "linkedin_link" column of type String was added to represent a person's LinkedIn profile link.<br></br>

- For the "classrooms" table, a "progress" column of type BigDecimal was added to represent the percentage of completion for the respective classroom's scholarship program.<br></br>

- Inside the "grades" table, the columns types were changed from Double to BigDecimal. With this change, it's possible to set limits for the values using annotations such as @DecimalMin and @DecimalMax, ensuring that users always provide grades between 0.00 and 10.00.<br></br>

<p>Additionally, the Lombok dependency was added to reduce the amount of code within the project. Annotations like @Getter, @Setter, and @NoArgsConstructor were used.<br>
Some data validations were added within the DTOs, such as @NotNull, @DecimalMin, @DecimalMax etc.<br>
In the resources directory, there is a file named " Endpoints_Scholarship.postman_collection.json " that contains the Postman endpoints for the evaluator to test the application.</p>

### Segundo desafio

#### PT BR
O sistema consiste na criacão de um gerenciador de turmas, abstraindo o funcionamento e organizacão do programa de bolsas da Compass.uol™. O sistema permite a criacão e gerenciamento de turmas, instrutores, scrum masters e estudantes. Há a possibiliade de agrupar os estudantes em squads e atribuir notas para os mesmo.

#### EN US
The system consists of the creation of a classroom manager, abstracting the operation and organization of the Compass.uol™ scholarship program. The system allows the creation and management of classes, instructors, scrum masters and students. There is the possibility to group students into squads and assign grades to them.



## Documentation

#### Swagger

```http
  GET /swagger-ui/index.html
```

#### Free resources

```http
  POST /auth/register
```

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `firstName`      | `string` | **Mandatory**. user`s first name |
| `lastName`      | `string` | **Mandatory**. user`s last name |
| `email`      | `string` | **Mandatory**. user`s email/username |
| `password`      | `string` | **Mandatory**. user`s password |

will create a user for first access

---

```http
  POST /auth/signin
```

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `username`      | `string` | **Mandatory**. user`s email/username |
| `password`      | `string` | **Mandatory**. user`s password |

will return a jwt token, to authenticate any other protected resources

---



## Rodando localmente

Clone the project

```bash
  git clone https://github.com/MakVinicius/cspc-api.git
```

Move to project directory

```bash
  cd cspc-api
```

**we recomend to open an IDE to run maven**

Launch the application inside an IDE (Intellij, Eclipse etc)



## Autores

- [@DaviVerissimo](https://www.github.com/DaviVerissimo)
- [@Gablier-R](https://www.github.com/Gablier-R)
- [@MakVinicius](https://www.github.com/MakVinicius)
- [@ThiagoHenriqueFP](https://www.github.com/ThiagoHenriqueFP)
