--
-- mysite : user
--
desc user;

-- 회원가입
insert into user(name, email, password, gender, join_date) values ('도우너', 'douner@gmail.com', password('1234'), 'male', current_date());

-- 회원 리스트
select * from user;

-- 삭제
delete from user where id=1;

-- 로그인
select id, name from user where email = 'dooly@gmail.com' and password = password('1234');

-- 회원 정보 수정
select * from user where id = 1;
update user set gender='Female' where id=4;