package application.database;

import application.database.CoustomerInfo;

import application.database.HostInfo;
import application.database.UserRole;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the login database table.
 * 
 */
@Entity
@Table(name="login")
@NamedQuery(name="Login.findAll", query="SELECT l FROM Login l")
public class Login implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private String email;

	private byte enabled;

	private byte isCustomer;

	private byte isHost;

	private String logincol;

	private String name;

	@Column(name="phone_num")
	private String phoneNum;

	@Column(name="photo_path")
	private String photoPath;

	private String surname;

	private short valid;

	//bi-directional one-to-one association to CoustomerInfo
	@OneToOne(mappedBy="login")
	private CoustomerInfo coustomerInfo;

	//bi-directional one-to-one association to HostInfo
	@OneToOne(mappedBy="login")
	private HostInfo hostInfo;

	//bi-directional many-to-one association to UserRole
	@OneToMany(mappedBy="login")
	private List<UserRole> userRoles;

	public Login() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getEnabled() {
		return this.enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}

	public byte getIsCustomer() {
		return this.isCustomer;
	}

	public void setIsCustomer(byte isCustomer) {
		this.isCustomer = isCustomer;
	}

	public byte getIsHost() {
		return this.isHost;
	}

	public void setIsHost(byte isHost) {
		this.isHost = isHost;
	}

	public String getLogincol() {
		return this.logincol;
	}

	public void setLogincol(String logincol) {
		this.logincol = logincol;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPhotoPath() {
		return this.photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public short getValid() {
		return this.valid;
	}

	public void setValid(short valid) {
		this.valid = valid;
	}

	public CoustomerInfo getCoustomerInfo() {
		return this.coustomerInfo;
	}

	public void setCoustomerInfo(CoustomerInfo coustomerInfo) {
		this.coustomerInfo = coustomerInfo;
	}

	public HostInfo getHostInfo() {
		return this.hostInfo;
	}

	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}

	public List<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public UserRole addUserRole(UserRole userRole) {
		getUserRoles().add(userRole);
		userRole.setLogin(this);

		return userRole;
	}

	public UserRole removeUserRole(UserRole userRole) {
		getUserRoles().remove(userRole);
		userRole.setLogin(null);

		return userRole;
	}

}