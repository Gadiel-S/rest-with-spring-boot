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
as informações de login estão detalhadas na seção **Autenticação**

## Sumário
- [Instalação e Execução](#instalacao-e-execucao)
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

## <a id="licenca"></a>Licença
[![Licence](https://img.shields.io/github/license/Gadiel-S/rest-with-spring-boot?style=for-the-badge)](./LICENSE)
