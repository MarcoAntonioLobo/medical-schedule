# Medical Schedule API

API REST para gerenciamento de **médicos**, **pacientes** e **consultas médicas**.

## 📌 Sobre

Essa API foi desenvolvida em **Java com Spring Boot** e segue padrões REST. Permite:

- Criar, listar, atualizar e deletar médicos, pacientes e consultas
- Retornar status HTTP semântico:
  - `201 Created` para POST
  - `204 No Content` para DELETE
  - `404 Not Found` para recursos inexistentes
  - `200 OK` para GET e PUT

## 📦 Como Rodar o Projeto

### 1. Rodando sem Docker

1. Clone o repositório:

```bash
git clone https://github.com/MarcoAntonioLobo/medical-schedule.git
cd medical-schedule
```

2. Configure o PostgreSQL (crie banco `medical_schedule`) e atualize `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_schedule
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

3. Compile e rode a aplicação:

```bash
mvn clean install
mvn spring-boot:run
```

### 2. Rodando com Docker

1. Clone o repositório:

```bash
git clone https://github.com/MarcoAntonioLobo/medical-schedule.git
cd medical-schedule
```

2. Build e up do Docker Compose:

```bash
docker compose up --build
```

> Isso irá criar os containers do **PostgreSQL** e da **aplicação Spring Boot**.  
> A aplicação ficará disponível em `http://localhost:8080`.

3. Para parar os containers:

```bash
docker compose down
```

## 🌐 Acessando a API

- Base URL: `http://localhost:8080`
- Documentação Swagger/OpenAPI:  
  `http://localhost:8080/swagger-ui/index.html`

## 🛠 Endpoints

### Doctors
```
| Método | Endpoint          | Descrição                    | Status HTTP                    |
|--------|-------------------|------------------------------|--------------------------------|
| GET    | /doctors          | Listar todos os médicos      | 200 OK                         |
| GET    | /doctors/{id}     | Obter médico por ID          | 200 OK / 404 Not Found         |
| POST   | /doctors          | Criar um novo médico         | 201 Created / 400 Bad Request  |
| DELETE | /doctors/{id}     | Deletar médico por ID        | 204 No Content / 404 Not Found |
```
### Patients
```
| Método | Endpoint          | Descrição                    | Status HTTP                    |
|--------|-------------------|------------------------------|--------------------------------|
| GET    | /patients         | Listar todos os pacientes    | 200 OK                         |
| GET    | /patients/{id}    | Obter paciente por ID        | 200 OK / 404 Not Found         |
| POST   | /patients         | Criar um novo paciente       | 201 Created / 400 Bad Request  |
| DELETE | /patients/{id}    | Deletar paciente por ID      | 204 No Content / 404 Not Found |
```
### Appointments
```
| Método | Endpoint            | Descrição                    | Status HTTP                    |
|--------|---------------------|------------------------------|--------------------------------|
| GET    | /appointments       | Listar todas as consultas    | 200 OK                         |
| GET    | /appointments/{id}  | Obter consulta por ID        | 200 OK / 404 Not Found         |
| POST   | /appointments       | Criar uma nova consulta      | 201 Created / 400 Bad Request  |
| PUT    | /appointments/{id}  | Atualizar consulta existente | 200 OK                         |
| DELETE | /appointments/{id}  | Deletar consulta por ID      | 204 No Content / 404 Not Found |
```
## 🧪 Testes

Todos os testes unitários e de integração passam com sucesso:

```bash
mvn test
```

## 📬 Contato

- GitHub: [https://github.com/MarcoAntonioLobo](https://github.com/MarcoAntonioLobo)  
- LinkedIn: [https://www.linkedin.com/in/marco-antonio-lobo-35568628b/](https://www.linkedin.com/in/marco-antonio-lobo-35568628b/)  
- Email: [Email: marcoantoniolobo82@gmail.com](mailto:marcoantoniolobo82@gmail.com)


