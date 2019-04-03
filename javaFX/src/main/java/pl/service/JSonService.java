package pl.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import pl.model.CourseModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class JSonService {

    //need refactor to List<T> t
    public static List<CourseModel> getListOfModels() throws IOException {
        URL url = new URL("http://localhost:5050/api/course");//your url i.e fetch data from .
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
        List<CourseModel> listOfCurseModel = mapper.readValue(output, new TypeReference<List<CourseModel>>() {
        });
        conn.disconnect();
        return listOfCurseModel;
    }

    public static HttpURLConnection postGetDeleteHttpREST(Long id, String urlConn, String methodRequest, CourseModel courseModel) throws IOException {
        URL url;
        if (id != null) {
            url = new URL(urlConn + "/" + id);
        } else {
            url = new URL(urlConn);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(methodRequest);
        conn.setRequestProperty("Accept", "application/json");
        String userCredentials = "user:password";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        conn.setRequestProperty("Authorization", basicAuth);

        if (methodRequest.equals("POST")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            conn.disconnect();
        }

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        return conn;
    }

    public static void addParsedJsonObject(CourseModel courseModel, HttpURLConnection conn) throws IOException {
        Gson gson = new Gson();
        String input = gson.toJson(courseModel);
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


    public static void putObject(CourseModel courseModel) throws IOException {

        URL url = new URL("http://localhost:5050/api/course");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String userCredentials = "user:password";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        conn.setRequestProperty("Authorization", basicAuth);
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        Gson gson = new Gson();
        String input = gson.toJson(courseModel);
        System.out.println("GSON: " + input);
        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        conn.disconnect();
    }


}
