package application.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the apartment database table.
 * 
 */
@Entity
@Table(name="apartment")
@NamedQuery(name="Apartment.findAll", query="SELECT a FROM Apartment a")
public class Apartment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int apartmentID;

	private byte aircondition;

	private BigDecimal area;

	private short baths;

	private short bed;

	private BigDecimal cleanprice;

	private byte events;

	private byte garage;

	private byte heat;

	@Column(name="house_description")
	private String houseDescription;

	private byte kitchen;

	private BigDecimal lat;

	@Column(name="leaving_room")
	private byte leavingRoom;

	private byte lift;

	private String location;

	private BigDecimal lon;

	private short maxPeople;

	private short minpeople;

	private String name;

	private byte parking;

	private byte pets;

	@Column(name="plus_price")
	private short plusPrice;

	@Column(name="reviews_amount")
	private int reviewsAmount;

	private short rooms;

	private String rules;

	private byte smoking;

	@Column(name="standard_people")
	private short standardPeople;

	private BigDecimal stars;

	@Column(name="trasnportation_description")
	private String trasnportationDescription;

	private byte tv;

	private String type;

	@Column(name="`wi-fi`")
	private byte wi_fi;

	//bi-directional many-to-one association to HostInfo
	@ManyToOne
	@JoinColumn(name="HostInfo_login_username")
	private HostInfo hostInfo;

	//bi-directional many-to-one association to BookInfo
	@OneToMany(mappedBy="apartmentBean")
	private List<BookInfo> bookInfos;

	//bi-directional many-to-one association to BookReview
	@OneToMany(mappedBy="apartment")
	private List<BookReview> bookReviews;

	public Apartment() {
	}

	public int getApartmentID() {
		return this.apartmentID;
	}

	public void setApartmentID(int apartmentID) {
		this.apartmentID = apartmentID;
	}

	public byte getAircondition() {
		return this.aircondition;
	}

	public void setAircondition(byte aircondition) {
		this.aircondition = aircondition;
	}

	public BigDecimal getArea() {
		return this.area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public short getBaths() {
		return this.baths;
	}

	public void setBaths(short baths) {
		this.baths = baths;
	}

	public short getBed() {
		return this.bed;
	}

	public void setBed(short bed) {
		this.bed = bed;
	}

	public BigDecimal getCleanprice() {
		return this.cleanprice;
	}

	public void setCleanprice(BigDecimal cleanprice) {
		this.cleanprice = cleanprice;
	}

	public byte getEvents() {
		return this.events;
	}

	public void setEvents(byte events) {
		this.events = events;
	}

	public byte getGarage() {
		return this.garage;
	}

	public void setGarage(byte garage) {
		this.garage = garage;
	}

	public byte getHeat() {
		return this.heat;
	}

	public void setHeat(byte heat) {
		this.heat = heat;
	}

	public String getHouseDescription() {
		return this.houseDescription;
	}

	public void setHouseDescription(String houseDescription) {
		this.houseDescription = houseDescription;
	}

	public byte getKitchen() {
		return this.kitchen;
	}

	public void setKitchen(byte kitchen) {
		this.kitchen = kitchen;
	}

	public BigDecimal getLat() {
		return this.lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public byte getLeavingRoom() {
		return this.leavingRoom;
	}

	public void setLeavingRoom(byte leavingRoom) {
		this.leavingRoom = leavingRoom;
	}

	public byte getLift() {
		return this.lift;
	}

	public void setLift(byte lift) {
		this.lift = lift;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getLon() {
		return this.lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public short getMaxPeople() {
		return this.maxPeople;
	}

	public void setMaxPeople(short maxPeople) {
		this.maxPeople = maxPeople;
	}

	public short getMinpeople() {
		return this.minpeople;
	}

	public void setMinpeople(short minpeople) {
		this.minpeople = minpeople;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getParking() {
		return this.parking;
	}

	public void setParking(byte parking) {
		this.parking = parking;
	}

	public byte getPets() {
		return this.pets;
	}

	public void setPets(byte pets) {
		this.pets = pets;
	}

	public short getPlusPrice() {
		return this.plusPrice;
	}

	public void setPlusPrice(short plusPrice) {
		this.plusPrice = plusPrice;
	}

	public int getReviewsAmount() {
		return this.reviewsAmount;
	}

	public void setReviewsAmount(int reviewsAmount) {
		this.reviewsAmount = reviewsAmount;
	}

	public short getRooms() {
		return this.rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}

	public String getRules() {
		return this.rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public byte getSmoking() {
		return this.smoking;
	}

	public void setSmoking(byte smoking) {
		this.smoking = smoking;
	}

	public short getStandardPeople() {
		return this.standardPeople;
	}

	public void setStandardPeople(short standardPeople) {
		this.standardPeople = standardPeople;
	}

	public BigDecimal getStars() {
		return this.stars;
	}

	public void setStars(BigDecimal stars) {
		this.stars = stars;
	}

	public String getTrasnportationDescription() {
		return this.trasnportationDescription;
	}

	public void setTrasnportationDescription(String trasnportationDescription) {
		this.trasnportationDescription = trasnportationDescription;
	}

	public byte getTv() {
		return this.tv;
	}

	public void setTv(byte tv) {
		this.tv = tv;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte getWi_fi() {
		return this.wi_fi;
	}

	public void setWi_fi(byte wi_fi) {
		this.wi_fi = wi_fi;
	}

	public HostInfo getHostInfo() {
		return this.hostInfo;
	}

	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}

	public List<BookInfo> getBookInfos() {
		return this.bookInfos;
	}

	public void setBookInfos(List<BookInfo> bookInfos) {
		this.bookInfos = bookInfos;
	}

	public BookInfo addBookInfo(BookInfo bookInfo) {
		getBookInfos().add(bookInfo);
		bookInfo.setApartmentBean(this);

		return bookInfo;
	}

	public BookInfo removeBookInfo(BookInfo bookInfo) {
		getBookInfos().remove(bookInfo);
		bookInfo.setApartmentBean(null);

		return bookInfo;
	}

	public List<BookReview> getBookReviews() {
		return this.bookReviews;
	}

	public void setBookReviews(List<BookReview> bookReviews) {
		this.bookReviews = bookReviews;
	}

	public BookReview addBookReview(BookReview bookReview) {
		getBookReviews().add(bookReview);
		bookReview.setApartment(this);

		return bookReview;
	}

	public BookReview removeBookReview(BookReview bookReview) {
		getBookReviews().remove(bookReview);
		bookReview.setApartment(null);

		return bookReview;
	}

}