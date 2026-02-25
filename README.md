# Rest with Spring Boot

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-f5f5f5?style=for-the-badge&logo=junit5&logoColor=dc524a)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

[![Continuous Integration and Delivery with Github Actions](https://github.com/Gadiel-S/rest-with-spring-boot/actions/workflows/continuous-deployment.yml/badge.svg)](https://github.com/Gadiel-S/rest-with-spring-boot/actions/workflows/continuous-deployment.yml)

Esse Projeto é uma API construída ao longo do curso de Spring Boot do professor Leandro Costa na Udemy, construído usando 
**Java, Spring Boot, MySQL, Flyway, Swagger, JUnit5, Spring Security, Docker, Maven**, entre outros.

Essa API permite a manipulação de várias informações sobre seus usuários e livros, upload e download de arquivos, envio de emails, etc. 
Os endpoints estão protegidos com JWT e Spring Security, então é necessário logar pra acessar os endpoints, 
as informações de login estão detalhadas na seção **[Autenticação](#autenticacao)**

## Sumário
- [Instalação e Execução](#instalacao-e-execucao)
- [API Endpoints](#api-endpoints)
- [Autenticação](#autenticacao)
- [Collections](#collections)
- [Licença](#licenca)

## <a id="instalacao-e-execucao"></a>Instalação e Execução

### Opção 1 - Docker (Recomendado)

#### Pré-Requisitos
- Docker (https://www.docker.com)
- Git (https://git-scm.com)

#### Passos
1. Clone o repositório e acesse o diretório criado:
```git
git clone https://github.com/Gadiel-S/rest-with-spring-boot.git
```
```console
cd rest-with-spring-boot
```

2. Suba a aplicação com Docker Compose:
````git
docker-compose up -d
````

3. A API estará disponível em:
````arduino
http://localhost:80
````

### Opção 2 - Maven

#### Pré-Requisitos
- Java 21+ (https://adoptium.net)
- Maven (https://maven.apache.org)
- MySQL 8+ (https://dev.mysql.com/downloads/mysql/)

#### Passos
1. Crie um banco de dados MySQL vazio:
- Pelo MySQL:
```sql
CREATE DATABASE nome_do_banco;
```
- Pelo Terminal:
```console
mysql -u root -p
```
```sql
CREATE DATABASE nome_do_banco;
```

2. Configure as credenciais do banco no arquivo application.yml:
```css
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/(NOME_DA_DATABASE)?useTimezone=true&serverTimezone=UTC
    username: (SEU_USUARIO)
    password: (SENHA_DO_USUARIO)
```

3. Execute a aplicação:
```console
mvn spring-boot:run
```

## <a id="api-endpoints"></a>API Endpoints
A API fornece os seguintes endpoints:

### Person

```markdown
GET /api/person/v1 - Retorna uma lista de todos os person

PUT /api/person/v1 - Atualiza as informações de um person

POST /api/person/v1 - Adiciona um novo person

POST /api/person/v1/massCreation - Adiciona vários person

GET /api/person/v1/{id} - Encontra um person pelo id

DELETE /api/person/v1/{id} - Deleta um person pelo id

PATCH /api/person/v1/{id} - Desabilita um person pelo id

GET /api/person/v1/findPeopleByName/{firstName} - Encontra um person pelo primeiro nome

GET /api/person/v1/exportPage - Exporta uma página de vários person em formato XLSX ou CSV

GET /api/person/v1/export/{id} - Exporta as informações de um único person em formato PDF pelo id
```

### Book
```markdown
GET /api/book/v1 - Retorna uma lista de todos os book

PUT /api/book/v1 - Atualiza as informações de um book

POST /api/book/v1 - Adiciona um novo book

GET /api/book/v1/{id} - Encontra um book pelo id

DELETE /api/book/v1/{id} - Deleta um book pelo id
```

### Autenticação
```markdown
POST /auth/signin - Autentica um usuário e retorna um access token e um refresh token

PUT /auth/refresh/{username} - Renova o token de um usuário autenticado e retorna um novo access token

POST /auth/createUser - Cria um novo usuário (Somente para desenvolvedores)
```

### Arquivos e E-mail
```markdown
POST /api/file/v1/uploadFile - Realiza o upload de um único arquivo para o servidor

POST /api/file/v1/uploadMultipleFiles - Realiza o upload de diversos arquivos para o servidor

GET /api/file/v1/downloadFile/{fileName} - Realiza o download de um arquivo do servidor

POST /api/email/v1 - Envia um e-mail

POST /api/email/v1/withAttachment - Envia um e-mail com anexo
```

## <a id="autenticacao"></a>Autenticação
Para enviar requisições para as endpoints dessa API, é necessário realizar o login no endpoint /auth/signin com a operação POST. A API disponibiliza dois usuários para realizar o login, que deverá ser enviado no corpo da requisição para o endpoint de login, dessa forma:
```console
{
  "username": "gadiel",
  "password": "admin123"
}
```
```console
{
  "username": "leandro",
  "password": "admin234"
}
```

Caso o login seja realizado com sucesso, será retornado um access token que deverá ser usado no header de todas as requisições para as endpoints dessa API, por exemplo:
```console
curl -X POST http://localhost:8080/api/person/v1 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
```

## <a id="collections"></a>Collections
Também está disponível nos arquivos desse projeto as Collections do Postman com todo o ambiente configurado, incluindo a autenticação, ambiente e endpoints, para facilitar os testes.
- [Download da Collection](./postman/collection.json)
- [Download do Environment](./postman/environment.json)

### Como importar

1. Abra o Postman
2. Selecione um Workspace
3. Clique em **Import**
4. Selecione o arquivo `.json` localizado na pasta `postman`
5. A Collection será adicionada automaticamente

### Observação
Todas as variáveis de autenticação já estão configuradas no **environment.json**,
e um script captura automaticamente a resposta do endpoint `/auth/signin` e salva o `access_token` e o `refresh_token` no Environment,
então é necessário apenas enviar a requisição de login, que já vem com o corpo configurado, e os demais endpoints estarão prontos para uso.

## <a id="licenca"></a>Licença
[![Licence](https://img.shields.io/github/license/Gadiel-S/rest-with-spring-boot?style=for-the-badge)](./LICENSE)
