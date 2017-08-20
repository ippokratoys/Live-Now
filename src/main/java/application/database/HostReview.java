package application.database;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the host_review database table.
 *
 */
@Entity
@Table(name="host_review")
@NamedQuery(name="HostReview.findAll", query="SELECT h FROM HostReview h")
public class HostReview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="review_id")
	private int reviewId;

	//bi-directional many-to-one association to BookInfo
	@OneToMany(mappedBy="hostReview")
	private List<BookInfo> bookInfos;

	//bi-directional many-to-one association to HostInfo
	@ManyToOne
	private HostInfo hostInfo;

	public HostReview() {
	}

	public int getReviewId() {
		return this.reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
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
