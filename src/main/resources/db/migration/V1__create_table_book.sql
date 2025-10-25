create table if not exists estoque.book(
    id bigserial primary key,
    referencia bigint not null,
    quantidade int default 0
);