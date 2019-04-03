import javafx.application.Application;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.model.CourseModel;
import pl.model.ReviewModel;
import pl.service.JSonService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;


public class MainFX extends Application {

    private Service<Void> backgroundThread;

    private TableView<CourseModel> tableOfCourses;
    private TableView<ReviewModel> tableOfReviews;

    private ObservableList<CourseModel> dataOfCourses;

    //print action in bottom
    private Text actionStatus;
    //input to add Title
    private TextField inputTitle = new TextField();
    //input to add Review
    private TextField inputReview = new TextField();
    //search in DB textField
    private TextField searchField = new TextField();





    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Projekt");

        Label topLabel = new Label("Kursy");
        topLabel.setTextFill(Color.DARKBLUE);
        topLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().addAll(searchField, topLabel);

        //create new view
        tableOfCourses = new TableView<>();
        //get data from JSON
        dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
        //set items to table of courses
        tableOfCourses.setItems(dataOfCourses);
        //set as editable
        tableOfCourses.setEditable(true);

        TableColumn titleCol = new TableColumn("Tytuł kursu");
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

        tableOfReviews = new TableView<>();

        //tableOfReviews.setEditable(true);

        TableColumn reviewColum = new TableColumn("Opinie");
        reviewColum.setCellValueFactory(new PropertyValueFactory<ReviewModel, String>("comment"));
        reviewColum.setCellFactory(TextFieldTableCell.forTableColumn());
        reviewColum.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ReviewModel, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ReviewModel, String> t) {
                ((ReviewModel) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setComment(t.getNewValue());
            }
        });

        tableOfReviews.getColumns().setAll(reviewColum);
        tableOfReviews.setPrefWidth(450);
        tableOfReviews.setPrefHeight(300);
        tableOfReviews.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfReviews.getSelectionModel().selectedIndexProperty().addListener(
                new RowSelectChangeListener());

        tableOfCourses.setPrefWidth(450);
        tableOfCourses.setPrefHeight(300);
        tableOfCourses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfCourses.getColumns().setAll(titleCol);
        tableOfCourses.getSelectionModel().selectedIndexProperty().addListener(
                new RowSelectChangeListener());


        final Text titleTxfield = new Text("Tytuł:");
        final Text reviewTxfield = new Text("Opinia");
        final Text searchTxfield = new Text("Szukaj: ");

        // Add and delete buttons
        Button addbtn = new Button("Dodaj");
        addbtn.setOnAction(new AddButtonListener());
        Button delbtn = new Button("Usuń");
        delbtn.setOnAction(new DeleteButtonListener());
        HBox buttonHb = new HBox(10);
        buttonHb.setAlignment(Pos.CENTER);
        buttonHb.getChildren().addAll(titleTxfield, this.inputTitle, reviewTxfield, this.inputReview, addbtn, delbtn);

        // Status message text
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);

        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(searchTxfield, searchField, labelHb, tableOfCourses, buttonHb, actionStatus, tableOfReviews);

        // Scene
        Scene scene = new Scene(vbox, 1280, 800); // w x h
        primaryStage.setScene(scene);
        primaryStage.show();

        // Select the first row onCreate
        tableOfReviews.getSelectionModel().select(0);

        tableOfCourses.getSelectionModel().select(0);
        CourseModel courseModel = tableOfCourses.getSelectionModel().getSelectedItem();
        actionStatus.setText(courseModel.toString());

        // TODO: 2019-04-02 dynamic database search
        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    addTextFilter(dataOfCourses,searchField,tableOfCourses);
                }
        );
    }
    private class RowSelectChangeListener implements ChangeListener<Number> {

        private Number actuallySelectedItemsInColumnOfReviews = 0;

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number oldVal, Number newVal) {
            actuallySelectedItemsInColumnOfReviews = ov.getValue();
            System.out.println("pobiera z drugiej kolumny");

            int ix = newVal.intValue();

            if ((ix < 0) || (ix >= dataOfCourses.size())) {
                System.out.println("Błędny indeks");

                return;
            }
            //print reviews from selected item
            System.out.println();
            tableOfReviews
                    .setItems(FXCollections.observableArrayList(
                            dataOfCourses.get((int) actuallySelectedItemsInColumnOfReviews).getReview()));
            CourseModel courseModel = dataOfCourses.get(ix);
            actionStatus.setText(courseModel.toString());

        }
    }

    private class AddButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {


            HttpURLConnection conn;

            if (!inputTitle.getText().trim().equals("")) {
                if (!inputReview.getText().trim().equals("")) {

                    CourseModel courseModel = new CourseModel();
                    courseModel.setTitle(inputTitle.getText().trim());
                    courseModel.setComment(inputReview.getText().trim());

                    try {
                        conn = JSonService
                                .httpConnectToREST("http://localhost:5050/api/course",
                                        "POST",
                                        "Authorization");

                        boolean isContainActuallyAddedCourse = false;

                        //if in database we have this course update reviews
                        for (int i = 0; i < dataOfCourses.size(); i++) {
                            if (dataOfCourses.get(i).getTitle().equals(courseModel.getTitle())) {
                                dataOfCourses.get(i).setComment(courseModel.getReview().get(0).getComment());
                                JSonService.putObject(dataOfCourses.get(i));
                                isContainActuallyAddedCourse = true;
                                break;
                            }
                        }

                        //if we dont have this user
                        if (!isContainActuallyAddedCourse) {
                            //add courseModel to database by REST URL
                            //JSonService.postGetDeleteHttpREST(null,"http://localhost:5050/api/course","POST",courseModel);
                            JSonService.addParsedJsonObject(courseModel, conn);
                        }
                        //clear old data
                        dataOfCourses = null;
                        //set data from REST server
                        dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
                        tableOfCourses.setItems(dataOfCourses);
                        //tableOfReviews.refresh();

                        // Select the new row
                        int row = dataOfCourses.size() - 1;
                        tableOfCourses.requestFocus();
                        tableOfCourses.getSelectionModel().select(row);
                        tableOfCourses.getFocusModel().focus(row);
                        tableOfCourses.refresh();
                        actionStatus.setText("Nowy kurs: Dodaj tytuł kursu i opinie. Zatwierdź przyciskiem <Dodaj>.");
                        //clear data
                        dataOfCourses = null;
                        //add new data
                        dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    actionStatus.setText("Błędne dane! Opinia nie może być pusta!");
                }
            } else {
                actionStatus.setText("Błędne dane! Kurs nie może być pusty!");
            }
        }

    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            int ix = tableOfCourses.getSelectionModel().getSelectedIndex();
            CourseModel courseModel = tableOfCourses.getSelectionModel().getSelectedItem();
            System.out.println("Pobrano z tabelki użytkownika: " + courseModel.toString());

            // Get selected row and delete
            if (tableOfCourses.getSelectionModel().getSelectedItem() != null) {

                try {
                    JSonService.postGetDeleteHttpREST(
                            courseModel.getId(),
                            "http://localhost:5050/api/course",
                            "DELETE",
                            null);
                    dataOfCourses.remove(ix);

                    dataOfCourses.addAll(JSonService.getListOfModels());
                    tableOfCourses.setItems(FXCollections.observableArrayList(FXCollections.observableArrayList(JSonService.getListOfModels())));
                    tableOfCourses.refresh();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // Select a row
                if (tableOfCourses.getItems().size() == 0) {
                    actionStatus.setText("Brak danych w tabeli!");
                    return;
                }

                if (ix != 0) {

                    ix = ix - 1;
                }

                tableOfCourses.requestFocus();
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
