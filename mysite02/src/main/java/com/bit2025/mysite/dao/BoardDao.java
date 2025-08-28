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

	// 전체 게시글 리스트 출력
	public List<BoardVo> findAll() {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select board.id, user.name, title, contents, hit, reg_date, g_no, o_no, depth from board join user on board.user_id = user.id order by g_no desc, o_no asc");
			ResultSet rs = pstmt.executeQuery();
		){
			while(rs.next()) {
				Long id = rs.getLong(1);
				String userName = rs.getString(2);
				String title = rs.getString(3);
				String contents = rs.getString(4);
				int hit = rs.getInt(5);
				Date regDate = rs.getDate(6);
				int groupNo = rs.getInt(7);
				int orderNo = rs.getInt(8);
				int depth = rs.getInt(9);
				
				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setUserName(userName);
				vo.setTitle(title);
				vo.setContent(contents);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				
				result.add(vo);
			}
		} catch(SQLException e) {
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
				PreparedStatement pstmt = conn.prepareStatement("select title, user.name, contents from board join user on board.user_id = user.id where board.id = ?");
			){
				pstmt.setLong(1, id);
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					String title = rs.getString(1);
					String userName = rs.getString(2);
					String contents = rs.getString(3);
					
					result = new BoardVo();
					result.setTitle(title);
					result.setUserName(userName);
					result.setContent(contents);
				}
			} catch(SQLException e) {
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
		} catch(ClassNotFoundException e) {
			System.err.println("드라이버 로딩에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return conn;
	}

}
