--
-- mysite : board
--

desc board;

-- 새 글 작성(둘리)
-- 새 게시글의 o_no : 1, depth : 0
insert into board values(null, 2, '스압', '\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n테스트', 0, current_date(), (ifnull((select max(g_no) from board as sub_board), 0)+1), 1, 0);

-- 게시글, 답글 조회
-- 페이징 기능 추가 필요
select * from board;
select board.id, user.id, user.name, title, contents, hit, reg_date, g_no, o_no, depth from board join user on board.user_id = user.id order by g_no desc, o_no asc;
-- 게시글 id로 내용 보기
select title, user.id, user.name, contents from board join user on board.user_id = user.id where board.id = 2;

-- 조회수 증가
update board set hit = hit + 1 where id = 1;

-- 본인 게시글 삭제
delete from board where id = 1 and user_id = 1;