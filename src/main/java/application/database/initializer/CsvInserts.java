package application.database.initializer;


import application.database.*;
import application.database.repositories.*;
import org.apache.catalina.Host;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.io.*;
import java.math.BigDecimal;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class CsvInserts{

    public void loginCsvInsertions(String csvFile, LoginRepository loginRepository){
        String line="";
        String csvSplitBy=",";
        BufferedReader buf =null;
        try{
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
            buf.readLine();
            while((line = buf.readLine()) != null){
                String[] arr_in = line.split(csvSplitBy);
                Login login=new Login();
                login.setUsername(arr_in[0]);
                login.setEmail(arr_in[1]);
                login.setEnabled(true);
                login.setName(arr_in[3]);
                login.setPassword(arr_in[4]);
                login.setPhoneNum(arr_in[5]);
                login.setPhotoPath(arr_in[6]);
                login.setSurname(arr_in[7]);
                login.setValid(true);
                loginRepository.save(login);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (buf != null) {
                try {
                    buf.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    BookReviewRepository bookReviewRepository;

    @Autowired
    HostReviewRepository hostReviewRepository;

    @Autowired
    BookInfoRepository bookInfoRepository;

    public void loginCsvInsertions2(String csvFile,LoginRepository loginRepository){
        String line="";
        String csvSplitBy=",";
        BufferedReader buf =null;
        Calendar cal = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Apartment> apartments=(List<Apartment>) apartmentRepository.findAll();
        int amountOfApartments=apartments.size();
        try{
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
            buf.readLine();
            int counter=0;
            while((line = buf.readLine()) != null){
                if(counter==5000)break;
                String[] arr_in = line.split(csvSplitBy);
                Login oldLogin=loginRepository.findOne(arr_in[3]);
                arr_in[2]=arr_in[2].replaceAll("\"","");
                arr_in[4]=arr_in[4].replaceAll("\"","");

                if(oldLogin==null){
//                    System.out.println("Does not Exists");
                    Login login=new Login();
                    login.setUsername(arr_in[3]);
                    login.setEmail(arr_in+"@gmail.com");
                    login.setEnabled(true);
                    login.setName(arr_in[4]);
                    login.setPassword("123");
                    login.setPhoneNum("6980594039");
                    login.setPhotoPath("/UserPhotos/not");
                    login.setSurname("Plakias");
                    login.setValid(true);
                    Login newLogin=loginRepository.save(login);

                    UserRole userRole=new UserRole();
                    userRole.setLogin(newLogin);
                    userRole.setRole("customer");
                    userRoleRepository.save(userRole);
                    oldLogin=newLogin;
                }

                BookInfo bookInfo=new BookInfo();
                bookInfo.setHostReviewDone(true);
                bookInfo.setReviewDone(true);
                bookInfo.setIncome(222);
                bookInfo.setPeople(4);
                try {
                    cal.setTime(format.parse(arr_in[2]));
                    cal.add(Calendar.DAY_OF_MONTH, -3);
                    bookInfo.setBookIn(cal.getTime());
                    bookInfo.setBookOut(format.parse(arr_in[2]));
                }catch (Exception e){
                    e.printStackTrace();
                }
                int apartmentId=ThreadLocalRandom.current().nextInt(1,amountOfApartments+1);
                Apartment apartment=apartmentRepository.findOne(apartmentId);
                bookInfo.setApartment(apartment);
                bookInfo.setLogin(oldLogin);
                BookInfo newBookInfo=bookInfoRepository.save(bookInfo);

                HostReview hostReview=new HostReview();
                hostReview.setLogin(oldLogin);
                hostReview.setContent("yolo");
                hostReview.setBookInfo(newBookInfo);
                int randomNum = ThreadLocalRandom.current().nextInt(1, 5 + 1);
                hostReview.setRating(randomNum);

                BookReview bookReview=new BookReview();
                bookReview.setApartment(apartment);
                bookReview.setBookInfo(newBookInfo);
                bookReview.setComment("yolo");
                bookReview.setRating(randomNum);
                try {
                    hostReview.setTime(format.parse(arr_in[2]));
                    bookReview.setTime(format.parse(arr_in[2]));
                }catch (Exception e){
                    e.printStackTrace();
                }

                bookReviewRepository.save(bookReview);
                hostReviewRepository.save(hostReview);

//                System.out.println("Username: "+arr_in[3]+"Rating: "+randomNum);
                counter++;
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (buf != null) {
                try {
                    buf.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("finished");
    }

    public void apartmentCsvInsertions(String csvFile, ApartmentRepository apartmentRepository ,LoginRepository loginRepository){
        String line="";
        String csvSplitBy=",";
        BufferedReader buf =null;
        try{
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
            buf.readLine();
            while((line = buf.readLine()) != null){
                String[] arr_in = line.split(csvSplitBy);
                Apartment apartment=new Apartment();
                apartment.setApartmentId(Integer.parseInt(arr_in[0]));
                apartment.setAircondition(Boolean.parseBoolean(arr_in[1]));
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator(',');
                symbols.setDecimalSeparator('.');
                String pattern = "#,##0.0#";
                DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                decimalFormat.setParseBigDecimal(true);
                try{
                    apartment.setArea((BigDecimal) decimalFormat.parse(arr_in[2]));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                apartment.setBaths(Short.parseShort(arr_in[3]));
                apartment.setBed(Short.parseShort(arr_in[4]));
                apartment.setCleanPrice(Short.parseShort(arr_in[5]));
                apartment.setEvents(Boolean.parseBoolean(arr_in[6]));
                apartment.setFridge(Boolean.parseBoolean(arr_in[7]));
                apartment.setGarage(Boolean.parseBoolean(arr_in[8]));
                apartment.setHeat(Boolean.parseBoolean(arr_in[9]));
                apartment.setHouseDescription(arr_in[10]);
                apartment.setKitchen(Boolean.parseBoolean(arr_in[11]));
                try{
                    apartment.setLat((BigDecimal) decimalFormat.parse(arr_in[12]));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                apartment.setLeavingRoom(Boolean.parseBoolean(arr_in[13]));
                apartment.setLift(Boolean.parseBoolean(arr_in[14]));
                apartment.setLocality(arr_in[15]);
                try{
                    apartment.setLon((BigDecimal) decimalFormat.parse(arr_in[16]));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                apartment.setMaxPeople(Short.parseShort(arr_in[17]));
                apartment.setMinPeople(Short.parseShort(arr_in[18]));
                apartment.setName(arr_in[19]);
                apartment.setParking(Boolean.parseBoolean(arr_in[20]));
                apartment.setPets(Boolean.parseBoolean(arr_in[21]));
                apartment.setPlusPrice(Short.parseShort(arr_in[22]));
                apartment.setPrice(Short.parseShort(arr_in[23]));
                apartment.setRooms(Short.parseShort(arr_in[24]));
                apartment.setRules(arr_in[25]);
                apartment.setSmoking(Boolean.parseBoolean(arr_in[26]));
                apartment.setStandardPeople(Short.parseShort(arr_in[27]));
                apartment.setTrasnportationDescription(arr_in[28]);
                apartment.setTv(Boolean.parseBoolean(arr_in[29]));
                apartment.setType(arr_in[30]);
                apartment.setWiFi(Boolean.parseBoolean(arr_in[31]));
                apartment.setLogin(loginRepository.findOne(arr_in[32]));
                apartment.setCountry(arr_in[33]);
                apartment.setStreetNumber(arr_in[34]);
                apartment.setRoute(arr_in[35]);
                apartmentRepository.save(apartment);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (buf != null) {
                try {
                    buf.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void userRoleCsvInsertions(String csvFile, LoginRepository loginRepository, UserRoleRepository userRoleRepository){
        String line="";
        String csvSplitBy=",";
        BufferedReader buf =null;
        try{
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
            buf.readLine();
            while((line = buf.readLine()) != null){
                String[] arr_in = line.split(csvSplitBy);
                UserRole userRole=new UserRole();
                userRole.setRoleId(Integer.parseInt(arr_in[0]));
                userRole.setRole(arr_in[2]);
                userRole.setLogin(loginRepository.findOne(arr_in[1]));
                userRoleRepository.save(userRole);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (buf != null) {
                try {
                    buf.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void availabilityCsvInsertions(String csvFile, AvailabilityRepository availabilityRepository,ApartmentRepository apartmentRepository){
        String line="";
        String csvSplitBy=",";
        BufferedReader buf =null;
        try{
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
            buf.readLine();
            while((line = buf.readLine()) != null){
                String[] arr_in = line.split(csvSplitBy);
                Availability availability=new Availability();
                availability.setAvailabilityId(Integer.parseInt(arr_in[0]));
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                try{
                    availability.setFromAv(format.parse(arr_in[1]));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                try{
                    availability.setToAv(format.parse(arr_in[2]));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                availability.setApartment(apartmentRepository.findOne(Integer.parseInt(arr_in[3])));
                availabilityRepository.save(availability);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (buf != null) {
                try {
                    buf.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void bookInfoCsvInsertions(String csvFile, BookInfoRepository bookInfoRepository, ApartmentRepository apartmentRepository ,LoginRepository loginRepository){
        String line="";
        String csvSplitBy=",";
        BufferedReader buf =null;
        try{
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
            buf.readLine();
            while((line = buf.readLine()) != null){
                String[] arr_in = line.split(csvSplitBy);
                BookInfo bookInfo=new BookInfo();
                bookInfo.setBookId(Integer.parseInt(arr_in[0]));
                bookInfo.setApartment(apartmentRepository.findOne(Integer.parseInt(arr_in[1])));
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                try{
                    bookInfo.setBookIn(format.parse(arr_in[2]));
                }catch (ParseException e) {
                    e.printStackTrace();
                }
                try{
                    bookInfo.setBookOut(format.parse(arr_in[3]));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                bookInfo.setLogin(loginRepository.findOne(arr_in[5]));
                bookInfo.setReviewDone(Boolean.parseBoolean(arr_in[6]));
                bookInfo.setReviewDone(Boolean.parseBoolean(arr_in[9]));
                bookInfoRepository.save(bookInfo);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (buf != null) {
                try {
                    buf.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}