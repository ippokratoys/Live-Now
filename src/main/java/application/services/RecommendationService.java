package application.services;
import application.database.Apartment;
import application.database.BookReview;
import application.database.Login;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.BookReviewRepository;
import application.database.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import info.debatty.java.lsh.LSHSuperBit;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Service
public class RecommendationService {
    @Autowired
    LoginRepository loginRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    BookReviewRepository bookReviewRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    public void recommendation(String username){
        String queryStr="";
        queryStr="select * from login, user_role where user_role.role=\"customer\" and login.username=user_role.username";
        Query query=entityManager.createNativeQuery(queryStr,Login.class);
        List<Login> logins = query.getResultList();
        List<Apartment> apartments=(ArrayList) apartmentRepository.findAll();

        //Making the matrix
        int [][] matrix=new int[logins.size()][apartments.size() ];
        for(int j=0;j<apartments.size();j++){
            List<BookReview> bookReviewList=bookReviewRepository.findAllByApartment(apartments.get(j));
            for(int i=0;i<logins.size();i++){
                Login login=logins.get(i);
                int rating=0;
                for(int k=0;k<bookReviewList.size();k++){
                    if(bookReviewList.get(k).getBookInfo().getLogin().getUsername().equals(login.getUsername())){
                        rating=(int)bookReviewList.get(k).getRating();
                        break;
                    }
                }
                matrix[i][j]=rating;
            }
        }

        //print
        int myMatrixPosition=-1;
        for(int i=0;i<logins.size();i++){
            if(logins.get(i).getUsername().equals(username))myMatrixPosition=i;
            for(int j=0;j<apartments.size();j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }

        //hashing
        int stages=2;
        int buckets=2;
        List<Integer> mybucket=new ArrayList<Integer>();
        int [][] hash=new int[logins.size()][];
        LSHSuperBit lsh = new LSHSuperBit(stages, buckets, apartments.size());
        for (int i = 0; i < logins.size(); i++) {
            int[] vector = matrix[i];
            hash[i] = lsh.hash(vector);
        }
        System.out.println();
        for(int i=0 ;i<logins.size();i++){
            System.out.println(hash[i]);
           if(i!=myMatrixPosition && hash[i][0]==hash[myMatrixPosition][0])mybucket.add(i);
        }
        for(int i=0;i<mybucket.size();i++){

        }
    }
}
