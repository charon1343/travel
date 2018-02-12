package kr.co.daegu.member;

import java.io.Serializable;

public class MemberDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String pw;
	private String pwch;
	private String name;
	private String email;
	private String hwal;
	
	@Override
	public String toString() {
		return "MemberDTO [id=" + id + ", pw=" + pw + ", pwch=" + pwch + ", name=" + name + ", email=" + email
				+ ", hwal=" + hwal + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getPwch() {
		return pwch;
	}

	public void setPwch(String pwch) {
		this.pwch = pwch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHwal() {
		return hwal;
	}

	public void setHwal(String hwal) {
		this.hwal = hwal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public MemberDTO(String id, String pw, String pwch, String name, String email, String hwal) {
		super();
		this.id = id;
		this.pw = pw;
		this.pwch = pwch;
		this.name = name;
		this.email = email;
		this.hwal = hwal;
	}

	public MemberDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
