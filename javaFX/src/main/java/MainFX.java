import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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



public class MainFX extends Application {
    private TableView<CourseModel> tableOfCourses;
    private TableView<ReviewModel> tableOfReviews;

    private ObservableList<CourseModel> dataOfCourses;

    private Text actionStatus;
    //input to add Title
    private TextField inputTitle = new TextField();
    //input to add Review
    private TextField inputReview = new TextField();

    private Number actuallySelectedItemsInColumnOfReviews = 0;
    private String getActuallyEditedColumn = "";




    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Projekt");

        // Books label
        Label topLabel = new Label("Kursy");
        topLabel.setTextFill(Color.DARKBLUE);
        topLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(topLabel);


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
                ).setTitle(getActuallyEditedColumn = t.getNewValue());
            }

        });

        tableOfReviews = new TableView<>();

        tableOfReviews.setEditable(true);

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

        //tableOfCourses.getColumns().setAll(titleCol, authorCol);
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
        Text titleLabel = new Text("Tytuł:");
        Text reviewLabel = new Text("Ocena");

        Button addbtn = new Button("Dodaj");
        addbtn.setOnAction(new AddButtonListener());
        Button delbtn = new Button("Usuń");
        delbtn.setOnAction(new DeleteButtonListener());
        HBox buttonHb = new HBox(10);
        buttonHb.setAlignment(Pos.CENTER);
        buttonHb.getChildren().addAll(titleLabel, this.inputTitle, reviewLabel, this.inputReview, addbtn, delbtn);

        // Status message text
        actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);

        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(labelHb, tableOfCourses, buttonHb, actionStatus, tableOfReviews);

        // Scene
        Scene scene = new Scene(vbox, 800, 600); // w x h
        primaryStage.setScene(scene);
        primaryStage.show();

        // Select the first row
        tableOfReviews.getSelectionModel().select(0);

        tableOfCourses.getSelectionModel().select(0);
        CourseModel courseModel = tableOfCourses.getSelectionModel().getSelectedItem();
        actionStatus.setText(courseModel.toString());

    }

    private class RowSelectChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number oldVal, Number newVal) {
            System.out.println("Aktaulnie zaznaczony: "+ov.getValue());
            actuallySelectedItemsInColumnOfReviews = newVal;
            System.out.println(dataOfCourses.get((int)actuallySelectedItemsInColumnOfReviews).getReview());
            tableOfReviews.setItems(FXCollections.observableArrayList(dataOfCourses.get((int)actuallySelectedItemsInColumnOfReviews).getReview()));

            int ix = newVal.intValue();

            if ((ix < 0) || (ix >= dataOfCourses.size())) {

                return; // invalid dataOfCourses
            }

            CourseModel courseModel = dataOfCourses.get(ix);
            actionStatus.setText(courseModel.toString());
        }
    }


    private class AddButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            System.out.println("Pobrałem aktualnie edytowany wiersz! : " + getActuallyEditedColumn);


            boolean isContainActuallyAddedCourse = false;


            // Create a new row after last row
            CourseModel courseModel = new CourseModel();
            courseModel.setTitle(inputTitle.getText());
            courseModel.setComment(inputReview.getText());


            for(int i =0; i<dataOfCourses.size(); i++){
                if(dataOfCourses.get(i).getTitle().equals(courseModel.getTitle())){

                    dataOfCourses.get(i).setComment(inputReview.getText());
                    System.out.println("Udało się porównać!");
                    isContainActuallyAddedCourse = true;


                }
                System.out.println(dataOfCourses.get(i).getTitle()+ ":" + inputTitle.getText());
                tableOfCourses.refresh();
            }


            if(isContainActuallyAddedCourse == false) {
                dataOfCourses.add(courseModel);
            }
            int row = dataOfCourses.size() - 1;

            // Select the new row
            tableOfCourses.requestFocus();
            tableOfCourses.getSelectionModel().select(row);
            tableOfCourses.getFocusModel().focus(row);

            actionStatus.setText("Nowy kurs: Dodaj tytuł kursu i opinie. Zatwierdź <Enter>.");
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            // Get selected row and delete
            int ix = tableOfCourses.getSelectionModel().getSelectedIndex();
            CourseModel courseModel = (CourseModel) tableOfCourses.getSelectionModel().getSelectedItem();
            dataOfCourses.remove(ix);
            actionStatus.setText("Usunięto: " + courseModel.toString());

            // Select a row

            if (tableOfCourses.getItems().size() == 0) {

                actionStatus.setText("Brak danych w tabeli!");
                return;
            }

            if (ix != 0) {

                ix = ix -1;
            }

            tableOfCourses.requestFocus();
            tableOfCourses.getSelectionModel().select(ix);
            tableOfCourses.getFocusModel().focus(ix);
        }
    }
}
