package application.database;

import java.io.Serializable;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the hostReview database table.
 * 
 */
@Entity
@Table(name="hostReview")
@NamedQuery(name="HostReview.findAll", query="SELECT h FROM HostReview h")
public class HostReview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int reviewID;

	//bi-directional many-to-one association to BookInfo
	@OneToMany(mappedBy="hostReview")
	private List<BookInfo> bookInfos;

	//bi-directional many-to-one association to HostInfo
	@ManyToOne
	@JoinColumn(name="HostInfo_login_username")
	private HostInfo hostInfo;

	public HostReview() {
	}

	public int getReviewID() {
		return this.reviewID;
	}

	public void setReviewID(int reviewID) {
		this.reviewID = reviewID;
	}

	public List<BookInfo> getBookInfos() {
		return this.bookInfos;
	}

	public void setBookInfos(List<BookInfo> bookInfos) {
		this.bookInfos = bookInfos;
	}

	public BookInfo addBookInfo(BookInfo bookInfo) {
		getBookInfos().add(bookInfo);
		bookInfo.setHostReview(this);

		return bookInfo;
	}

	public BookInfo removeBookInfo(BookInfo bookInfo) {
		getBookInfos().remove(bookInfo);
		bookInfo.setHostReview(null);

		return bookInfo;
	}

	public HostInfo getHostInfo() {
		return this.hostInfo;
	}

	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}

}