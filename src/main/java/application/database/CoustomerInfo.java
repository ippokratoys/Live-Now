package application.database;

import application.database.Login;


import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CoustomerInfo database table.
 * 
 */
@Entity
@NamedQuery(name="CoustomerInfo.findAll", query="SELECT c FROM CoustomerInfo c")
public class CoustomerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="login_username")
	private String loginUsername;

	//bi-directional one-to-one association to Login
	@OneToOne
	private Login login;

	//bi-directional many-to-one association to BookInfo
	@OneToMany(mappedBy="coustomerInfo")
	private List<BookInfo> bookInfos;

	public CoustomerInfo() {
	}

	public String getLoginUsername() {
		return this.loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public Login getLogin() {
		return this.login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public List<BookInfo> getBookInfos() {
		return this.bookInfos;
	}

	public void setBookInfos(List<BookInfo> bookInfos) {
		this.bookInfos = bookInfos;
	}

	public BookInfo addBookInfo(BookInfo bookInfo) {
		getBookInfos().add(bookInfo);
		bookInfo.setCoustomerInfo(this);

		return bookInfo;
	}

	public BookInfo removeBookInfo(BookInfo bookInfo) {
		getBookInfos().remove(bookInfo);
		bookInfo.setCoustomerInfo(null);

		return bookInfo;
	}

}