package application.database;

import application.search.Search;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by thanasis on 1/10/2017.
 */

@Entity
public class UserVector implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    int searchId;

    @ManyToOne
    Login login;

    String searchString1;

    String searchString2;

    int people;

    String roomType;

    double price;

    public UserVector() {
    }

    public UserVector(Login login, Search search) {
        this.login = login;
        this.price=search.getMaxCost();
        this.people=search.getNumberOfPerson();
        this.roomType=search.getRoomType();
        String[] parts = search.getCity().split(" ");
        searchString1 = parts[0];
        if(parts.length>1){
            searchString2=parts[1];
        }

    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getSearchString1() {
        return searchString1;
    }

    public void setSearchString1(String searchString1) {
        this.searchString1 = searchString1;
    }

    public String getSearchString2() {
        return searchString2;
    }

    public void setSearchString2(String searchString2) {
        this.searchString2 = searchString2;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
