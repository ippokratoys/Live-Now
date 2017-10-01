package application;

import application.database.Apartment;
import application.database.Login;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.LoginRepository;
import info.debatty.java.lsh.LSHSuperBit;
import info.debatty.java.lsh.SuperBit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recommended.UserInfoRec;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Component
public class Recommendation {
    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    ArrayList<UserInfoRec> allUsersInfo;

    ArrayList<Integer> apartmentsIdArray;

    List<List<UserInfoRec>> usersInBuckets;

    LSHSuperBit lsh=null;

    SuperBit sb=null;

    int stages=2;

    int buckets=2;

    int numberOfApartments;

    @Autowired
    private EntityManager entityManager;

    public void initRec(){
        usersInBuckets= new ArrayList<List<UserInfoRec>>(buckets);
        for (int i = 0; i < buckets; i++) {
            usersInBuckets.add(new ArrayList<UserInfoRec>());
        }
        //take all the apartments
        ArrayList<Apartment> allApartments = (ArrayList<Apartment>) apartmentRepository.findAllByOrderByApartmentId();
        numberOfApartments=allApartments.size();
        //create the lsh
        lsh=new LSHSuperBit(stages, buckets,numberOfApartments);
        //create a list for the users
        allUsersInfo =new ArrayList<UserInfoRec>();
        //init the array with the users and the rating as empty
        String queryStr="select * from login, user_role where user_role.role=\"customer\" and login.username=user_role.username ORDER BY login.username";
        Query query=entityManager.createNativeQuery(queryStr,Login.class);
        ArrayList<Login> allLogins = (ArrayList<Login>) query.getResultList();

        for (Login oneLogin: allLogins) {
            UserInfoRec newInfo = new UserInfoRec(oneLogin,allApartments.size());
            allUsersInfo.add(newInfo);
        }

//init the apartments array
        int i=0;
        apartmentsIdArray=new ArrayList<Integer>();
        for (Apartment oneApartment : allApartments) {
            apartmentsIdArray.add(oneApartment.getApartmentId());
            for (UserInfoRec oneInfo : allUsersInfo) {
                oneInfo.updateApartment(oneApartment,i);
            }
            i++;
        }
        for(UserInfoRec oneInfo:allUsersInfo){
            int curBucket;
            oneInfo.updateBucket(lsh);
            curBucket=oneInfo.getBucket();
            usersInBuckets.get(curBucket).add(oneInfo);
//            System.out.println(oneInfo.getBucket());
        }
        sb=new SuperBit(numberOfApartments);
    }

    int findUser(String username){
        int high=allUsersInfo.size()-1;
        int low=0;

         while(high >= low) {
             int middle = (low + high) / 2;
//             a , a
             if(allUsersInfo.get(middle).getUsername().equals(username)) {
                 return middle;
             }
//             a , b
             if(allUsersInfo.get(middle).getUsername().compareTo(username)<0) {
                 low = middle + 1;
             }
//             b , a
             if(allUsersInfo.get(middle).getUsername().compareTo(username)>0) {
                 high = middle - 1;
             }
        }
        return -1;
    }

