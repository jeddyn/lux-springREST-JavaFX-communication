package pl.zar.luxspringRESTJavaFXcommunication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.zar.luxspringRESTJavaFXcommunication.entities.Course;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class MainTest {
    public static void main(String[] args) throws IOException {

//        List<Course> a = jsonService();
//        for(int i =0; i<a.size(); i++){
//            System.out.println(a.get(i).getReview());
//        }
    }












//    public static List<Course> jsonService() throws IOException {
//        URL url = new URL("http://localhost:5050/test/course");//your url i.e fetch data from .
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//        String userCredentials = "user:password";
//        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
//        conn.setRequestProperty("Authorization", basicAuth);
//
//        if (conn.getResponseCode() != 200) {
//            throw new RuntimeException("Failed : HTTP Error code : "
//                    + conn.getResponseCode());
//        }
//
//        InputStreamReader in = new InputStreamReader(conn.getInputStream());
//        BufferedReader br = new BufferedReader(in);
//        String output;
//
//        //parse to list of objects <CourseModel>, create service coursemodel, extract
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
//        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//
//        output = br.readLine();
//        System.out.println("Pobrany JSon: "+output);
//        List<Course> listOfCurseModel = mapper.readValue(output, new TypeReference<List<Course>>(){});
//        conn.disconnect();
//        return listOfCurseModel;
//    }
}
