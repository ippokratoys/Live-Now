package application.database;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the customer_info database table.
 *
 */
@Entity
@Table(name="customer_info")
@NamedQuery(name="CustomerInfo.findAll", query="SELECT c FROM CustomerInfo c")
public class CustomerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="login_username")
	private String loginUsername;

	//bi-directional many-to-one association to BookInfo
	@OneToMany(mappedBy="customerInfo")
	private List<BookInfo> bookInfos;

	//bi-directional many-to-one association to Chat
	@OneToMany(mappedBy="customerInfo")
	private List<Chat> chats;

	//bi-directional one-to-one association to Login
	@JoinColumn(name="login_username", insertable=false, updatable=false)
	@OneToOne
	@JoinColumn(name="Login_email", insertable=false, updatable=false)
	private Login login;

	public CustomerInfo() {
	}

	public String getLoginUsername() {
		return this.loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public List<BookInfo> getBookInfos() {
		return this.bookInfos;
	}

	public void setBookInfos(List<BookInfo> bookInfos) {
		this.bookInfos = bookInfos;
	}

	public BookInfo addBookInfo(BookInfo bookInfo) {
		getBookInfos().add(bookInfo);
		bookInfo.setCustomerInfo(this);

		return bookInfo;
	}

	public BookInfo removeBookInfo(BookInfo bookInfo) {
		getBookInfos().remove(bookInfo);
		bookInfo.setCustomerInfo(null);

		return bookInfo;
	}

	public List<Chat> getChats() {
		return this.chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public Chat addChat(Chat chat) {
		getChats().add(chat);
		chat.setCustomerInfo(this);

		return chat;
	}

	public Chat removeChat(Chat chat) {
		getChats().remove(chat);
		chat.setCustomerInfo(null);

		return chat;
	}

	public Login getLogin() {
		return this.login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

}
