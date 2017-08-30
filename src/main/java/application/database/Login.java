package application.database;

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

	private boolean enabled;

	@Column(name="is_customer")
	private boolean isCustomer;

	@Column(name="is_host")
	private boolean isHost;

	private String name;

	@Column(name="phone_num")
	private String phoneNum;

	@Column(name="photo_path")
	private String photoPath;

	private String password;

	private String surname;

	private boolean valid;

	//bi-directional many-to-one association to UserRole

	@OneToMany(mappedBy="login")
	private List<UserRole> userRoles;
	//bi-directional many-to-one association to Apartment

	@OneToMany(mappedBy="login")
	private List<Apartment> apartments;

	@OneToMany(mappedBy="login")
	private List<HostReview> hostReviews;

	@OneToMany(mappedBy="login")
	private List<Chat> chats;

	@OneToMany(mappedBy="login")
	private List<BookInfo> bookInfos;

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

	public boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getIsCustomer() {
		return this.isCustomer;
	}

	public void setIsCustomer(boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

	public boolean getIsHost() {
		return this.isHost;
	}

	public void setIsHost(boolean isHost) {
		this.isHost = isHost;
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

	public boolean getValid() {
		return this.valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public List<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<Apartment> getApartments() {
		return this.apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments= apartments;
	}

	public Apartment addApartment(Apartment apartment) {
		getApartments().add(apartment);
		apartment.setLogin(this);

		return apartment;
	}

	public Apartment removeApartment(Apartment apartment) {
		getApartments().remove(apartment);
		apartment.setLogin(null);

		return apartment;
	}

	public List<HostReview> getHostReviews() {
		return this.hostReviews;
	}

	public void setHostReviews(List<HostReview> hostReviews) {
		this.hostReviews= hostReviews;
	}

	public HostReview addHostReview(HostReview hostReview) {
		getHostReviews().add(hostReview);
		hostReview.setLogin(this);

		return hostReview;
	}

	public HostReview removeHostReview(HostReview hostReview) {
		getHostReviews().remove(hostReview);
		hostReview.setLogin(null);

		return hostReview;
	}

	public List<Chat> getChats() {
		return this.chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats= chats;
	}

	public Chat addChat(Chat chat) {
		getChats().add(chat);
		chat.setLogin(this);

		return chat;
	}

	public Chat removeChat(Chat chat) {
		getChats().remove(chat);
		chat.setLogin(null);

		return chat;
	}

	public List<BookInfo> getBookInfos() {
		return this.bookInfos;
	}

	public void setBookInfos(List<BookInfo> bookInfos) {
		this.bookInfos=bookInfos ;
	}

	public BookInfo addBookInfo(BookInfo bookInfo) {
		getBookInfos().add(bookInfo);
		bookInfo.setLogin(this);

		return bookInfo;
	}

	public BookInfo removeBookInfo(BookInfo bookInfo) {
		getBookInfos().remove(bookInfo);
		bookInfo.setLogin(null);

		return bookInfo;
	}

}
