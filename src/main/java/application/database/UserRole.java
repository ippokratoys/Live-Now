package application.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the userRole database table.
 * 
 */
@Entity
@Table(name="userRole")
@NamedQuery(name="UserRole.findAll", query="SELECT u FROM UserRole u")
public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int roleID;

	private String role;

	//bi-directional many-to-one association to Login
	@ManyToOne
	@JoinColumn(name="username")
	private Login login;

	public UserRole() {
	}

	public int getRoleID() {
		return this.roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Login getLogin() {
		return this.login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

}