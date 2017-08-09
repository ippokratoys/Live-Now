package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the admin_login database table.
 * 
 */
@Entity
@Table(name="admin_login")
@NamedQuery(name="AdminLogin.findAll", query="SELECT a FROM AdminLogin a")
public class AdminLogin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private String pwd;

	public AdminLogin() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}