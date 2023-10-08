--liquibase formatted sql

--changeset StormIT:002_1
insert into questions (id, name, category_id)
values (random_uuid(), 'Dlaczego warto uczyć się programowania',
        (select id from categories where name = 'Edukacja')),
       (random_uuid(), 'Dlaczego Java jest dobrym językiem na start',
        (select id from categories where name = 'Edukacja'));

insert into questions (id, name, category_id)
values (random_uuid(), 'Jakie są najzdrowsze warzywa?', (select id from categories where name = 'Zdrowie'));

insert into answers (id, name, question_id)
values (random_uuid(), 'Marchewka', (select id from questions where name = 'Jakie są najzdrowsze warzywa?')),
       (random_uuid(), 'Brokuł', (select id from questions where name = 'Jakie są najzdrowsze warzywa?')),
       (random_uuid(), 'Dynia', (select id from questions where name = 'Jakie są najzdrowsze warzywa?')),
       (random_uuid(), 'Groch', (select id from questions where name = 'Jakie są najzdrowsze warzywa?'));

insert into answers (id, name, question_id)
values (random_uuid(), 'Gdańsk',
        (select id from questions where name = 'Gdzie najlepiej spędzić wakacje z Polsce')),
       (random_uuid(), 'Bieszczady',
        (select id from questions where name = 'Gdzie najlepiej spędzić wakacje z Polsce')),
       (random_uuid(), 'Mazury',
        (select id from questions where name = 'Gdzie najlepiej spędzić wakacje z Polsce'));

