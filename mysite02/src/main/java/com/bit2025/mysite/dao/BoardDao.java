package com.bit2025.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bit2025.mysite.common.Page;
import com.bit2025.mysite.vo.BoardVo;

public class BoardDao {
	
	public int count(String keyword) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn
					.prepareStatement("select count(*) from board");
			PreparedStatement pstmt2 = conn
					.prepareStatement("select count(*) from board where title like ? or contents like ?");
		) {
			ResultSet rs = null;

			if (keyword == null) {
				rs = pstmt1.executeQuery();
			} else {// 키워드 게시글
				pstmt2.setString(1, "%" + keyword + "%");
				pstmt2.setString(2, "%" + keyword + "%");
				
				rs = pstmt2.executeQuery();
			}
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}

	public int insert(BoardVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn
					.prepareStatement("insert into board values(null, ?, ?, ?, 0, now(), (ifnull((select max(g_no) "
							+ "from board as sub_board), 0)+1), 1, 0)");
			PreparedStatement pstmt2 = conn
					.prepareStatement("insert into board values(null, ?, ?, ?, 0, now(), ?, ?, ?)");
		) {
			if(vo.getGroupNo() == null) {// 새 글 작성
				// Parameter Binding
				pstmt1.setLong(1, vo.getUserId());
				pstmt1.setString(2, vo.getTitle());
				pstmt1.setString(3, vo.getContents());

				result = pstmt1.executeUpdate();
			} else {//답글 작성
				// Parameter Binding
				pstmt2.setLong(1, vo.getUserId());
				pstmt2.setString(2, vo.getTitle());
				pstmt2.setString(3, vo.getContents());
				pstmt2.setInt(4,vo.getGroupNo());
				pstmt2.setInt(5, vo.getOrderNo() + 1);
				pstmt2.setInt(6, vo.getDepth() + 1);

				result = pstmt2.executeUpdate();
			}
			
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}

	public int update(BoardVo vo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("update board set title = ?, contents = ? where id = ?");
		) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getId());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}
	
	// view로 조회한 게시글의 조회수 증가
	public int updateHit(Long id) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("update board set hit = hit + 1 where id = ?");
		) {
			pstmt.setLong(1, id);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}
	
	public int updateOrderNo(BoardVo vo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("update board set o_no = o_no + 1 where g_no = ? and o_no > ?");
		) {
			pstmt.setInt(1, vo.getGroupNo());
			pstmt.setInt(2, vo.getOrderNo());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}

	public int delete(Long id, Long userId) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from board where id = ? and user_id = ?");
		) {
			pstmt.setLong(1, id);
			pstmt.setLong(2, userId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}

	// 게시글 id로 게시글의 내용 보기
	public BoardVo findById(Long id) {
		BoardVo result = null;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"select user.id, title, user.name, contents, hit, g_no, o_no, depth "
					+ "from board join user on board.user_id = user.id "
					+ "where board.id = ?");
		) {
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				Long userId = rs.getLong(1);
				String title = rs.getString(2);
				String userName = rs.getString(3);
				String contents = rs.getString(4);
				int hit = rs.getInt(5);
				int groupNo = rs.getInt(6);
				int orderNo = rs.getInt(7);
				int depth = rs.getInt(8);

				result = new BoardVo();
				result.setId(id);
				result.setUserId(userId);
				result.setTitle(title);
				result.setUserName(userName);
				result.setContents(contents);
				result.setHit(hit);
				result.setGroupNo(groupNo);
				result.setOrderNo(orderNo);
				result.setDepth(depth);
			}
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}
	
	public BoardVo findByIdAndUserId(Long id, Long userId) {
		BoardVo result = null;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"select title, user.name, contents, hit, g_no, o_no, depth "
					+ "from board join user on board.user_id = user.id "
					+ "where board.id = ? and user.id = ?");
		) {
			pstmt.setLong(1, id);
			pstmt.setLong(2, userId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String title = rs.getString(1);
				String userName = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				int groupNo = rs.getInt(5);
				int orderNo = rs.getInt(6);
				int depth = rs.getInt(7);

				result = new BoardVo();
				result.setId(id);
				result.setUserId(userId);
				result.setTitle(title);
				result.setUserName(userName);
				result.setContents(contents);
				result.setHit(hit);
				result.setGroupNo(groupNo);
				result.setOrderNo(orderNo);
				result.setDepth(depth);
			}
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}
	
	// 전체 게시글 리스트 출력
	public List<BoardVo> findAllByKeyword(String keyword, int offset) {
		List<BoardVo> result = new ArrayList<BoardVo>();

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement(
					"select board.id, user.id, user.name, title, contents, hit, "
					+ "date_format(reg_date, '%Y-%m-%d %p %h:%i:%s') as regDate, depth "
					+ "from board join user on board.user_id = user.id "
					+ "order by g_no desc, o_no asc "
					+ "limit ? offset ?");
			PreparedStatement pstmt2 = conn.prepareStatement(
					"select board.id, user.id, user.name, title, contents, hit, "
					+ "date_format(reg_date, '%Y-%m-%d %p %h:%i:%s') as regDate, depth "
					+ "from board join user on board.user_id = user.id "
					+ "where (title like ? or contents like ?) "
					+ "order by g_no desc, o_no asc "
					+ "limit ? offset ?");
		) {
			ResultSet rs = null;
			
			if(keyword == null) {
				pstmt1.setInt(1, Page.BOARD_SIZE);
				pstmt1.setInt(2, offset);	
				
				rs = pstmt1.executeQuery();
			} else {// 키워드 게시글
				pstmt2.setString(1, "%" + keyword + "%");
				pstmt2.setString(2, "%" + keyword + "%");
				pstmt2.setInt(3, Page.BOARD_SIZE);
				pstmt2.setInt(4, offset);	
				
				rs = pstmt2.executeQuery();
			}
			 
			while (rs.next()) {
				Long id = rs.getLong(1);
				Long userId = rs.getLong(2);
				String userName = rs.getString(3);
				String title = rs.getString(4);
				String contents = rs.getString(5);
				int hit = rs.getInt(6);
				String regDate = rs.getString(7);
				int depth = rs.getInt(8);

				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setUserId(userId);
				vo.setUserName(userName);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		return result;
	}

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");

			String url = "jdbc:mariadb://192.168.0.181:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로딩에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return conn;
	}

}
