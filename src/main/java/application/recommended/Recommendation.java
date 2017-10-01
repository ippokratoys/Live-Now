package application.recommended;

import application.database.Apartment;
import application.database.Login;
import application.database.UserVector;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.LoginRepository;
import application.database.repositories.UserVectorRepositorie;
import application.search.Search;
import info.debatty.java.lsh.LSHSuperBit;
import info.debatty.java.lsh.SuperBit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Component
public class Recommendation {
    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    UserVectorRepositorie userVectorRepositorie;

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

        for (Login oneLogin: allLogins){
            UserInfoRec newInfo = new UserInfoRec(oneLogin,allApartments.size());
            allUsersInfo.add(newInfo);
        }

//init the apartments array
        int i=0;
        apartmentsIdArray=new ArrayList<Integer>();
        for (Apartment oneApartment : allApartments) {
            apartmentsIdArray.add(oneApartment.getApartmentId());
            //for each apartment if he has comment to this update the value
            for (UserInfoRec oneInfo : allUsersInfo) {
                oneInfo.updateApartment(oneApartment,i);
            }
            i++;
        }
        //find the bucket
        for(UserInfoRec oneInfo:allUsersInfo){
            //only if he has rate somting
            if(oneInfo.isHasRatings()){
                int curBucket;
                oneInfo.updateBucket(lsh);
                curBucket=oneInfo.getBucket();
                usersInBuckets.get(curBucket).add(oneInfo);
    //            System.out.println(oneInfo.getBucket());
            }else{
                //if he hasn't rate don't do something
            }
        }
        sb=new SuperBit(numberOfApartments);
    }

    int findUser(String username){
        if(allUsersInfo==null)return -1;
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

    private List<Apartment> getRecFromSearch(UserInfoRec askedUser){
        if(allUsersInfo==null)return null;
        //this will be called from getRec if the user doesn't have ratings
        return null;
    }

    public List<Apartment> getRec(String username){
        if(allUsersInfo==null)return null;
        int index=findUser(username);
        List<Apartment> apartments=new ArrayList<Apartment>();
        UserInfoRec myUserInfo = allUsersInfo.get(index);
        if(index<0){
            System.out.println("No user found with name "+username);
            this.getRecFromSearch(myUserInfo);
            return null;
        }
//        System.out.println(index);
//        System.out.println(myUserInfo.getUsername());
        if(myUserInfo.isHasRatings()==false){
            //if the user don't have ratings
            System.out.println("this user does not have ratings");
        }
        int curUserBucket=myUserInfo.getBucket();
        List<UserInfoRec> sameUsers=usersInBuckets.get(curUserBucket);
        System.out.println(myUserInfo.getUsername()+"{bucket="+myUserInfo.getBucket()+"}");

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
            if(myUserInfo==aSameUser){
                continue;
            }
            map.put("name",aSameUser);
            curCos=cosineSimilarity(myUserInfo.getRatings(),aSameUser.getRatings());
            map.put("rating",curCos);
            System.out.println(aSameUser.getUsername()+"{bucket="+aSameUser.getBucket()+"} : "+curCos);
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
        for(UserInfoRec userInfoRec:allUsersInfo){
            if(userInfoRec.isHasRatings()){
                //if he has ratings
                userInfoRec.getRatings().add(0.0);
            }else{
                //if he don't have ratings
                Apartment apartment = apartmentRepository.findOne(apartmentId);
                double rate = calcRatingOfApartment(loginRepository.findOne(userInfoRec.getUsername()),apartment);
                System.out.println("New Appartment with rating "+rate+" from "+userInfoRec.getUsername());
                //if don't have ratings
            }
        }
    }

    public void addCustomer(String username){
        UserInfoRec newUserInfoRec=new UserInfoRec(loginRepository.findOne(username),numberOfApartments);
//        int curBucket;
        newUserInfoRec.updateBucket(lsh);
        System.out.println("aaa");
//find the sorted position in the array
        int newPosition=-1;
        for(int i=0 ;i<allUsersInfo.size();i++){
            if(allUsersInfo.get(i).getUsername().compareTo(username)>0){
                //stop when you find someone bigger
                newPosition=i;
                break;
            }
        }
        System.out.println("aaa");
        if(newPosition==-1){
            //if nothing found put him in the end
            allUsersInfo.add(newUserInfoRec);
        }else{
            //else in the right position
            allUsersInfo.add(newPosition,newUserInfoRec);
        }
        //don't add him in a bucket
//        curBucket=newUserInfoRec.getBucket();
//        usersInBuckets.get(curBucket).add(newUserInfoRec);
    }

    public void addReview(Double rating,String username,int apartmentId){
        int index=findApartment(apartmentId);
        int position=findUser(username);
        System.out.println(index+"  "+position);
        UserInfoRec curUser = allUsersInfo.get(position);
        if(curUser.isHasRatings()==false){
            //if it's he is first rating
            curUser.firstRating();
        }
        allUsersInfo.get(position).getRatings().set(index,rating);
        int oldBucket=allUsersInfo.get(position).getBucket();
        allUsersInfo.get(position).updateBucket(lsh);//update the bucket that should be the element
        int newBucket=allUsersInfo.get(position).getBucket();//get the new bucket value
        if(oldBucket==-1){
            //if it's the first time
            usersInBuckets.get(newBucket).add(allUsersInfo.get(position));
        }else if(oldBucket!=newBucket){
            //that means that it's NOT the first time get in a bucket
            usersInBuckets.get(oldBucket).remove(allUsersInfo.get(position));
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

    public double calcRatingOfApartment(Login login, Apartment apartment){
        double score = 0;
        List<UserVector> userVector = login.getUserVectors();
        if(userVector.size()==0){
            //if no reviews
            return 0.0;
        }

        //check the type
        int sharedRoom=userVectorRepositorie.countAllByRoomTypeAndLogin("shared_room",login);
        int privateRoom=userVectorRepositorie.countAllByRoomTypeAndLogin("private_room",login);
        int whole_apartment=userVectorRepositorie.countAllByRoomTypeAndLogin("whole_apartment",login);
        String hePreferes = "";
        if(sharedRoom>privateRoom && sharedRoom>whole_apartment){
            hePreferes="shared_room";
        }else if(privateRoom>sharedRoom && privateRoom>whole_apartment){
            hePreferes="private_room";
        }else{
            hePreferes="whole_apartment";
        }
        if(apartment.getType().equals(hePreferes)){
            score+=1.0;
        }
        System.out.println("He prefers "+hePreferes+" apartment: "+apartment.getType());
        System.out.println("score:"+score);
        //check the price if it's less
        double avgPrice = userVectorRepositorie.findAvgMaxPriceOfLogin(login);
        if( apartment.getPrice()<((int) avgPrice)+5 || avgPrice==0.0){
            score+=1.0;
        }
        System.out.println("avgPrice:"+avgPrice+" apartment:"+apartment.getPrice());
        System.out.println("score:"+score);

        //check the people
        double avgPeople = userVectorRepositorie.findAvgMaxPplOfLogin(login);
        if(apartment.getMaxPeople()<avgPeople+1 || avgPeople==0){
            score+=1.0;
        }
        System.out.println("avgPrice:"+avgPeople+" apartment:"+apartment.getMaxPeople());
        System.out.println("score:"+score);

        //check the area can give you up to 2 points
        int timesSearchedArea=userVectorRepositorie.getTimesLoginSearchedForArea(login,apartment.getCountry(),apartment.getLocality());
        System.out.println(" times Searched = "+timesSearchedArea);
        score+=Math.min( timesSearchedArea/userVector.size()*10,2);
        System.out.println("total Score : "+score+" for aprtment "+apartment.getApartmentId());
        System.out.println("--------------------------");
        return score;
    }

    public void newSearch(String username, Search search){
        int index = findUser(username);//find where is the user info
        if(index==-1){//not found
            System.out.println("smthing Is Wrong");
            return ;
        }
        UserInfoRec theUser = allUsersInfo.get(index);//get the user
        if(theUser.isHasRatings()){//if he has rate houses no need to update
            System.out.println("No need to change something");
            return;
        }

        //create new entrie and save it to database
        Login login = loginRepository.findOne(username);
        UserVector userVector = new UserVector(login,search);
        userVectorRepositorie.save(userVector);

        //update every
        if(login.getUserVectors().size()<15 || login.getUserVectors().size()%5==0) {
            //at the beggining every search is important
            //every 10 search of the user change afthe the 15 first
            System.out.println("updating vector of user " + username);
            for (int i = 0; i < apartmentsIdArray.size(); i++) {//for every apartment
                //get the apartment id and search on db
                Apartment curApartment = apartmentRepository.findOne(apartmentsIdArray.get(i));
                //calc the new rating
                double newRate=calcRatingOfApartment(login,curApartment);//calculate the new rate
                //update the table with the ratings
                theUser.ratings.set(i,newRate);//update the rating table
            }
        }
    }

}
