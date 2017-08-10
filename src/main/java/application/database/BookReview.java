package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the bookReview database table.
 * 
 */
@Entity
@Table(name="bookReview")
@NamedQuery(name="BookReview.findAll", query="SELECT b FROM BookReview b")
public class BookReview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int reviewID;

	private String comment;

	private short rating;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	//bi-directional many-to-one association to BookInfo
	@ManyToOne
	@JoinColumn(name="bookId")
	private BookInfo bookInfo;

	//bi-directional many-to-one association to Apartment
	@ManyToOne
	private Apartment apartment;

	public BookReview() {
	}

	public int getReviewID() {
		return this.reviewID;
	}

	public void setReviewID(int reviewID) {
		this.reviewID = reviewID;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public short getRating() {
		return this.rating;
	}

	public void setRating(short rating) {
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