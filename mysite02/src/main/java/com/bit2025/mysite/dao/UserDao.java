package com.bit2025.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.bit2025.mysite.vo.UserVo;

public class UserDao {

	public int insert(UserVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("insert into user(name, email, password, gender, join_date) "
							+ "values (?, ?, password(?), ?, current_date())");
		) {
			// Parameter Binding
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}

	public UserVo findByEmailAndPassword(String email, String password) {
		UserVo result = null;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("select id, name from user where email = ? and password = password(?)");
		) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			// 1건만 조회, email&password가 틀리면 null
			if (rs.next()) {
				Long id = rs.getLong(1);
				String name = rs.getString(2);

				result = new UserVo();
				result.setId(id);
				result.setName(name);
			}

			rs.close();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}

	public UserVo findById(Long id) {
		UserVo result = null;
		
		try(
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from user where id = ?");
		){
			pstmt.setLong(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String name = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);
				String gender = rs.getString(5);
				Date joinDate = rs.getDate(6);
				
				result = new UserVo();
				result.setId(id);
				result.setName(name);
				result.setEmail(email);
				result.setPassword(password);
				result.setGender(gender);
				result.setJoinDate(joinDate);
			}
			
			rs.close();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}

	// 인자로 받은 UserVo로 회원 정보 수정
	public int update(UserVo vo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update user set name = ?, gender = ? where id = ?");
		) {
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getGender());
			pstmt.setLong(3, vo.getId());
				
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}
	
	public int updateAll(UserVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update user set name = ?, password = password(?), gender = ? where id = ?");
		) {
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getGender());
			pstmt.setLong(4, vo.getId());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			// 1. JDBC Driver 로드 -> 실패시 ClassNotFoundException
			Class.forName("org.mariadb.jdbc.Driver");

			// 2. Connection 연결 -> 호출 위치로 반환, Exception도 호출 위치로
			String url = "jdbc:mariadb://192.168.0.181:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.err.println("Driver Class Not Found");
		}

		return conn;
	}

}
