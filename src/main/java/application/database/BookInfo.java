package application.database;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the book_info database table.
 *
 */
@Entity
@Table(name="book_info")
@NamedQuery(name="BookInfo.findAll", query="SELECT b FROM BookInfo b")
public class BookInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="book_id")
	private int bookId;

	@Temporal(TemporalType.DATE)
	@Column(name="book_in")
	private Date bookIn;

	@Temporal(TemporalType.DATE)
	@Column(name="book_out")
	private Date bookOut;

	//bi-directional many-to-one association to Apartment
	@ManyToOne
	@JoinColumn(name="apartment")
	private Apartment apartment;

	//bi-directional many-to-one association to Login

	@ManyToOne
	private Login login;
	//bi-directional many-to-one association to HostReview

	@ManyToOne
	@JoinColumn(name="host_review_id")
	private HostReview hostReview;
	//bi-directional many-to-one association to BookReview

	@OneToMany(mappedBy="bookInfo")
	private List<BookReview> bookReviews;
	public BookInfo() {
	}

	public int getBookId() {
		return this.bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public Date getBookIn() {
		return this.bookIn;
	}

	public void setBookIn(Date bookIn) {
		this.bookIn = bookIn;
	}

	public Date getBookOut() {
		return this.bookOut;
	}

	public void setBookOut(Date bookOut) {
		this.bookOut = bookOut;
	}

	public Apartment getApartment() {
		return this.apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment= apartment;
	}

	public HostReview getHostReview() {
		return this.hostReview;
	}

	public void setHostReview(HostReview hostReview) {
		this.hostReview = hostReview;
	}

	public List<BookReview> getBookReviews() {
		return this.bookReviews;
	}

	public void setBookReviews(List<BookReview> bookReviews) {
		this.bookReviews = bookReviews;
	}

	public BookReview addBookReview(BookReview bookReview) {
		getBookReviews().add(bookReview);
		bookReview.setBookInfo(this);

		return bookReview;
	}

	public BookReview removeBookReview(BookReview bookReview) {
		getBookReviews().remove(bookReview);
		bookReview.setBookInfo(null);

		return bookReview;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

}
