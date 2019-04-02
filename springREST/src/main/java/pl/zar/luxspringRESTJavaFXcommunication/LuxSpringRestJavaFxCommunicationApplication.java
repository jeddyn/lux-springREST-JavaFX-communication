package pl.zar.luxspringRESTJavaFXcommunication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.zar.luxspringRESTJavaFXcommunication.staticdataservis.StaticDataOfCourses;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LuxSpringRestJavaFxCommunicationApplication {

    @Autowired
    private StaticDataOfCourses staticDataOfCourses;

    @PostConstruct
    public void initialize() {
        staticDataOfCourses.addStaticResources();
    }

    public static void main(String[] args) {
        SpringApplication.run(LuxSpringRestJavaFxCommunicationApplication.class, args);

    }

}
