package application;

import info.debatty.java.lsh.LSHSuperBit;
import org.springframework.stereotype.Component;
import recommended.UserInfoRec;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class Recommendation {

    private int val;

    UserInfoRec[] allUsersInfo;

    ArrayList<Integer> apartmentsIdArray;

    LSHSuperBit lsh;

    int stages=2;

    int buckets=2;

    int numberOfApartments;

    @PostConstruct
    public void init(){
//        lsh = new LSHSuperBit(stages, buckets, numberOfApartments);
        val=5;
    }
}
