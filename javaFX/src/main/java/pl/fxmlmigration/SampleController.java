package pl.fxmlmigration;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import pl.model.CourseModel;
import pl.model.ReviewModel;
import pl.service.JSonService;

import java.io.IOException;

public class SampleController {

    @FXML
    private TableView<CourseModel> tableOfCourses;

    @FXML
    private TableColumn<CourseModel, String> titleCol;

    @FXML
    private TableView<ReviewModel> tableOfReviews;

    @FXML
    private TableColumn<ReviewModel, String> reviewColum;

    @FXML
    private TextField searchField;

    @FXML
    private Button delbtn;

    @FXML
    private Button addbtn;

    @FXML
    private TextField inputTitle;

    @FXML
    private TextField inputReview;

    private ObservableList<CourseModel> dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());


    public SampleController() throws IOException {
        System.out.println("Sample constructor");
    }

    @FXML
    void initialize() {

        tableOfCourses.setItems(dataOfCourses);
        tableOfCourses.setEditable(true);
        titleCol.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CourseModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CourseModel, String> t) {

                ((CourseModel) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setTitle(t.getNewValue());

                CourseModel editedCourse = t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    JSonService.putObject(editedCourse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

    }


}
