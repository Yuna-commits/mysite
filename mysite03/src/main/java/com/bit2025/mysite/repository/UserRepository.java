package com.bit2025.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2025.mysite.vo.UserVo;

@Repository
public class UserRepository {
	
	@Autowired
	private DataSource dataSource;

	public int insert(UserVo vo) {
		int result = 0;

		try (
			Connection conn = dataSource.getConnection();
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

	// 인자로 받은 UserVo로 회원 정보 수정
	public int update(UserVo vo) {
		int result = 0;
		
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("update user set name = ?, gender = ? where id = ?");
			PreparedStatement pstmt2 = conn.prepareStatement("update user set name = ?, password = password(?), gender = ? where id = ?");
		) {
			// password 변경 x -> password is blank	
			if(vo.getPassword().isBlank()) {	
				pstmt1.setString(1, vo.getName());
				pstmt1.setString(2, vo.getGender());
				pstmt1.setLong(3, vo.getId());
				
				result = pstmt1.executeUpdate();
			} else {
				pstmt2.setString(1, vo.getName());
				pstmt2.setString(2, vo.getPassword());
				pstmt2.setString(3, vo.getGender());
				pstmt2.setLong(4, vo.getId());
				
				result = pstmt2.executeUpdate();
			}
			
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}
	
	public UserVo findByEmailAndPassword(String email, String password) {
		UserVo result = null;

		try (
			Connection conn = dataSource.getConnection();
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
			Connection conn = dataSource.getConnection();
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
}
