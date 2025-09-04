package com.bit2025.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2025.mysite.common.Page;
import com.bit2025.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	
	@Autowired
	private DataSource dataSource;
	
	public int count() {
		int result = 0;
		
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("select count(*) from board");
		) {
			ResultSet rs = pstmt.executeQuery();
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
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("insert into board values(null, ?, ?, ?, 0, current_date(), (ifnull((select max(g_no) "
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
	
	public int insertReply(BoardVo vo) {
		int result = 0;

		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("insert into board values(null, ?, ?, ?, 0, current_date(), ?, ?, ?)");
		) {
			// Parameter Binding
			pstmt.setLong(1, vo.getUserId());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getContent());
			pstmt.setInt(4, vo.getgNo());
			pstmt.setInt(5, vo.getoNo() + 1);
			pstmt.setInt(6, vo.getDepth() + 1);

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
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("update board set title = ?, contents = ? where id = ? and user_id = ?");
		) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(3, vo.getId());
			pstmt.setLong(4, vo.getUserId());

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
			Connection conn = dataSource.getConnection();
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
	
	public int updateHierarchy(BoardVo boardVo) {
		int result = 0;
		
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("update board set o_no = o_no + 1 where g_no = ? and o_no > ?");
		) {
			pstmt.setInt(1, boardVo.getgNo());
			pstmt.setInt(2, boardVo.getoNo());

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
			Connection conn = dataSource.getConnection();
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
			Connection conn = dataSource.getConnection();
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

	public BoardVo findByIdAndUserId(Long id, Long userId) {
		BoardVo result = null;
		
		try (
			Connection conn = dataSource.getConnection();
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
				int gNo = rs.getInt(5);
				int oNo = rs.getInt(6);
				int depth = rs.getInt(7);

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
	public List<BoardVo> findAll(int offset) {
		List<BoardVo> result = new ArrayList<BoardVo>();

		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"select board.id, user.id, user.name, title, contents, hit, reg_date, depth "
					+ "from board join user on board.user_id = user.id "
					+ "order by g_no desc, o_no asc limit ? offset ?");
		) {
			pstmt.setInt(1, Page.BOARD_COUNT);
			pstmt.setInt(2, offset);
			
			ResultSet rs = pstmt.executeQuery();
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

}
