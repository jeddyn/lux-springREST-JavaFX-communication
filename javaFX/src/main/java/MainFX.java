import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
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
import service.JSonService;

import java.io.IOException;
import java.net.HttpURLConnection;


public class MainFX extends Application {
    Service<Void> backgroundThread;


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

    private Number actuallySelectedItemsInColumnOfReviews = 0;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Projekt");


        Label topLabel = new Label("Kursy");
        topLabel.setTextFill(Color.DARKBLUE);
        topLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().addAll(searchField, topLabel);


        // tworzenie nowego widoku, pobieranie danych z bazy, wrzucanie danych do tabelki, edit true
        tableOfCourses = new TableView<>();
        dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
        tableOfCourses.setItems(dataOfCourses);
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


        // Add and delete buttons
        Text titleTxfield = new Text("Tytuł:");
        Text reviewTxfield = new Text("Ocena");
        Text searchTxfield = new Text("Szukaj: ");


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
                    System.out.println(newValue);
                }
        );

    }

    private class RowSelectChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number oldVal, Number newVal) {
            actuallySelectedItemsInColumnOfReviews = ov.getValue();


            int ix = newVal.intValue();

            if ((ix < 0) || (ix >= dataOfCourses.size())) {

                return;
            }
            //print reviews from selected item
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
            try {
                conn = JSonService
                        .httpConnectToREST("http://localhost:5050/api/course",
                                "POST",
                                "Authorization");

                boolean isContainActuallyAddedCourse = false;

                //create new user and set args
                CourseModel courseModel = new CourseModel();
                courseModel.setTitle(inputTitle.getText());
                courseModel.setComment(inputReview.getText());


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

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            dataOfCourses = null;
            try {//check its could be single get from REST
                dataOfCourses = FXCollections.observableArrayList(JSonService.getListOfModels());
            } catch (IOException e1) {
                e1.printStackTrace();
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
}