    public List<Apartment> getRec(String username){
        int index=findUser(username);
        List<Apartment> apartments=new ArrayList<Apartment>();
        if(index<0){
            System.out.println("No user found with name "+username);
            return null;
        }
        UserInfoRec myUserInfo = allUsersInfo.get(index);
//        System.out.println(index);
//        System.out.println(myUserInfo.getUsername());

        int curUserBucket=myUserInfo.getBucket();
        List<UserInfoRec> sameUsers=usersInBuckets.get(curUserBucket);
        System.out.println(myUserInfo.getUsername()+"{"+myUserInfo.getBucket()+"}");

        Comparator<Map<String,Object>> doubleComparator = new Comparator<Map<String,Object>>() {
            @Override
            public int compare(Map<String,Object> o1, Map<String,Object> o2){
                Double cos1 = (Double) o1.get("rating");
                Double cos2 = (Double) o2.get("rating");
                if(cos1<cos2){
                    return -1;
                }else if(cos1>cos2){
                    return 1;
                }else{
                    return 0;
                }
            }
        };
        PriorityQueue<Map<String,Object>> queue = new PriorityQueue<Map<String,Object>>(10, doubleComparator);
        for (UserInfoRec aSameUser:sameUsers){
            Map<String,Object> map = new HashMap<String,Object>();
            double curCos;
            if(aSameUser==null){
                continue;
            }
//            if(myUserInfo==aSameUser){
//                continue;
//            }
            map.put("name",aSameUser);
            curCos=cosineSimilarity(myUserInfo.getRatings(),aSameUser.getRatings());
            map.put("rating",curCos);
            System.out.println(aSameUser.getUsername()+"{"+aSameUser.getBucket()+"} : "+curCos);
            queue.add(map);
        }
        while (queue.size() != 0){
            Map<String,Object> curMap =queue.remove();
            UserInfoRec curUser =(UserInfoRec) curMap.get("name");
            Double curSimil = (Double) curMap.get("rating");
            for(int i=0 ; i<curUser.getRatings().size();i++){
                if(curUser.getRatings().get(i)>=4.0 && myUserInfo.getRatings().get(i)>0){
                    apartments.add(apartmentRepository.findOne(apartmentsIdArray.get(i)));
                }
            }
//            System.out.println( curUser.getUsername() +"\t"+curSimil);
        }
        return apartments;
    }



    //cosine similarity fucntions
    public static double cosineSimilarity(final List<Double> v1, final List<Double> v2) {
        double n1=norm(v1),n2=norm(v2);
        if(n1==0 || n2==0){
            return 0.0;
        }
        return dotProduct(v1, v2) / (n1 * n2);
    }

    private static Double norm(final List<Double> v) {
        double agg = 0.0;

        for (int i = 0; i < v.size(); i++) {
            agg += (v.get(i) * v.get(i));
        }

        return Math.sqrt(agg);
    }

    private static double dotProduct(final List<Double> v1, final List<Double> v2) {
        double agg = 0;

        for (int i = 0; i < v1.size(); i++) {
            agg += (v1.get(i) * v2.get(i));
        }

        return agg;
    }

    public void addApartment(int apartmentId){
        numberOfApartments++;
        apartmentsIdArray.add(apartmentId);
        for(UserInfoRec userInfoRec:allUsersInfo) userInfoRec.getRatings().add(0.0);
    }

    public void addCustomer(String username){
        UserInfoRec newUserInfoRec=new UserInfoRec(loginRepository.findOne(username),numberOfApartments);
        int curBucket;
        newUserInfoRec.updateBucket(lsh);
        System.out.println("aaa");
        int newPosition=-1;
        for(int i=0 ;i<allUsersInfo.size();i++){
            if(allUsersInfo.get(i).getUsername().compareTo(username)>0){
                newPosition=i;
                break;
            }
        }
        System.out.println("aaa");
        if(newPosition==-1){
            allUsersInfo.add(newUserInfoRec);
        }else{
            allUsersInfo.add(newPosition,newUserInfoRec);
        }
        curBucket=newUserInfoRec.getBucket();
        usersInBuckets.get(curBucket).add(newUserInfoRec);
    }

    public void addReview(Double rating,String username,int apartmentId){
        int index=findApartment(apartmentId);
        int position=findUser(username);
        System.out.println(index+"  "+position);
        allUsersInfo.get(position).getRatings().set(index,rating);
        int oldBucket=allUsersInfo.get(position).getBucket();
        allUsersInfo.get(position).updateBucket(lsh);
        int newBucket=allUsersInfo.get(position).getBucket();
        if(oldBucket!=newBucket){
            usersInBuckets.get(oldBucket).remove(allUsersInfo.get(position));
            usersInBuckets.get(newBucket).add(allUsersInfo.get(position));
        }
    }

    private int findApartment(int apartmentId){
        int high=numberOfApartments-1;
        int low=0;
        while(high >= low) {
            int middle = (low + high) / 2;
//             a , a
            if(apartmentsIdArray.get(middle)==apartmentId) {
                return middle;
            }
//             a , b
            if(apartmentsIdArray.get(middle)<apartmentId) {
                low = middle + 1;
            }
//             b , a
            if(apartmentsIdArray.get(middle)>apartmentId) {
                high = middle - 1;
            }
        }
        return -1;
    }
}
