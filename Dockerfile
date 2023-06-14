FROM postgres:latest

ENV POSTGRES_USER admin
ENV POSTGRES_PASSWORD 12345
ENV POSTGRES_DB rede_social

COPY redeSocial.sql /docker-entrypoint-initdb.d/

EXPOSE 5432
