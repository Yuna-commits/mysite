--
-- mysite : board
--

desc board;

-- 새 글 작성(둘리)
-- 새 게시글의 o_no : 1, depth : 0
insert into board values(null, 8, '둘리야', '시끄럽다', 0, current_date(), (ifnull((select max(g_no) from board as sub_board), 0)+1), 1, 0);

-- 게시글, 답글 조회
-- 페이징 기능 추가 필요
select count(*) from board;
select board.id, user.id, user.name, title, contents, hit, reg_date, g_no, o_no, depth from board join user on board.user_id = user.id order by g_no desc, o_no asc;
select board.id, user.id, user.name, title, contents, hit, reg_date, g_no, o_no, depth from board join user on board.user_id = user.id order by g_no desc, o_no asc limit 5 offset 5;
-- 게시글 id로 내용 보기
select title, user.name, contents from board join user on board.user_id = user.id where board.id = 3 and user.id = 1;

-- 게시글 답글 달기
-- 1
update board set o_no = o_no + 1 where g_no = 6 and o_no > 3;
-- 2
insert into board values(null, 5, '땡!', '틀렸어', 0, current_date(), 6, 4, 2);
insert into board values(null, 3, '정답은 철수!', '철수를 구하시오', 0, current_date(), 6, 2, 1);

-- 조회수 증가
update board set hit = hit + 1 where id = 1;

-- 본인 게시글 수정
update board set title = '수정', contents = '게시글\n수정\n테스트' where id = 3 and user_id = 1;

-- 본인 게시글 삭제
delete board, user from board join user on board.user_id = user.id where board.id = 48 and user.password = password('1111');