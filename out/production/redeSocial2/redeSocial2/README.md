# oop-social-media

## Description
Uma aplicação extremamente simples com um CRUD de usuários e mensagens.

## Installation
Instale o docker https://www.docker.com/get-started
faça download da imagem do postgres usando o comando:
```bash
$docker pull postgres
```
Crie uma imagem do banco da rede social usando o comando:
```bash
$docker build -t rede_social .
```
Crie um container do banco usando o comando:
```bash
$docker run -d -p 5432:5432 --name rede_social_container rede_social
```
Isso criará um contêiner PostgreSQL com o nome de usuário "admin", senha "12345" e o banco de dados "rede_social" com as tabelas definidas no script SQL.
