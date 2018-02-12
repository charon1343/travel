package kr.co.daegu.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("*.project")
public class MemberFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public MemberDTO memberDTO;
	public MemberDAO memberDAO;

	public MemberFrontController() {
		memberDTO = new MemberDTO();
		memberDAO = new MemberDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//================ 초기설정 URI설정==================
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		
//================ 회원가입==================		
		if (command.equals("/register.project")) {
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			String pwch = request.getParameter("pwch");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			memberDTO.setId(id);
			memberDTO.setPw(pw);
			memberDTO.setPwch(pwch);
			memberDTO.setName(name);
			memberDTO.setEmail(email);
			memberDAO.memberInsert(memberDTO);
			response.sendRedirect("template.jsp");
		} //회원가입
		

//================ 로그인==================
		else if (command.equals("/loginform.project")) {//로그인
			   HttpSession session = request.getSession(false);
			   String id = request.getParameter("id");
			   String pw = request.getParameter("pw");
			   memberDTO.setId(id);
			   memberDTO.setPw(pw);
			   memberDAO.sessionUse(memberDTO);//세션유지
			   int login = memberDAO.memberLogin(memberDTO);//회원상태확인
			   //리턴0 -> 로그인실패
			   //리턴1 -> 로그인성공
			   //리턴2 -> 로그인성공했으나 비활성화상태			   
			   if (login == 0) {
			    System.out.println("실패");
			    out.print("<script type='text/javascript'>");
			    out.print("alert('로그인을 실패하였습니다.');");
			    out.print("alert('다시 로그인해 주세요.');");
			    out.print("location='index.jsp'");
			    out.print("</script>");
			   } else if (login==1){
				System.out.println("성공");
				session.setAttribute("id", memberDTO.getId());
				session.setAttribute("name", memberDTO.getName());
				session.setAttribute("email", memberDTO.getEmail());
				response.sendRedirect("template.jsp");
			   } else if (login==2) {
			    System.out.println("비활성화상태입니다.");
			    out.print("<script type='text/javascript'>");
			    out.print("alert('해당 아이디는 현재 비활성화상태입니다.');");
			    out.print("location='template.jsp'");
			    out.print("</script>");
			   }else {

			   }
			  }		
			
//================ 로그아웃==================
		else if(command.equals("/logout.project")){//로그아웃
			HttpSession session = request.getSession(false);
			session.invalidate();
			response.sendRedirect("template.jsp");
		}//로그아웃
		
//================ 회원전체출력(관리자메뉴)==================
		else if (command.equals("/memberList.project")) {
			ArrayList<MemberDTO> memberList = memberDAO.memberList(memberDTO);
			RequestDispatcher dis = request.getRequestDispatcher("template.jsp?page=memberList");
			request.setAttribute("memberList", memberList);
			dis.forward(request, response);
		}
//================ 회원 비활성화==================
		else if(command.equals("/membertal.project")) {//비활성화  
		     String id = request.getParameter("id");
		     String hwal = request.getParameter("hwal");
		     memberDTO.setId(id); 
		     memberDTO.setHwal(hwal);
		     memberDAO.disable(memberDTO);
		     response.sendRedirect("memberList.project");
		    }//비황성화
//================ 회원 재활성화==================
		else if(command.equals("/membertal2.project")) {//재활성화  
		     String id = request.getParameter("id");
		     String hwal = request.getParameter("hwal");
		     memberDTO.setId(id); 
		     memberDTO.setHwal(hwal);
		     memberDAO.able(memberDTO);
		     response.sendRedirect("memberList.project");
		    }//재활성화
//================ 관리자권한 회원 탈퇴==================
		else if(command.equals("/delete.project")) {//관리자권한 회원탈퇴
		     String id = request.getParameter("id");
		     memberDTO.setId(id);
		     memberDAO.delete(memberDTO);
		     response.sendRedirect("memberList.project");
		    }
/*================ 회원탈퇴==================*/
		else if(command.equals("/escape.project")) {
			String id = request.getParameter("id");
			memberDTO.setId(id);
			memberDAO.escape(memberDTO);
			HttpSession session = request.getSession(false);
			session.removeAttribute("id");
			session.removeAttribute("name");
			session.removeAttribute("email");
			response.sendRedirect("template.jsp");
		}
/*================ 회원수정==================*/
		else if(command.equals("/update.project")) {
			HttpSession session = request.getSession(false);
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			memberDTO.setId(id);
			memberDTO.setName(name);
			memberDTO.setEmail(email);
			memberDAO.update(memberDTO);
			session.setAttribute("id", memberDTO.getId());
			session.setAttribute("name", memberDTO.getName());
			session.setAttribute("email", memberDTO.getEmail());
			response.sendRedirect("template.jsp");
		}
		
//================ 로그인==================
//		else if (command.equals("/loginform.project")) {//로그인
//			HttpSession session = request.getSession(false);
//			String id = request.getParameter("id");
//			String pw = request.getParameter("pw");
//			memberDTO.setId(id);
//			memberDTO.setPw(pw);
//			memberDAO.Output(memberDTO);
//			boolean login = memberDAO.memberLogin(memberDTO);
//			if (login == true) {
//				session.setAttribute("id", memberDTO.getId());
//				session.setAttribute("name", memberDTO.getName());
//				session.setAttribute("email", memberDTO.getEmail());
//				System.out.println("성공");
//			}else {
//				System.out.println("실패");
//			}
//			response.sendRedirect("template.jsp");
//		}//로그인
		
//		else if (command.equals("/idcheck.member")) {
//		// out.print("아이디 중복체크");
//		String id = request.getParameter("id");
//		memberDTO.setId(id);
//		boolean check = memberDAO.memberIdCheck(memberDTO);
//		if (check) {
//			out.print(id + "는 이미 존재하는 아이디입니다.");
//		} else {
//			out.print(id + "는 가입가능합니다.");
//		}
//		out.print("<input type='button' value='종료' onClick='self.close()'>");
//		// System.out.println(id);
//	} else if (command.equals("/findId.member")) {
//		String name = request.getParameter("name");
//		String email = request.getParameter("email");
//
//		memberDTO.setIrum(name);
//		memberDTO.setEmail(email);
//		String findId = memberDAO.memberFindId(memberDTO);
//		if (findId.equals("")) {
//			out.print("일치하는 정보가 없습니다.");
//		} else {
//			out.print("아이디는 '" + findId + "'입니다.");
//		}
//	} else if (command.equals("/findPw.member")) {
//		String id = request.getParameter("id");
//		String name = request.getParameter("name");
//		String email = request.getParameter("email");
//
//		memberDTO.setId(id);
//		memberDTO.setIrum(name);
//		memberDTO.setEmail(email);
//		String findPw = memberDAO.memberFindPw(memberDTO);
//		if (findPw.equals("")) {
//			out.print("일치하는 정보가 없습니다.");
//		} else {
//			out.print("비밀번호는 '" + findPw + "'입니다.");
//		}
//		out.print("<input type='button' value='종료' onClick='self.close()'>");
//	}
		
	}
}