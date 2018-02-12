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
//================ �ε�==================
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

	
//================ ����߰�==================
	public void memberInsert(MemberDTO memberDTO) {//����߰�
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
	}//����߰�
	
//================ �ݱ�==================
	public void close() {//�ݱ�
		try {
			conn.close();
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//�ݱ�
	
	
//================ �α���Ȯ��==================
//	public boolean memberLogin(MemberDTO memberDTO) {//�α���Ȯ��
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
//	}//�α���Ȯ��

//================ �α���Ȯ��==================
	public int memberLogin(MemberDTO memberDTO) {// �α���Ȯ��
		  
	//================ �α��ν���==================
		int login = 0;	//(0,�α��� ���� )(1,Ȱ��ȭ)(2,��Ȱ��ȭ)
		
		try {
			sql = "select id,pw,hwal from member where id=? and pw=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getPw());
			rs = pstmt.executeQuery();
			while (rs.next()) {// �ִ� ���̵��ε�
	// ================ �α��� Ȱ��ȭ==================
				if (rs.getString("hwal").equals("y")) {// Ȱ��ȭ
					login = 1;
	// ================ �α��� ��Ȱ��ȭ==================
				} else {// ��Ȱ��ȭ
					login = 2;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return login;
	}	
	
	
	
	
//================ �α��� ��������==================
	public void sessionUse(MemberDTO memberDTO) { // �α��� ��������
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
	
//================ ȸ�� ��ü���==================
//	public ArrayList<MemberDTO> memberList(MemberDTO memberDTO) {// ȸ����ü���
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
//	}// ȸ����ü���
	
//================ �����ڱ��� ȸ�� ��Ȱ��ȭ(login=2)==================
	public MemberDTO disable(MemberDTO memberDTO) {//��Ȱ��ȭ
		sql = "update member set hwal='n' where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberDTO;
	}//��Ȱ��ȭ��Ű��
	
//================ �����ڱ��� ȸ�� ��Ȱ��ȭ(login=1)==================
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
	}// ��Ȱ��ȭ��Ű��	 
	
//================ �����ڱ��� ȸ�� Ż��(delete)==================

	public void delete(MemberDTO memberDTO) {
		sql = "delete from member where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// �����ڱ��� ȸ��Ż��	 
	 
//================ ȸ��Ż��==================
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
	//================ ȸ������==================
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
////================ ȸ����ü���==================
	public ArrayList<MemberDTO> memberList(MemberDTO memberDTO){//ȸ����ü���
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
	}//ȸ����ü���

	
//	//================ ����Ƽã��==================
//		public String memberFindId(MemberDTO memberDTO) {//���̵�ã��
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
//		}//���̵�ã��
//	//================ ��й�ȣã��==================
//		public String memberFindPw(MemberDTO memberDTO) {//��й�ȣã��
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
//		}//��й�ȣã��
//		
//	//================ �ߺ����̵�Ȯ��==================
//		public boolean memberIdCheck(MemberDTO memberDTO) {//�ߺ����̵�Ȯ��
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
//		}//�ߺ����̵�Ȯ��
	
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
//		  } return oneoneone;////�ߺ�üũ����¼���3
//		  
//		 }
	
	
}
