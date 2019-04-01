package pl.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class MainTestFX {
    public static void main(String[] args) throws IOException {
//        List<CourseModel> a =
//                getListFromREST(getHttpConnectionToGetCourses("http://localhost:5050/test", "GET","course"));
//        for(int i =0; i<a.size(); i++){
//            System.out.println(a.get(i));
//        }
//        System.out.println(a.getClass());

    }



//    public static <T> List<T> getListFromREST(HttpURLConnection conn) throws IOException {
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
//        //System.out.println("Pobrany JSon: "+output);
//        List<T> resultList = mapper.readValue(output, new TypeReference<List<T>>(){});
//        conn.disconnect();
//        return resultList;
//
//    }
//
//    //                                  URLConn: http://localhost:5050/test | methodType get/post/delete/put  endpoint: course/review
//    public static HttpURLConnection getHttpConnectionToGetCourses(String URLConn, String methodType, String endPoint) throws IOException {
//        URL url = new URL(URLConn+"/"+endPoint);//your url i.e fetch data from .
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod(methodType);
//        conn.setRequestProperty("Accept", "application/json");
//        String userCredentials = "user:password";
//        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
//        conn.setRequestProperty("Authorization", basicAuth);
//
//        if (conn.getResponseCode() != 200) {
//            throw new RuntimeException("Failed : HTTP Error code : "
//                    + conn.getResponseCode());
//        }
//        return conn;
    }
















//    public static List<CourseModel> jsonService() throws IOException {
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
//        List<CourseModel> listOfCurseModel = mapper.readValue(output, new TypeReference<List<CourseModel>>(){});
//        conn.disconnect();
//        return listOfCurseModel;
//    }
//}
