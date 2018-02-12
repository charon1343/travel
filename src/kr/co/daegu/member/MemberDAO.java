package kr.co.daegu.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO {
	private DataSource dataFactory;
	private Connection conn;
	private String sql;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private ArrayList<MemberDTO> memberList;
//================ 로드==================
	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			dataFactory = (DataSource)ctx.lookup("java:comp/env/jdbc/Oracle11g");
			conn = dataFactory.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	
//================ 멤버추가==================
	public void memberInsert(MemberDTO memberDTO) {//멤버추가
		try {
			sql = "insert into member(id,pw,pwch,name,email) values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getPw());
			pstmt.setString(3, memberDTO.getPwch());
			pstmt.setString(4, memberDTO.getName());
			pstmt.setString(5, memberDTO.getEmail());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//멤버추가
	
//================ 닫기==================
	public void close() {//닫기
		try {
			conn.close();
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//닫기
	
	
//================ 로그인확인==================
//	public boolean memberLogin(MemberDTO memberDTO) {//로그인확인
//		boolean login=false;
//		try {
//			sql="select id,pw from member where id=? and pw=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, memberDTO.getId());
//			pstmt.setString(2, memberDTO.getPw());
//			rs=pstmt.executeQuery();
//			while(rs.next()) {
//				login=true;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return login;
//	}//로그인확인

//================ 로그인확인==================
	public int memberLogin(MemberDTO memberDTO) {// 로그인확인
		  
	//================ 로그인실패==================
		int login = 0;	//(0,로그인 실패 )(1,활성화)(2,비활성화)
		
		try {
			sql = "select id,pw,hwal from member where id=? and pw=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getPw());
			rs = pstmt.executeQuery();
			while (rs.next()) {// 있는 아이디인데
	// ================ 로그인 활성화==================
				if (rs.getString("hwal").equals("y")) {// 활성화
					login = 1;
	// ================ 로그인 비활성화==================
				} else {// 비활성화
					login = 2;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return login;
	}	
	
	
	
	
//================ 로그인 세션유지==================
	public void sessionUse(MemberDTO memberDTO) { // 로그인 세션유지
		String id = null;
		String name = null;
		String email = null;
		try {
			sql = "select id,name,email from member where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getString("id");
				name = rs.getString("name");
				email = rs.getString("email");
			}
			memberDTO.setId(id);
			memberDTO.setName(name);
			memberDTO.setEmail(email);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//================ 회원 전체출력==================
//	public ArrayList<MemberDTO> memberList(MemberDTO memberDTO) {// 회원전체출력
//		try {
//			sql = "select id,pw,pwch,name,email,hwal from member where id!='admin' order by id asc";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			memberList = new ArrayList<MemberDTO>();
//			while (rs.next()) {
//				memberDTO = new MemberDTO();
//				String id = rs.getString("id");
//				String pw = rs.getString("pw");
//				String pwch = rs.getString("pwch");
//				String name = rs.getString("name");
//				String email = rs.getString("email");
//				String hwal = rs.getString("hwal");
//				memberDTO.setId(id);
//				memberDTO.setPw(pw);
//				memberDTO.setPwch(pwch);
//				memberDTO.setName(name);
//				memberDTO.setEmail(email);
//				memberDTO.setHwal(hwal);
//				memberList.add(memberDTO);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return memberList;
//	}// 회원전체출력
	
//================ 관리자권한 회원 비활성화(login=2)==================
	public MemberDTO disable(MemberDTO memberDTO) {//비활성화
		sql = "update member set hwal='n' where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberDTO;
	}//비활성화시키기
	
//================ 관리자권한 회원 재활성화(login=1)==================
	public MemberDTO able(MemberDTO memberDTO) {
		sql = "update member set hwal='y' where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberDTO;
	}// 재활성화시키기	 
	
//================ 관리자권한 회원 탈퇴(delete)==================

	public void delete(MemberDTO memberDTO) {
		sql = "delete from member where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// 관리자권한 회원탈퇴	 
	 
//================ 회원탈퇴==================
	 public void escape(MemberDTO memberDTO) {
		 
		try {
			sql = "delete from member where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
	 }
	//================ 회원수정==================
	public void update(MemberDTO memberDTO) {
		try {
				sql="update member set name=?,email=? where id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberDTO.getName());
				pstmt.setString(2, memberDTO.getEmail());
				pstmt.setString(3, memberDTO.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
////================ 회원전체출력==================
	public ArrayList<MemberDTO> memberList(MemberDTO memberDTO){//회원전체출력
		try {
			sql= "select id,name from member";
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			memberList = new ArrayList<MemberDTO>();
			while(rs.next()) {
			memberDTO = new MemberDTO();
			String id=rs.getString("id");
			String name= rs.getString("name");
			memberDTO.setId(id);
			memberDTO.setName(name);
			memberList.add(memberDTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return memberList;
	}//회원전체출력

	
//	//================ 아이티찾기==================
//		public String memberFindId(MemberDTO memberDTO) {//아이디찾기
//			String id = new String();
//			
//			try {
//				sql = "select id from member where name=? and email=?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, memberDTO.getName());
//				pstmt.setString(2, memberDTO.getEmail());
//				rs = pstmt.executeQuery();
//				while(rs.next()) {
//					id = rs.getString("id");
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			return id;
//		}//아이디찾기
//	//================ 비밀번호찾기==================
//		public String memberFindPw(MemberDTO memberDTO) {//비밀번호찾기
//			String pw = new String();
//			
//			try {
//				sql = "select pw from member where id=? and name=? and email=?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, memberDTO.getId());
//				pstmt.setString(2, memberDTO.getName());
//				pstmt.setString(3, memberDTO.getEmail());
//				rs = pstmt.executeQuery();
//				while(rs.next()) {
//					pw = rs.getString("pw");
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			return pw;
//		}//비밀번호찾기
//		
//	//================ 중복아이디확인==================
//		public boolean memberIdCheck(MemberDTO memberDTO) {//중복아이디확인
//			boolean idcheck = false;
//			
//			try {
//				sql = "select id from member where id=?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, memberDTO.getId());
//				rs = pstmt.executeQuery();
//				while(rs.next()) {
//					idcheck = true;
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			return idcheck;
//		}//중복아이디확인
	
//	public void memberOneOne(MemberDTO memberDTO) {
//		  sql = "insert into member(id,pw,pwch,name,email) values(?,?,?,?,?)";
//		  try {
//		   pstmt = conn.prepareStatement(sql);
//		   pstmt.setString(1, memberDTO.getId());
//		   pstmt.setString(2, memberDTO.getPw());
//		   pstmt.setString(3, memberDTO.getPwch());
//		   pstmt.setString(4, memberDTO.getName());
//		   pstmt.setString(5, memberDTO.getEmail());
//		      pstmt.executeUpdate();
//		  } catch (SQLException e) {
//		   e.printStackTrace();
//		  } 
//		 }
//	
//	public boolean memberOneOneOne(MemberDTO memberDTO) {
//		  boolean oneoneone = false;
//		  sql = "select id from member where id=?";
//		  try {
//		   pstmt = conn.prepareStatement(sql);
//		   pstmt.setString(1, memberDTO.getId());
//		   rs = pstmt.executeQuery();
//		   while(rs.next()) {
//		    oneoneone = true;
//		   }
//		  } catch (SQLException e) {
//		   e.printStackTrace();
//		  } return oneoneone;////중복체크만드는순서3
//		  
//		 }
	
	
}
