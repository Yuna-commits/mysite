--
-- mysite : guestbok
--

desc guestbook;
alter table guestbook auto_increment=1;
-- insert
insert into guestbook values(null, '둘리', password('1234'), '하이', now());

-- findAll
select * from guestbook;
select id, name, message, date_format(reg_date, '%Y-%m-%d %h:%i:%s') from guestbook order by reg_date desc;

-- deleteByIdAndPassword
delete from guestbook;
delete from guestbook where id=11 and password=password('1234');