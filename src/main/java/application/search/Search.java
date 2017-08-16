package application.search;

import java.util.Date;

/**
 * Created by thanasis on 16/8/2017.
 */
public class Search {
//    this 4 must be filed
    private Date fromDate=null;
    private Date toDate=null;

    public Search() {
    }

    public Search(Date fromDate, Date toDate, String city, Integer numberOfPerson) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.city = city;
        this.numberOfPerson = numberOfPerson;
    }

    public Search(Date fromDate, Date toDate, String city, Integer numberOfPerson, boolean hasWifi, boolean hasFrige, boolean hasKitchen, boolean hasTV, boolean hasParking, boolean hasElevator, boolean hasAircondition, String roomType, Integer maxCost) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.city = city;
        this.numberOfPerson = numberOfPerson;

        this.hasWifi = hasWifi;
        this.hasFrige = hasFrige;
        this.hasKitchen = hasKitchen;
        this.hasTV = hasTV;
        this.hasParking = hasParking;
        this.hasElevator = hasElevator;
        this.hasAircondition = hasAircondition;

        this.roomType = roomType;

        this.maxCost = maxCost;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(Integer numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public boolean isHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public boolean isHasFrige() {
        return hasFrige;
    }

    public void setHasFrige(boolean hasFrige) {
        this.hasFrige = hasFrige;
    }

    public boolean isHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }

    public boolean isHasTV() {
        return hasTV;
    }

    public void setHasTV(boolean hasTV) {
        this.hasTV = hasTV;
    }

    public boolean isHasParking() {
        return hasParking;
    }

    public void setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
    }

    public boolean isHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(boolean hasElevator) {
        this.hasElevator = hasElevator;
    }

    public boolean isHasAircondition() {
        return hasAircondition;
    }

    public void setHasAircondition(boolean hasAircondition) {
        this.hasAircondition = hasAircondition;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Integer maxCost) {
        this.maxCost = maxCost;
    }

    private String city=null;
    private Integer numberOfPerson =0;

//this are optional
    private boolean hasWifi=false;
    private boolean hasFrige=false;
    private boolean hasKitchen=false;
    private boolean hasTV=false;
    private boolean hasParking=false;
    private boolean hasElevator=false;
    private boolean hasAircondition=false;

    private String roomType=null;
    private Integer maxCost=0;

    private String needsAnd(String curQuery){
        if(curQuery!=""){
            return "and ";
        }else{
            return "";
        }
    }
    public String buildQuery(){
        String query="";
        if (hasWifi==true) {
            String and = needsAnd(query);
            query+= and + "hasWifi ";
        }
        if (hasFrige==true) {
            String and = needsAnd(query);
            query+= and + "hasFrige ";
        }
        if (hasKitchen==true) {
            String and = needsAnd(query);
            query+= and + "hasKitchen ";
        }
        if (hasTV==true){
            String and = needsAnd(query);
            query+= and + "hasTV ";
        }
        if (hasParking==true) {
            String and = needsAnd(query);
            query+= and + "hasParking ";
        }
        if (hasElevator==true) {
            String and = needsAnd(query);
            query+= and + "hasElevator ";
        }
        if (hasAircondition==true) {
            String and = needsAnd(query);
            query += and + "hasAircondition ";
        }
        if(roomType==null || roomType=="" ){
            String and = needsAnd(query);
            query += and + "roomType ";
        }
        query+=";";
        System.out.println(query);
        return query;
    }
}
