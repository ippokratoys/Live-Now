package recommended;

public class UserInfoRec {

    private String username;
    private int bucket;

    private int[] ratings;


    public UserInfoRec() {
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int[] getRatings() {
        return ratings;
    }

    public void setRatings(int[] ratings) {
        this.ratings = ratings;
    }

    public int getBucket() {
        return bucket;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }
}
