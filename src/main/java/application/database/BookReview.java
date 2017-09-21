package application.database;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the book_review database table.
 *
 */
@Entity
@Table(name="book_review")
@NamedQuery(name="BookReview.findAll", query="SELECT b FROM BookReview b")
public class BookReview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="review_id")
	private int reviewId;

	private String comment;

	private double rating;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	//bi-directional many-to-one association to BookInfo
	@ManyToOne
	@JoinColumn(name="book_id")
	private BookInfo bookInfo;

	//bi-directional many-to-one association to Apartment
	@ManyToOne
	@JoinColumn(name="apartment_id")
	private Apartment apartment;

	public BookReview() {
	}

	public int getReviewId() {
		return this.reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getRating() {
		return this.rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public BookInfo getBookInfo() {
		return this.bookInfo;
	}

	public void setBookInfo(BookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}

	public Apartment getApartment() {
		return this.apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

}
