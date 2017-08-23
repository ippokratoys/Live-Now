package application.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the image database table.
 *
 */
@Entity
@Table(name="image")
@NamedQuery(name="image.findAll", query="SELECT i FROM Image i")
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="image_id")
    private int imageId;

    @Column(name="picture_path")
    private String picturePath;


    //bi-directional many-to-one association to Login

    @ManyToOne
    @JoinColumn(name="apartment_apartment_id")
    private Apartment apartment;
    public Image() {
    }

    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

}
