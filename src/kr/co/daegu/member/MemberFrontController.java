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
//================ �ʱ⼳�� URI����==================
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		
//================ ȸ������==================		
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
		} //ȸ������
		

//================ �α���==================
		else if (command.equals("/loginform.project")) {//�α���
			   HttpSession session = request.getSession(false);
			   String id = request.getParameter("id");
			   String pw = request.getParameter("pw");
			   memberDTO.setId(id);
			   memberDTO.setPw(pw);
			   memberDAO.sessionUse(memberDTO);//��������
			   int login = memberDAO.memberLogin(memberDTO);//ȸ������Ȯ��
			   //����0 -> �α��ν���
			   //����1 -> �α��μ���
			   //����2 -> �α��μ��������� ��Ȱ��ȭ����			   
			   if (login == 0) {
			    System.out.println("����");
			    out.print("<script type='text/javascript'>");
			    out.print("alert('�α����� �����Ͽ����ϴ�.');");
			    out.print("alert('�ٽ� �α����� �ּ���.');");
			    out.print("location='index.jsp'");
			    out.print("</script>");
			   } else if (login==1){
				System.out.println("����");
				session.setAttribute("id", memberDTO.getId());
				session.setAttribute("name", memberDTO.getName());
				session.setAttribute("email", memberDTO.getEmail());
				response.sendRedirect("template.jsp");
			   } else if (login==2) {
			    System.out.println("��Ȱ��ȭ�����Դϴ�.");
			    out.print("<script type='text/javascript'>");
			    out.print("alert('�ش� ���̵�� ���� ��Ȱ��ȭ�����Դϴ�.');");
			    out.print("location='template.jsp'");
			    out.print("</script>");
			   }else {

			   }
			  }		
			
//================ �α׾ƿ�==================
		else if(command.equals("/logout.project")){//�α׾ƿ�
			HttpSession session = request.getSession(false);
			session.invalidate();
			response.sendRedirect("template.jsp");
		}//�α׾ƿ�
		
//================ ȸ����ü���(�����ڸ޴�)==================
		else if (command.equals("/memberList.project")) {
			ArrayList<MemberDTO> memberList = memberDAO.memberList(memberDTO);
			RequestDispatcher dis = request.getRequestDispatcher("template.jsp?page=memberList");
			request.setAttribute("memberList", memberList);
			dis.forward(request, response);
		}
//================ ȸ�� ��Ȱ��ȭ==================
		else if(command.equals("/membertal.project")) {//��Ȱ��ȭ  
		     String id = request.getParameter("id");
		     String hwal = request.getParameter("hwal");
		     memberDTO.setId(id); 
		     memberDTO.setHwal(hwal);
		     memberDAO.disable(memberDTO);
		     response.sendRedirect("memberList.project");
		    }//��Ȳ��ȭ
//================ ȸ�� ��Ȱ��ȭ==================
		else if(command.equals("/membertal2.project")) {//��Ȱ��ȭ  
		     String id = request.getParameter("id");
		     String hwal = request.getParameter("hwal");
		     memberDTO.setId(id); 
		     memberDTO.setHwal(hwal);
		     memberDAO.able(memberDTO);
		     response.sendRedirect("memberList.project");
		    }//��Ȱ��ȭ
//================ �����ڱ��� ȸ�� Ż��==================
		else if(command.equals("/delete.project")) {//�����ڱ��� ȸ��Ż��
		     String id = request.getParameter("id");
		     memberDTO.setId(id);
		     memberDAO.delete(memberDTO);
		     response.sendRedirect("memberList.project");
		    }
/*================ ȸ��Ż��==================*/
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
/*================ ȸ������==================*/
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
		
//================ �α���==================
//		else if (command.equals("/loginform.project")) {//�α���
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
//				System.out.println("����");
//			}else {
//				System.out.println("����");
//			}
//			response.sendRedirect("template.jsp");
//		}//�α���
		
//		else if (command.equals("/idcheck.member")) {
//		// out.print("���̵� �ߺ�üũ");
//		String id = request.getParameter("id");
//		memberDTO.setId(id);
//		boolean check = memberDAO.memberIdCheck(memberDTO);
//		if (check) {
//			out.print(id + "�� �̹� �����ϴ� ���̵��Դϴ�.");
//		} else {
//			out.print(id + "�� ���԰����մϴ�.");
//		}
//		out.print("<input type='button' value='����' onClick='self.close()'>");
//		// System.out.println(id);
//	} else if (command.equals("/findId.member")) {
//		String name = request.getParameter("name");
//		String email = request.getParameter("email");
//
//		memberDTO.setIrum(name);
//		memberDTO.setEmail(email);
//		String findId = memberDAO.memberFindId(memberDTO);
//		if (findId.equals("")) {
//			out.print("��ġ�ϴ� ������ �����ϴ�.");
//		} else {
//			out.print("���̵�� '" + findId + "'�Դϴ�.");
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
//			out.print("��ġ�ϴ� ������ �����ϴ�.");
//		} else {
//			out.print("��й�ȣ�� '" + findPw + "'�Դϴ�.");
//		}
//		out.print("<input type='button' value='����' onClick='self.close()'>");
//	}
		
	}
}