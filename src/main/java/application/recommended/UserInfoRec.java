package application.recommended;

import application.database.Apartment;
import application.database.BookReview;
import application.database.Login;
import info.debatty.java.lsh.LSHSuperBit;

import java.util.ArrayList;

public class UserInfoRec {

    private String username;
    boolean hasRatings=false;
    private int bucket;

    ArrayList<Double> ratings;

    public UserInfoRec(Login login){
        hasRatings=false;
        username = login.getUsername();
        ratings = new ArrayList<Double>();
    }

    public UserInfoRec(Login login,int numOfApartments){
        hasRatings=false;
        username = login.getUsername();
        ratings = new ArrayList<Double>(numOfApartments);
        for(int i=0;i<numOfApartments;i++)ratings.add(0.0);
    }

    public UserInfoRec() {
        hasRatings=false;
    }

    public boolean isHasRatings() {
        return hasRatings;
    }

    public void setHasRatings(boolean hasRatings) {
        this.hasRatings = hasRatings;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBucket() {
        return bucket;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }

    public void updateApartment(Apartment apartment,int index){
        for (BookReview oneReview :apartment.getBookReviews()) {
            if (oneReview.getBookInfo().getLogin().getUsername().equals(username)) {
                hasRatings=true;
                ratings.set(index,oneReview.getRating());
            }
        }
    }

    public void updateBucket(LSHSuperBit lshSuperBit){
        if(hasRatings==false){
            //if he has ratings the he can't
            return;
        }
        //get the array
        int arraySize = ratings.size();
        double[] ratingsToArray=new double[arraySize];
        for (int i = 0; i < arraySize; i++) {
            ratingsToArray[i]=ratings.get(i);
        }
//        System.out.println("-----------------");
//        for (int i = 0; i < ratingsToArray.length; i++) {
//            System.out.print(ratingsToArray[i]+"|");
//        }
//        System.out.println("-----------------");

        int[] hash=lshSuperBit.hash(ratingsToArray);

//        int[] result = lshSuperBit.hash(ratingsToArray);
//        System.out.println("-----------------");
//        for (int i = 0; i < result.length; i++) {
//            System.out.print(result[i]);
//        }
//        System.out.println("-----------------");

        this.bucket=hash[0];
    }

    public String ratingsToStr(){
        return ratings.toString();
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Double> ratings) {
        this.ratings = ratings;
    }

    public void firstRating() {
        this.hasRatings=true;
        //set all the ratings as zero
        for (int i = 0; i < ratings.size(); i++) {
            ratings.set(i,0.0);
        }
        //so it's sure different with the previous one
        bucket = -1;
    }
}
