create sequence if not exists tokens_seq  start with 1 increment by 50;
create table if not exists bank_account
(
    deposit       float(53)    not null,
    start_balance float(53)    not null,
    user_id       varchar(255) not null,
    primary key (user_id)
);
create table if not exists emails
(
    is_main boolean,
    id      bigserial not null,
    email   varchar(255) unique,
    user_id varchar(255),
    primary key (id)
);

create table if not exists phones
(
    is_main boolean,
    id       bigserial not null,
    number  varchar(255) unique,
    user_id varchar(255),
    primary key (id)
);
create table if not exists tokens
(
    id        numeric(38, 0) not null,
    is_active boolean        not null,
    login     varchar(255),
    token     varchar(255),
    primary key (id)
);
create table if not exists users
(
    birthday    date,
    first_name  varchar(255),
    id          varchar(255) not null,
    last_name   varchar(255),
    login       varchar(255),
    middle_name varchar(255),
    password    varchar(255),
    primary key (id)
);
alter table if exists bank_account
    add constraint FOREIGH_KEY_ACCOUNT foreign key (user_id) references users;
alter table if exists emails
    add constraint FOREIGH_KEY_EMAIL foreign key (user_id) references users;
alter table if exists phones
    add constraint FOREIGH_KEY_PHONES foreign key (user_id) references users;








