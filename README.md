# oop-social-media

## Description
Uma aplicação extremamente simples com um CRUD de usuários e mensagens.

## Installation
PTBR: Instale o docker https://www.docker.com/get-started
faça download da imagem do postgres usando o comando:

ENG: Install docker https://www.docker.com/get-started and download the postgres image using the command:
```bash
$docker pull postgres
```
PTBR: Crie uma imagem do banco da rede social usando o comando:

ENG: Create a social network database image using the command:
```bash
$docker build -t rede_social .
```
PTBR: Crie um container do banco usando o comando:

ENG: Create a database container using the command:
```bash
$docker run -d -p 5432:5432 --name rede_social_container rede_social
```
PTBR:
Isso criará um contêiner PostgreSQL com o nome de usuário "admin", senha "12345" e o banco de dados "rede_social" com as tabelas definidas no script SQL.

ENG:
It'll create a PostgreSQL container with username "admin", password "12345" and database "rede_social" with the tables defined in the SQL script.
