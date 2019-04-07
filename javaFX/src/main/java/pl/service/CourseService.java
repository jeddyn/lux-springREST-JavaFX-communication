package pl.service;

import pl.model.CourseModel;

import java.util.List;

public class CourseService {

    public int getIdCourseIfExist(List<CourseModel> courses, String title){
        int id = -1;

        for(int i = 0; i<courses.size(); i++){
            if(courses.get(i).getTitle().equals(title)){
                id = i;
                break;
            }
        }
        return id;
    }
}
