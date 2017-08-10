package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the HostInfo database table.
 * 
 */
@Entity
@NamedQuery(name="HostInfo.findAll", query="SELECT h FROM HostInfo h")
public class HostInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="login_username")
	private String loginUsername;

	@Column(name="host_about")
	private String hostAbout;

	private String location;

	@Column(name="response_rate")
	private BigDecimal responseRate;

	@Column(name="response_time")
	private String responseTime;

	@Column(name="super_host")
	private byte superHost;

	private byte verified;

	//bi-directional one-to-one association to Login
	@OneToOne
	private Login login;

	//bi-directional many-to-one association to Apartment
	@OneToMany(mappedBy="hostInfo")
	private List<Apartment> apartments;

	//bi-directional many-to-one association to HostReview
	@OneToMany(mappedBy="hostInfo")
	private List<HostReview> hostReviews;

	public HostInfo() {
	}

	public String getLoginUsername() {
		return this.loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getHostAbout() {
		return this.hostAbout;
	}

	public void setHostAbout(String hostAbout) {
		this.hostAbout = hostAbout;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getResponseRate() {
		return this.responseRate;
	}

	public void setResponseRate(BigDecimal responseRate) {
		this.responseRate = responseRate;
	}

	public String getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public byte getSuperHost() {
		return this.superHost;
	}

	public void setSuperHost(byte superHost) {
		this.superHost = superHost;
	}

	public byte getVerified() {
		return this.verified;
	}

	public void setVerified(byte verified) {
		this.verified = verified;
	}

	public Login getLogin() {
		return this.login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public List<Apartment> getApartments() {
		return this.apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments = apartments;
	}

	public Apartment addApartment(Apartment apartment) {
		getApartments().add(apartment);
		apartment.setHostInfo(this);

		return apartment;
	}

	public Apartment removeApartment(Apartment apartment) {
		getApartments().remove(apartment);
		apartment.setHostInfo(null);

		return apartment;
	}

	public List<HostReview> getHostReviews() {
		return this.hostReviews;
	}

	public void setHostReviews(List<HostReview> hostReviews) {
		this.hostReviews = hostReviews;
	}

	public HostReview addHostReview(HostReview hostReview) {
		getHostReviews().add(hostReview);
		hostReview.setHostInfo(this);

		return hostReview;
	}

	public HostReview removeHostReview(HostReview hostReview) {
		getHostReviews().remove(hostReview);
		hostReview.setHostInfo(null);

		return hostReview;
	}

}