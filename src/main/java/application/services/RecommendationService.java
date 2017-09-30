package application.services;
import application.database.Apartment;
import application.database.BookReview;
import application.database.Login;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.BookReviewRepository;
import application.database.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for(int i=0;i<logins.size();i++){
            for(int j=0;j<apartments.size();j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }
}
