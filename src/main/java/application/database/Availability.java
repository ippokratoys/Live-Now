package application.database;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * The persistent class for the user_role database table.
 *
 */
@Entity
@Table(name="availability")
@NamedQuery(name="Availability.findAll", query="SELECT a FROM Availability a")
public class Availability implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="availability_id")
    private int availabilityId;

    @Column(name="from_av")
    @Temporal(TemporalType.DATE)
    private Date fromAv;

    @Column(name="to_av")
    @Temporal(TemporalType.DATE)
    private Date toAv;

    @ManyToOne
    @JoinColumn(name="apartment_apartment_id")
    private Apartment apartment;

    public Availability() {
    }

    public int getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(int availabilityId) {
        this.availabilityId = availabilityId;
    }


    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public Date getFromAv() {
        return fromAv;
    }

    public void setFromAv(Date fromAv) {
        this.fromAv = fromAv;
    }

    public Date getToAv() {
        return toAv;
    }

    public void setToAv(Date toAv) {
        this.toAv = toAv;
    }

}
