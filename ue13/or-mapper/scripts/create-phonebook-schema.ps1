$schema=@'
create schema if not exists PhoneBookDb;
use PhoneBookDb;
drop table if exists tbl_person;
create table tbl_person (
  id int,
  first_name varchar(20),
  last_name varchar(25),
  address varchar(30),
  phone_number varchar(20),
  constraint pk_tbl_person primary key (id)
);

insert into tbl_person values (1, 'Franz', 'Huber', '4020 Linz', '+43 732 55 33 22');
insert into tbl_person values (2, 'Susanne', 'Mittermair', '4232 Hagenberg', '+43 7236 11 33');
insert into tbl_person values (3, 'Daniela', 'Gruber', '5020 Salzburg', '+43 635 70 30 405');
'@

echo $schema | docker exec -i mysql mysql
