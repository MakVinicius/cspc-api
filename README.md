
# Compass Classrooms - API
### Proposta Ponto Extra
Adicionar o Flyway e utilizar migrations para manipulação do Banco de Dados e fazer algumas alterações.
Implementar a Classe Record para simplificação dos DTOs do projeto. 

### Proposta de solução Mak Vinícius
Alterei quase todos os DTOs do projeto para implementação da classe Record do Java. As implementações podem ser visualizadas dentro do diretório >aa>aaa>
Para o Flyway eu adicionei algumas alterações nas entidades do projeto. 
- Para a classe User eu adicionei o campo linkedInLink do tipo String para representar o link do perfil do LinkedIn da pessoa;
- Para a classe Classroom eu adicionei o campo progress do tipo BigDecimal para representar a porcentaegm de conclusão da trilha que a classroom representa;
- Para a classe Grade eu alterei o tipo das variáveis de Double para BigDecimal. Com essa mudança é possível colocar limites para os valores com as anotações @DecimalMin e @DecimalMax de forma que o usuário deve sempre informar notas entre 0.00 e 10.00;

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

whil create a user for first access

---

```http
  POST /auth/signin
```

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `username`      | `string` | **Mandatory**. user`s email/username |
| `password`      | `string` | **Mandatory**. user`s password |

whil return a jwt token, to authenticate any other protected resources

---



## Rodando localmente

Clone the project

```bash
  git clone https://github.com/ThiagoHenriqueFP/cspc-api
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
