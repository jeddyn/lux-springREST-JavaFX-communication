package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import pl.model.CourseModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class JSonService {

    //need refactor to List<T>
    public static List<CourseModel> getListOfModels() throws IOException {
        URL url = new URL("http://localhost:5050/test/course");//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        String userCredentials = "user:password";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        conn.setRequestProperty("Authorization", basicAuth);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;

        //parse to list of objects <CourseModel>, create service coursemodel, extract
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        output = br.readLine();
        System.out.println("Pobrany JSon: "+output);
        List<CourseModel> listOfCurseModel = mapper.readValue(output, new TypeReference<List<CourseModel>>(){});
        conn.disconnect();
        return listOfCurseModel;
    }

    public static HttpURLConnection delete(long id) throws IOException {

        URL url = new URL("http://localhost:5050/test/course/"+id);//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");
        String userCredentials = "user:password";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        conn.setRequestProperty("Authorization", basicAuth);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        return conn;
    }

    public static void addParsedJsonObject(CourseModel courseModel, HttpURLConnection conn) throws IOException {
        JSONObject jsonObject1 = new JSONObject();
        //jsonObject1.put("title", courseModel.getTitle());
        jsonObject1.put("review", courseModel.getReview()); //dodaje tylko z opiniami 1
        String input = jsonObject1.toJSONString();
        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        conn.disconnect();
    }

    public static HttpURLConnection httpConnectToREST(String urlConn, String methodType, String requestProperty) throws IOException {
        URL url = new URL(urlConn);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String userCredentials = "user:password";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        conn.setRequestProperty(requestProperty, basicAuth);
        conn.setDoOutput(true);
        conn.setRequestMethod(methodType);
        conn.setRequestProperty("Content-Type", "application/json");
        return conn;
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
//        System.out.println("TYP ZWRACANY!: " + resultList);
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
//    }







}
