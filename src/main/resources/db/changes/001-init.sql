--liquibase formatted sql

--changeset StormIT:001_1

create table if not exists categories
(
    id   uuid not null
        primary key,
    name varchar(255)
);
create table if not exists questions
(
    id          uuid not null
        primary key,
    created     timestamp,
    modified    timestamp,
    name        varchar(255),
    category_id uuid
        constraint fkctl6tuf74n8cufkb3ulj6b3fc
        references categories
);
create table if not exists answers
(
    id          uuid not null
        primary key,
    name        varchar(255),
    question_id uuid
        constraint fk3erw1a3t0r78st8ty27x6v3g1
        references questions
);

--changeset StormIT:001_2
delete
from answers;
delete
from questions;
delete
from categories;

--changeset StormIT:001_3
insert into categories (id, name)
values (random_uuid(), 'Zdrowie'),
       (random_uuid(), 'Zwierzęta'),
       (random_uuid(), 'Turystyka'),
       (random_uuid(), 'Uroda i Styl'),
       (random_uuid(), 'Kultura'),
       (random_uuid(), 'Edukacja'),
       (random_uuid(), 'Gry'),
       (random_uuid(), 'Hobby'),
       (random_uuid(), 'Dom i Ogród'),
       (random_uuid(), 'Biznes'),
       (random_uuid(), 'Finanse'),
       (random_uuid(), 'Kulinaria'),
       (random_uuid(), 'Komputery'),
       (random_uuid(), 'Osobiste'),
       (random_uuid(), 'Motoryzacja'),
       (random_uuid(), 'Polityka'),
       (random_uuid(), 'Praca'),
       (random_uuid(), 'Prezenty'),
       (random_uuid(), 'Zakupy'),
       (random_uuid(), 'Elektronika'),
       (random_uuid(), 'Rozrywka'),
       (random_uuid(), 'Sex'),
       (random_uuid(), 'Związki'),
       (random_uuid(), 'Inne');

insert into questions (id, name, category_id, created)
values (random_uuid(), 'Gdzie najlepiej spędzić wakacje z Polsce',
        (select id from categories where name = 'Turystyka'), now()),
       (random_uuid(), 'Gdzie najlepiej spędzić wakacje z Europie',
        (select id from categories where name = 'Turystyka'), now());
