package com.bit2025.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bit2025.mysite.vo.BoardVo;

public class BoardDao {

	public int insert(BoardVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"insert into board values(null, ?, ?, ?, 0, current_date(), (ifnull((select max(g_no) "
							+ "from board as sub_board), 0)+1), 1, 0)");
		) {
			// Parameter Binding
			pstmt.setLong(1, vo.getUserId());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getContent());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}
	
	public int insertReply(BoardVo vo, int[] hierNo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"insert into board values(null, ?, ?, ?, 0, current_date(), ?, ?, ?)");
		) {
			// Parameter Binding
			pstmt.setLong(1, vo.getUserId());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getContent());

			pstmt.setInt(4, hierNo[0]);
			pstmt.setInt(5, hierNo[1] + 1);
			pstmt.setInt(6, hierNo[2] + 1);

			result = pstmt.executeUpdate();
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
			PreparedStatement pstmt = conn.prepareStatement("update board set title = ?, contents = ? where id = ?");
		) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(3, vo.getId());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}
	
	// view로 조회한 게시글의 조회수 증가
	public int updateHitCount(Long id) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update board set hit = hit + 1 where id = ?");
		) {
			pstmt.setLong(1, id);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}
	
	public int updateHierarchy(int[] hierNo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update board set o_no = o_no + 1 where g_no = ? and o_no > ?");
		) {
			pstmt.setInt(1, hierNo[0]);
			pstmt.setInt(2, hierNo[1]);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}

	public int deleteById(Long id) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from board where id = ?");
		) {
			pstmt.setLong(1, id);
			
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
					"select user.id, title, user.name, contents, hit, g_no, o_no, depth from board join user on board.user_id = user.id where board.id = ?");
		) {
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				Long userId = rs.getLong(1);
				String title = rs.getString(2);
				String userName = rs.getString(3);
				String contents = rs.getString(4);
				int hit = rs.getInt(5);
				int gNo = rs.getInt(6);
				int oNo = rs.getInt(7);
				int depth = rs.getInt(8);

				result = new BoardVo();
				result.setId(id);
				result.setUserId(userId);
				result.setTitle(title);
				result.setUserName(userName);
				result.setContent(contents);
				result.setHit(hit);
				result.setgNo(gNo);
				result.setoNo(oNo);
				result.setDepth(depth);
			}
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}
	
	// 전체 게시글 리스트 출력
	public List<BoardVo> findAll() {
		List<BoardVo> result = new ArrayList<BoardVo>();

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"select board.id, user.id, user.name, title, contents, hit, reg_date, depth from board join user on board.user_id = user.id order by g_no desc, o_no asc");
			ResultSet rs = pstmt.executeQuery();
		) {
			while (rs.next()) {
				Long id = rs.getLong(1);
				Long userId = rs.getLong(2);
				String userName = rs.getString(3);
				String title = rs.getString(4);
				String contents = rs.getString(5);
				int hit = rs.getInt(6);
				Date regDate = rs.getDate(7);
				int depth = rs.getInt(8);

				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setUserId(userId);
				vo.setUserName(userName);
				vo.setTitle(title);
				vo.setContent(contents);
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
