create sequence tokens_seq start with 1 increment by 50;

create table users
(
    id          varchar(255) default gen_random_uuid(),
    birthday    date,
    first_name  varchar(255),
    last_name   varchar(255),
    login       varchar(255) unique not null,
    middle_name varchar(255),
    password    varchar(255),
    primary key (id)
);

create table bank_account
(
    deposit       float(53)    not null,
    start_balance float(53)    not null,
    user_id       varchar(255) not null,
    check ( deposit >= 0 ),
    foreign key (user_id) references users
);

create table emails
(
    id      bigserial           not null,
    email   varchar(254) unique not null,
    is_main boolean,
    user_id varchar(255)        not null,
    primary key (id),
    foreign key (user_id) references users
);
create table phones
(
    is_main boolean,
    id      bigserial    not null,
    number  varchar(255) unique,
    user_id varchar(255) not null,
    primary key (id),
    foreign key (user_id) references users
);
create table tokens
(
    id        numeric(38, 0) not null,
    is_active boolean        not null,
    login     varchar(255),
    token     varchar(255),
    primary key (id)
);









