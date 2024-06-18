$schema=@'
create schema if not exists PhoneBookDb;
use PhoneBookDb;
drop table if exists Person;
create table Person (id int auto_increment primary key, 
                     first_name varchar(20), last_name varchar(25), address varchar(30),
                     phone_number varchar(20));
'@

echo $schema | docker exec -i mysql mysql
