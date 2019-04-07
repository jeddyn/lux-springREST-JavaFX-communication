package pl.controller;


import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import pl.model.CourseModel;
import pl.model.ReviewModel;
import pl.service.CourseService;
import pl.service.JSonService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

@Slf4j
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

    @FXML
    private Text actionStatus;

    private CourseService courseService = new CourseService();

    private int actuallySelectedItemInColumnOfCourseModel;

    private Service<Void> backgroundThread;

    private ObservableList<CourseModel> dataOfCourses;


    public SampleController() throws IOException {
        System.out.println("Sample constructor");
    }

    @FXML
    void initialize(){
        try {
            dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
        }catch (IOException e){
            log.info("Nie załadowano modułu springREST: " + e.getMessage());
        }
        tableOfCourses.setItems(dataOfCourses);
        tableOfCourses.setEditable(true);
        titleCol.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        tableOfCourses.getSelectionModel().selectedIndexProperty().addListener(
                new RowOfCourseSelectChangeListener());
        tableOfCourses.getSelectionModel().select(0);
        onEditCourseRow();


        tableOfReviews.setEditable(true);
        reviewColum.setCellValueFactory(new PropertyValueFactory<ReviewModel, String>("comment"));
        reviewColum.setCellFactory(TextFieldTableCell.forTableColumn());
        tableOfReviews.getSelectionModel().selectedIndexProperty().addListener(
                new RowOfReviewsSelectChangeListener());
        onEditReviewRow();

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    addTextFilter(dataOfCourses, searchField, tableOfCourses);
                }
        );

        addbtn.setOnAction(new AddButtonListener());
        delbtn.setOnAction(new DeleteButtonListener());

    }

    public void onEditCourseRow() {
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

    public void onEditReviewRow() {
        reviewColum.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ReviewModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ReviewModel, String> t) {

                //get id of edited review from DB
                long idOfSelectedOpinion =
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).getId();

                ((ReviewModel) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setComment(t.getNewValue());

                //get new opinion from edited cell
                String newReview = t.getTableView().getItems().get(t.getTablePosition().getRow()).getComment();

                //create new object Review, add old id and put new review
                ReviewModel reviewModel = new ReviewModel(idOfSelectedOpinion, newReview);

                try {
                    //update review
                    JSonService.putObjectOfReview(reviewModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class RowOfCourseSelectChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number oldVal, Number newVal) {
            actionStatus.setFill(Color.BLACK);
            actuallySelectedItemInColumnOfCourseModel = newVal.intValue();

            if ((actuallySelectedItemInColumnOfCourseModel < 0) || (actuallySelectedItemInColumnOfCourseModel >= dataOfCourses.size())) {
                return;
            }
            //get reviews from selected course
            ObservableList<ReviewModel> selectedCourseReviews =
                    FXCollections.observableArrayList(dataOfCourses
                            .get(actuallySelectedItemInColumnOfCourseModel)
                            .getReview());
            //fill table with reviews
            tableOfReviews.setItems(selectedCourseReviews);
            //create course model to print which is selected
            CourseModel selectedCourse = dataOfCourses.get(actuallySelectedItemInColumnOfCourseModel);
            actionStatus.setText(selectedCourse.toString());

        }
    }

    private class RowOfReviewsSelectChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number oldVal, Number newVal) {

            int actuallySelectedItemInColumnOfReviews = newVal.intValue();
            actionStatus.setFill(Color.BLACK);

            if ((actuallySelectedItemInColumnOfReviews < 0) || (actuallySelectedItemInColumnOfReviews >= dataOfCourses.size())) {
                return;
            }
        }
    }

    private class AddButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            if (!inputTitle.getText().trim().equals("")) {
                if (!inputReview.getText().trim().equals("")) {

                    CourseModel courseModel = new CourseModel();
                    courseModel.setTitle(inputTitle.getText().trim());
                    courseModel.setComment(inputReview.getText().trim());
                    HttpURLConnection conn;

                    try {
                        conn = JSonService
                                .httpConnectToREST("http://localhost:5050/api/course",
                                        "POST",
                                        "Authorization");

                        //get course id ifExist
                        int id = courseService.getIdCourseIfExist(dataOfCourses, inputTitle.getText());
                        //if not exist return -1, create new course
                        if (id == -1) {
                            JSonService.addParsedJsonObject(courseModel, conn);
                            id = dataOfCourses.size();
                        } else {
                            //add comment to exist course title
                            dataOfCourses.get(id).setComment(inputReview.getText());
                            JSonService.putObject(dataOfCourses.get(id));
                        }

                        //reload data from REST
                        dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
                        tableOfCourses.setItems(dataOfCourses);

                        // Select the new row
                        tableOfCourses.getSelectionModel().select(id);
                        tableOfCourses.getFocusModel().focus(id);
                        tableOfCourses.requestFocus();
                        actionStatus.setText("Nowy kurs: Dodaj tytuł kursu i opinie. Zatwierdź przyciskiem <Dodaj>.");

                        //update data using REST
                        dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    actionStatus.setText("Błędne dane! Opinia nie może być pusta!");
                    actionStatus.setFill(Color.RED);
                }
            } else {
                actionStatus.setText("Błędne dane! Tytuł kursu nie może być pusty!");
                actionStatus.setFill(Color.RED);
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            int ix = tableOfCourses.getSelectionModel().getSelectedIndex();
            CourseModel courseModel = tableOfCourses.getSelectionModel().getSelectedItem();
            // Get selected row is !=null
            if (tableOfCourses.getSelectionModel().getSelectedItem() != null) {

                try {
                    //delete by course using JSON
                    JSonService.postGetDeleteHttpREST(
                            courseModel.getId(),
                            "http://localhost:5050/api/course",
                            "DELETE",
                            null);
                    //remove from local DB list
                    dataOfCourses.remove(ix);
                    //load fresh data from REST
                    dataOfCourses.addAll(JSonService.getListOfModels());
                    //set new data from REST
                    tableOfCourses.setItems
                            (FXCollections
                                    .observableArrayList
                                            (FXCollections.observableArrayList
                                                    (JSonService.getListOfModels())));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // if table size == 0
                if (tableOfCourses.getItems().size() == 0) {
                    actionStatus.setText("Brak danych w tabeli!");
                    return;
                }

                //if we delete index in table --
                if (ix != 0) {
                    ix = ix - 1;
                }
                //set focus on row ix
                tableOfCourses.getSelectionModel().select(ix);
                tableOfCourses.getFocusModel().focus(ix);
            }
        }
    }

    private <T> void addTextFilter(ObservableList<T> allData,
                                   TextField filterField, TableView<T> table) {

        final List<TableColumn<T, ?>> columns = table.getColumns();

        FilteredList<T> filteredData = new FilteredList<>(allData);
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            String text = filterField.getText();

            if (text == null || text.isEmpty()) {
                return null;
            }
            final String filterText = text.toLowerCase();

            return o -> {
                for (TableColumn<T, ?> col : columns) {
                    ObservableValue<?> observable = col.getCellObservableValue(o);
                    if (observable != null) {
                        Object value = observable.getValue();
                        if (value != null && value.toString().toLowerCase().contains(filterText)) {
                            backgroundThread = new Service<Void>() {
                                @Override
                                protected Task<Void> createTask() {
                                    try {
                                        dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
                                        tableOfCourses.refresh();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                            };
                            return true;
                        }
                    }
                }
                return false;
            };

        }, filterField.textProperty()));

        SortedList<T> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

}
