package application.services;

import application.recommended.Recommendation;
import application.database.*;
import application.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApartmentService{

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    Recommendation recommendation;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FileUploadService fileUploadService;

    public Boolean createApartment(Login login, Apartment apartment, MultipartFile image1,MultipartFile image2,MultipartFile image3,MultipartFile image4) throws Exception{
        List<UserRole> listUserRole=  login.getUserRoles();
        int isHost=0;
        for(int i=0;i<listUserRole.size();i++){
            if(listUserRole.get(i).getRole().equals("host")){
                isHost=1;
                break;
            }
        }
        if(isHost==0){
            throw new Exception("User is not a Host");
        }
        apartment.setLogin(login);
        Apartment apartment1=apartmentRepository.save(apartment);
        fileUploadService.save_image(image1,apartment1);
        fileUploadService.save_image(image2,apartment1);
        fileUploadService.save_image(image3,apartment1);
        fileUploadService.save_image(image4,apartment1);
        recommendation.addApartment(apartment1.getApartmentId());
        return true;
    }



    public Boolean authentication(UserDetails userDetails, int apartmentId){

        if(userDetails==null){
            return false;
        }
        Apartment apartment = apartmentRepository.findOne(apartmentId);
        if(apartment==null){
            return false;
        }

        if(apartment.getLogin().getUsername().equals(userDetails.getUsername())){
            return true;
        }else {
            return false;
        }

    }

    public Boolean authentication(UserDetails userDetails,Apartment apartment){
        if(apartment==null){
            return false;
        }
        return authentication(userDetails,apartment.getApartmentId());
    }

    public Boolean authentication(String username,Apartment apartment){
        return true;
    }

    public Boolean authentication(String username,int apartmentId){
        return true;
    }



    public Boolean editApartment(int apartmentId,Apartment newApartment) throws Exception{
        Apartment apartment=apartmentRepository.findOne(apartmentId);
        apartment.setAircondition(newApartment.getAircondition());
        apartment.setArea(newApartment.getArea());
        apartment.setBaths(newApartment.getBaths());
        apartment.setBed(newApartment.getBed());
        apartment.setCleanPrice(newApartment.getCleanPrice());
        apartment.setEvents(newApartment.getEvents());
        apartment.setFridge(newApartment.isFridge());
        apartment.setGarage(newApartment.getGarage());
        apartment.setHeat(newApartment.getHeat());
        apartment.setHouseDescription(newApartment.getHouseDescription());
        apartment.setKitchen(newApartment.getKitchen());
        apartment.setLeavingRoom(newApartment.getLeavingRoom());
        apartment.setLift(newApartment.getLift());
        apartment.setMaxPeople(newApartment.getMaxPeople());
        apartment.setMinPeople(newApartment.getMinPeople());
        apartment.setName(newApartment.getName());
        apartment.setParking(newApartment.getParking());
        apartment.setPets(newApartment.getPets());
        apartment.setPlusPrice(newApartment.getPlusPrice());
        apartment.setPrice(newApartment.getPrice());
        apartment.setRooms(newApartment.getRooms());
        apartment.setRules(newApartment.getRules());
        apartment.setSmoking(newApartment.getSmoking());
        apartment.setStandardPeople(newApartment.getStandardPeople());
        apartment.setTrasnportationDescription(newApartment.getTrasnportationDescription());
        apartment.setTv(newApartment.getTv());
        apartment.setWiFi(newApartment.getWiFi());
        apartmentRepository.save(apartment);
        return true;
    }

    public Boolean newMessageFormHost(String message,int chatId){
        Chat chat=chatRepository.findOne(chatId);
        if(chat==null){
            System.out.println("the chat does not exist");
            return false;
        }
        Message newMessage= new Message();
        newMessage.setChat(chat);
        newMessage.setContent(message);
        Date date = new Date();
        newMessage.setDateTime(date);
        newMessage.setFromCustomer(false);
        messageRepository.save(newMessage);
        return true;
    }

    public Boolean newMessageFromUser(String message, String username, int apartmentId){
        int notCreateNew=0;
        Chat createdChat=null;
        Message newMessage=new Message();
        Apartment apartment=apartmentRepository.findOne(apartmentId);
        List<Chat> chatlist=chatRepository.findAllByApartment(apartment);
        if(username.equals(apartment.getLogin().getUsername())){
            System.out.println("You sent to your self");
            return false;
        }
        for(Chat chat : chatlist){
            if(chat.getLogin().getUsername().equals(username)){
               notCreateNew=1;
               createdChat=chat;
               break;
            }
        }
        if(notCreateNew==1){
            newMessage.setChat(createdChat);
            newMessage.setContent(message);
            Date date = new Date();
            newMessage.setDateTime(date);
            newMessage.setFromCustomer(true);
            messageRepository.save(newMessage);
        }else{
            Chat newChat=new Chat();
            newChat.setApartment(apartment);
            newChat.setLogin(loginRepository.findOne(username));
            createdChat=chatRepository.save(newChat);
            newMessage.setChat(createdChat);
            newMessage.setContent(message);
            newMessage.setFromCustomer(true);
            Date date = new Date();
            newMessage.setDateTime(date);
            messageRepository.save(newMessage);
        }
        return true;
    }

    public List<Map<String,String>> getAvailableDates(int apartmentId){
        Apartment apartment= apartmentRepository.findOne(apartmentId);
        List resultList = new ArrayList<Map<String,String>>();
        for (Availability oneAvailability :
                apartment.getAvailabilities())
        {
            Map returnMap = new HashMap(2);
            returnMap.put("from",simpleDateFormat.format(oneAvailability.getFromAv()));
            returnMap.put("to",simpleDateFormat.format(oneAvailability.getToAv()));
            resultList.add(returnMap);
        }
        return resultList;
    }

    public List<Map<String,String>> getBookedDates(int apartmentId){
        Apartment apartment= apartmentRepository.findOne(apartmentId);
        List resultList = new ArrayList<Map<String,String>>();
        for (BookInfo oneBookInfo :
                apartment.getBookInfos())
        {
            Map returnMap = new HashMap(2);
            returnMap.put("from",simpleDateFormat.format(oneBookInfo.getBookIn()));
            returnMap.put("to",simpleDateFormat.format(oneBookInfo.getBookOut()));
            resultList.add(returnMap);
        }
        return resultList;

    }

    public String createXml(int apartmentId){
        String xmlReq=null;
        Apartment apartment=apartmentRepository.findOne(apartmentId);
        Login login=apartment.getLogin();
        List<BookInfo> bookInfos=apartment.getBookInfos();
        List<HostReview> hostReviews=new ArrayList<HostReview>();
        List<BookReview> bookReviews=new ArrayList<BookReview>();


        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if(bookInfos.size()>0){
            for(BookInfo bookInfo:bookInfos){
                if(bookInfo.getBookReviews().size()>0){
                    BookReview bookReview=bookInfo.getBookReviews().get(0);
                    bookReviews.add(bookReview);
                }
                if(bookInfo.getHostReviews().size()>0){
                    HostReview hostReview=bookInfo.getHostReviews().get(0);
                    hostReviews.add(hostReview);
                }
            }
        }else{
            System.out.println("Zero books");
        }

        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Apartment");
            doc.appendChild(rootElement);


            Element apartmentInfo = doc.createElement("ApartmentInformation");
            rootElement.appendChild(apartmentInfo);

            Element apartmentBooks = doc.createElement("ApartmentBooks");
            if(bookInfos.size()==0){
                apartmentBooks.appendChild(doc.createTextNode("NONE"));
            }
            rootElement.appendChild(apartmentBooks);

            Element apartmentReviews = doc.createElement("ApartmentReviews");
            if(bookReviews.size()==0){
                apartmentReviews.appendChild(doc.createTextNode("NONE"));
            }
            rootElement.appendChild(apartmentReviews);

            Element HostReviews = doc.createElement("HostReviews");
            if(hostReviews.size()==0){
                HostReviews.appendChild(doc.createTextNode("NONE"));
            }
            rootElement.appendChild(HostReviews);

            Element id = doc.createElement("apartmentId");
            id.appendChild(doc.createTextNode(String.valueOf(apartmentId)));
            apartmentInfo.appendChild(id);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(apartment.getName()));
            apartmentInfo.appendChild(name);

            Element owner = doc.createElement("owner");
            owner.appendChild(doc.createTextNode(login.getUsername()));
            apartmentInfo.appendChild(owner);

            Element aircondition = doc.createElement("aircondition");
            aircondition.appendChild(doc.createTextNode(Boolean.toString(apartment.getAircondition())));
            apartmentInfo.appendChild(aircondition);

            Element area = doc.createElement("area");
            area.appendChild(doc.createTextNode(String.valueOf(apartment.getArea())));
            apartmentInfo.appendChild(area);

            Element baths = doc.createElement("baths");
            baths.appendChild(doc.createTextNode(String.valueOf(apartment.getBaths())));
            apartmentInfo.appendChild(baths);

            Element bed = doc.createElement("bed");
            bed.appendChild(doc.createTextNode(String.valueOf(apartment.getBed())));
            apartmentInfo.appendChild(bed);

            Element cleanPrice = doc.createElement("cleanPrice");
            cleanPrice.appendChild(doc.createTextNode(String.valueOf(apartment.getCleanPrice())));
            apartmentInfo.appendChild(cleanPrice);

            Element country = doc.createElement("country");
            country.appendChild(doc.createTextNode(apartment.getCountry()));
            apartmentInfo.appendChild(country);

            Element events = doc.createElement("events");
            events.appendChild(doc.createTextNode(Boolean.toString(apartment.getEvents())));
            apartmentInfo.appendChild(events);

            Element fridge = doc.createElement("fridge");
            fridge.appendChild(doc.createTextNode(Boolean.toString(apartment.isFridge())));
            apartmentInfo.appendChild(fridge);

            Element garage = doc.createElement("garage");
            garage.appendChild(doc.createTextNode(Boolean.toString(apartment.getGarage())));
            apartmentInfo.appendChild(garage);

            Element heat = doc.createElement("heat");
            heat.appendChild(doc.createTextNode(Boolean.toString(apartment.getHeat())));
            apartmentInfo.appendChild(heat);

            Element houseDescription = doc.createElement("houseDescription");
            houseDescription.appendChild(doc.createTextNode(apartment.getHouseDescription()));
            apartmentInfo.appendChild(houseDescription);

            Element kitchen = doc.createElement("kitchen");
            kitchen.appendChild(doc.createTextNode(Boolean.toString(apartment.getKitchen())));
            apartmentInfo.appendChild(kitchen);

            Element lat= doc.createElement("lat");
            lat.appendChild(doc.createTextNode(String.valueOf(apartment.getLat())));
            apartmentInfo.appendChild(lat);

            Element leavingRoom = doc.createElement("leavingRoom");
            leavingRoom.appendChild(doc.createTextNode(Boolean.toString(apartment.getLeavingRoom())));
            apartmentInfo.appendChild(leavingRoom);

            Element lift = doc.createElement("lift");
            lift.appendChild(doc.createTextNode(Boolean.toString(apartment.getLift())));
            apartmentInfo.appendChild(lift);

            Element locality = doc.createElement("locality");
            locality.appendChild(doc.createTextNode(apartment.getLocality()));
            apartmentInfo.appendChild(locality);

            Element lon = doc.createElement("lon");
            lon.appendChild(doc.createTextNode(String.valueOf(apartment.getLon())));
            apartmentInfo.appendChild(lon);

            Element maxPeople = doc.createElement("maxPeople");
            maxPeople .appendChild(doc.createTextNode(String.valueOf(apartment.getMaxPeople())));
            apartmentInfo.appendChild(maxPeople );

            Element minPeople = doc.createElement("minPeople");
            minPeople.appendChild(doc.createTextNode(String.valueOf(apartment.getMinPeople())));
            apartmentInfo.appendChild(minPeople);

            Element parking = doc.createElement("parking");
            parking.appendChild(doc.createTextNode(Boolean.toString(apartment.getParking())));
            apartmentInfo.appendChild(parking);

            Element pets = doc.createElement("pets");
            pets.appendChild(doc.createTextNode(Boolean.toString(apartment.getPets())));
            apartmentInfo.appendChild(pets);

            Element plusPrice = doc.createElement("plusPrice");
            plusPrice.appendChild(doc.createTextNode(String.valueOf(apartment.getPlusPrice())));
            apartmentInfo.appendChild(plusPrice);

            Element price = doc.createElement("bed");
            price.appendChild(doc.createTextNode(String.valueOf(apartment.getPrice())));
            apartmentInfo.appendChild(price);

            Element rooms = doc.createElement("rooms");
            rooms.appendChild(doc.createTextNode(String.valueOf(apartment.getRooms())));
            apartmentInfo.appendChild(rooms);

            Element route = doc.createElement("route");
            route.appendChild(doc.createTextNode(apartment.getRoute()));
            apartmentInfo.appendChild(route);

            Element rules = doc.createElement("rules");
            rules.appendChild(doc.createTextNode(apartment.getRules()));
            apartmentInfo.appendChild(rules);

            Element smoking = doc.createElement("smoking");
            smoking.appendChild(doc.createTextNode(Boolean.toString(apartment.getSmoking())));
            apartmentInfo.appendChild(smoking);

            Element standardPeople = doc.createElement("standardPeople");
            standardPeople.appendChild(doc.createTextNode(String.valueOf(apartment.getStandardPeople())));
            apartmentInfo.appendChild(standardPeople);

            Element streetNumber = doc.createElement("streetNumber");
            streetNumber.appendChild(doc.createTextNode(String.valueOf(apartment.getStreetNumber())));
            apartmentInfo.appendChild(streetNumber);

            Element transportationDescription = doc.createElement("transportationDescription");
            transportationDescription.appendChild(doc.createTextNode(apartment.getTrasnportationDescription()));
            apartmentInfo.appendChild(transportationDescription);

            Element tv = doc.createElement("tv");
            tv.appendChild(doc.createTextNode(Boolean.toString(apartment.getTv())));
            apartmentInfo.appendChild(tv);

            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode(apartment.getType()));
            apartmentInfo.appendChild(type);

            Element wiFi = doc.createElement("wiFi");
            wiFi.appendChild(doc.createTextNode(Boolean.toString(apartment.getWiFi())));
            apartmentInfo.appendChild(wiFi);

            if(bookInfos.size()>0){
                for(BookInfo bookInfo: bookInfos){
                    Element tempBookInfo = doc.createElement("BookInformation");
                    apartmentBooks.appendChild(tempBookInfo);

                    Element bookId = doc.createElement("bookId");
                    bookId.appendChild(doc.createTextNode(String.valueOf(bookInfo.getBookId())));
                    tempBookInfo.appendChild(bookId);

                    Element visitor = doc.createElement("visitor");
                    visitor.appendChild(doc.createTextNode(bookInfo.getLogin().getUsername()));
                    tempBookInfo.appendChild(visitor);

                    Element people = doc.createElement("amountOfPeople");
                    people.appendChild(doc.createTextNode(String.valueOf(bookInfo.getPeople())));
                    tempBookInfo.appendChild(people);

                    Element income = doc.createElement("income");
                    income.appendChild(doc.createTextNode(String.valueOf(bookInfo.getIncome())));
                    tempBookInfo.appendChild(income);

                    Element checkIn = doc.createElement("checkIn");
                    checkIn.appendChild(doc.createTextNode(df.format(bookInfo.getBookIn())));
                    tempBookInfo.appendChild(checkIn);

                    Element checkOut = doc.createElement("checkOut");
                    checkOut.appendChild(doc.createTextNode(df.format(bookInfo.getBookOut())));
                    tempBookInfo.appendChild(checkOut);
                }
            }

            if(hostReviews.size()>0){
                for(HostReview hostReview:hostReviews){
                    Element tempHostReview = doc.createElement("HostReviewInformation");
                    HostReviews.appendChild(tempHostReview);

                    Element reviewId = doc.createElement("hostReviewId");
                    reviewId.appendChild(doc.createTextNode(String.valueOf(hostReview.getReviewId())));
                    tempHostReview.appendChild(reviewId);

                    Element user = doc.createElement("from");
                    user.appendChild(doc.createTextNode(hostReview.getLogin().getUsername()));
                    tempHostReview.appendChild(user);

                    Element content = doc.createElement("content");
                    content.appendChild(doc.createTextNode(String.valueOf(hostReview.getContent())));
                    tempHostReview.appendChild(content);

                    Element rating = doc.createElement("rating");
                    rating.appendChild(doc.createTextNode(String.valueOf(hostReview.getRating())));
                    tempHostReview.appendChild(rating);

                    Element bookID = doc.createElement("bookId");
                    bookID.appendChild(doc.createTextNode(String.valueOf(hostReview.getBookInfo().getBookId())));
                    tempHostReview.appendChild(bookID);

                    Element date = doc.createElement("date");
                    date.appendChild(doc.createTextNode(df2.format(hostReview.getTime())));
                    tempHostReview.appendChild(date);
                }
            }

            if(bookReviews.size()>0){
                for(BookReview bookReview:bookReviews){
                    Element tempBookReview = doc.createElement("ApartmentReviewInformation");
                    apartmentReviews.appendChild(tempBookReview);

                    Element reviewId = doc.createElement("apartmentReviewId");
                    reviewId.appendChild(doc.createTextNode(String.valueOf(bookReview.getReviewId())));
                    tempBookReview.appendChild(reviewId);

                    Element content = doc.createElement("content");
                    content.appendChild(doc.createTextNode(bookReview.getComment()));
                    tempBookReview.appendChild(content);

                    Element rating = doc.createElement("rating");
                    rating.appendChild(doc.createTextNode(String.valueOf(bookReview.getRating())));
                    tempBookReview.appendChild(rating);

                    Element date = doc.createElement("date");
                    date.appendChild(doc.createTextNode(df2.format(bookReview.getTime())));
                    tempBookReview.appendChild(date);

                    Element apartmentId2 = doc.createElement("apartmentId");
                    apartmentId2.appendChild(doc.createTextNode(String.valueOf(bookReview.getApartment().getApartmentId())));
                    tempBookReview.appendChild(apartmentId2);

                    Element bookID = doc.createElement("bookId");
                    bookID.appendChild(doc.createTextNode(String.valueOf(bookReview.getBookInfo().getBookId())));
                    tempBookReview.appendChild(bookID);

                    Element from = doc.createElement("from");
                    from.appendChild(doc.createTextNode(String.valueOf(bookReview.getBookInfo().getLogin().getUsername())));
                    tempBookReview.appendChild(from);
                }
            }


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            OutputStream out = new ByteArrayOutputStream();
            StreamResult streamResult = new StreamResult();
            streamResult.setOutputStream(out);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, streamResult);
            xmlReq = streamResult.getOutputStream().toString();

            System.out.println("File saved!");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
        return xmlReq;

    }
}
