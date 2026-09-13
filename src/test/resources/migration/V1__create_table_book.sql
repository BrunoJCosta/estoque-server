create schema if not exists estoque_server;
create table if not exists estoque_server.book(
    id bigserial primary key,
    referencia bigint not null,
    quantidade int default 0
);