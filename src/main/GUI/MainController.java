package main.GUI;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Model model;
    @FXML Label word;
    @FXML ImageView image;
    @FXML TextArea translation;
    @FXML Label info;
    private int counter = 0;

    private DictionaryElement current_element;
    private ArrayList<DictionaryElement> last_list;
    private DictionaryElement last_element;

    private void load() {
        current_element = Diki.downloadTranslation(model.list_unknown.get(0));
        word.setText(current_element.englishWord);
        try {
            Image img = new Image(new File("files/pronunciation/" + current_element.englishWord + ".png").toURI().toString());
            image.setFitHeight(1.25 * img.getHeight());
            image.setFitWidth(1.25 * img.getWidth());
            image.setImage(img);
        }
        catch (Exception ignored) {
            image.setImage(null);
        }
        translation.setText(current_element.meanings.replace("; ", "\n"));

        new_value.setText(current_element.englishWord);

        saving_info.setText("");
    }

    @FXML TextField new_value;
    @FXML ProgressBar progress_bar;
    private double total_amount = 0;
    private double added_amount = 0;
    private void moveAndLoadNext(ArrayList<DictionaryElement> list, String name_to_info) {
        current_element.meanings = translation.getText().replace("\n", "; ");
        list.add(current_element);
        model.list_unknown.remove(0);

        info.setText("\"" + current_element.englishWord + "\" was moved to " + name_to_info + ".");

        last_list=list;
        last_element=current_element;

        load();

        counter++;
        if (counter % 10 == 0) model.save(saving_progress, saving_info);
    }

    @FXML
    void add_known() {
        moveAndLoadNext(model.list_known, "\"known\" list");
    }

    @FXML
    void add_to_repeat() {
        moveAndLoadNext(model.list_to_repeat, "\"to repeat\" list");
    }

    @FXML
    void add_to_learn() {
        moveAndLoadNext(model.list_to_learn, "\"to learn\" list");
    }

    @FXML
    void undo() {
        if(last_element != null && last_list != null) {

            last_list.remove(last_list.size()-1);
            model.list_unknown.add(last_element.englishWord);

            info.setText("UNDID - " + last_element.englishWord + " moved to unknown list.");

            last_list = null;
            last_element = null;
        }
    }

    @FXML Label saving_info;
    @FXML ProgressIndicator saving_progress;

    @FXML
    void save_action() {
        model.save(saving_progress, saving_info);
    }

    @FXML
    void fix() {
        current_element = Diki.downloadTranslation(new_value.getText());
        word.setText(current_element.englishWord);
        try {
            Image img = new Image(new File("files/pronunciation/" + current_element.englishWord + ".png").toURI().toString());
            image.setFitHeight(1.25 * img.getHeight());
            image.setFitWidth(1.25 * img.getWidth());
            image.setImage(img);
        }
        catch (Exception ignored) {
            image.setImage(null);
        }
        translation.setText(current_element.meanings.replace("; ", "\n"));
    }

    private void addEditButtonToTable(TableView table, ArrayList<DictionaryElement> list) {
        TableColumn<TableElement, Void> colBtn = new TableColumn("");

        Callback<TableColumn<TableElement, Void>, TableCell<TableElement, Void>> cellFactory = new Callback<TableColumn<TableElement, Void>, TableCell<TableElement, Void>>() {
            @Override
            public TableCell<TableElement, Void> call(final TableColumn<TableElement, Void> param) {
                final TableCell<TableElement, Void> cell = new TableCell<TableElement, Void>() {

                    private final Button btn = new Button("edit");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            TableElement data = getTableView().getItems().get(getIndex());

                            EditController.editing_word = new DictionaryElement(data.getWord(), data.getMeaning());
                            EditController.list = list;

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_view.fxml"));
                            Parent root = null;
                            try {
                                root = fxmlLoader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage = new Stage();
                            stage.setTitle("Edit word");
                            stage.setScene(new Scene(root));
                            stage.show();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
    }

    private void addMoveButtonToTable(TableView table, ArrayList<DictionaryElement> list) {
        TableColumn<TableElement, Void> colBtn = new TableColumn("");

        Callback<TableColumn<TableElement, Void>, TableCell<TableElement, Void>> cellFactory = new Callback<TableColumn<TableElement, Void>, TableCell<TableElement, Void>>() {
            @Override
            public TableCell<TableElement, Void> call(final TableColumn<TableElement, Void> param) {
                final TableCell<TableElement, Void> cell = new TableCell<TableElement, Void>() {

                    private final Button btn = new Button("move");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            TableElement data = getTableView().getItems().get(getIndex());

                            MoveController.moving_word = new DictionaryElement(data.getWord(), data.getMeaning());
                            MoveController.list = list;

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("move_view.fxml"));
                            Parent root = null;
                            try {
                                root = fxmlLoader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage = new Stage();
                            stage.setTitle("Move word");
                            stage.setScene(new Scene(root));
                            stage.show();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
    }

    private void fillTable(TableView table, ArrayList<TableElement> list) {
        table.getColumns().clear();

        final ObservableList<TableElement> data =
                FXCollections.observableArrayList(list);

        TableColumn wordCol = new TableColumn("word");
        wordCol.setMinWidth(50);
        wordCol.setCellValueFactory(
                new PropertyValueFactory<TableElement, String>("word"));

        TableColumn meaningCol = new TableColumn("meaning");
        meaningCol.setMinWidth(300);
        meaningCol.setCellValueFactory(
                new PropertyValueFactory<TableElement, String>("meaning"));

        table.setItems(data);
        table.getColumns().addAll(wordCol, meaningCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // przeglądaj znane
    @FXML TableView known_table;
    @FXML Label number_of_known;
    @FXML
    void reload_known() {
        ArrayList<TableElement> list = new ArrayList<>();
        for(DictionaryElement elem : model.list_known) {
            list.add(new TableElement(elem.englishWord, elem.meanings));
        }
        fillTable(known_table, list);
        addEditButtonToTable(known_table, model.list_known);
        addMoveButtonToTable(known_table, model.list_known);

        number_of_known.setText("Current number of records: " + model.list_known.size());
    }

    @FXML TableView repeat_table;
    @FXML Label number_of_repeat;
    @FXML
    void reload_repeat() {
        ArrayList<TableElement> list = new ArrayList<>();
        for(DictionaryElement elem : model.list_to_repeat) {
            list.add(new TableElement(elem.englishWord, elem.meanings));
        }
        fillTable(repeat_table, list);
        addEditButtonToTable(repeat_table, model.list_to_repeat);
        addMoveButtonToTable(repeat_table, model.list_to_repeat);

        number_of_repeat.setText("Current number of records: " + model.list_to_repeat.size());
        search_repeat.clear();
    }

    @FXML TableView learn_table;
    @FXML Label number_of_learn;
    @FXML
    void reload_learn() {
        ArrayList<TableElement> list = new ArrayList<>();
        for(DictionaryElement elem : model.list_to_learn) {
            list.add(new TableElement(elem.englishWord, elem.meanings));
        }
        fillTable(learn_table, list);
        addEditButtonToTable(learn_table, model.list_to_learn);
        addMoveButtonToTable(learn_table, model.list_to_learn);

        number_of_learn.setText("Current number of records: " + model.list_to_learn.size());
    }

    void generate_pdf(ArrayList<DictionaryElement> list) throws IOException, DocumentException {
        PDFCreator.generatePDF(list);
    }
    @FXML
    void generate_pdf_known() throws IOException, DocumentException {
        generate_pdf(model.list_known);
    }
    @FXML
    void generate_pdf_repeat() throws IOException, DocumentException {
        generate_pdf(model.list_to_repeat);
    }
    @FXML
    void generate_pdf_learn() throws IOException, DocumentException {
        generate_pdf(model.list_to_learn);
    }

    @FXML TextField search_known;
    @FXML
    void search_in_known(){
        String pattern = search_known.getText();
        if (pattern.isEmpty()) return;
        pattern = "^.*" + pattern + ".*$";
        ArrayList<DictionaryElement> result = new ArrayList<>();
        for (DictionaryElement elem : model.list_known) {
            if(elem.englishWord.matches(pattern) || elem.meanings.matches(pattern)) result.add(elem);
        }

        ArrayList<TableElement> list = new ArrayList<>();
        for(DictionaryElement elem : result) {
            list.add(new TableElement(elem.englishWord, elem.meanings));
        }
        fillTable(known_table, list);
        addEditButtonToTable(known_table, model.list_known);
        addMoveButtonToTable(known_table, model.list_known);
        number_of_known.setText("Current number of records: " + result.size());
    }

    @FXML TextField search_repeat;
    @FXML
    void search_in_repeat(){
        String pattern = search_repeat.getText();
        if (pattern.isEmpty()) return;
        pattern = "^.*" + pattern + ".*$";
        ArrayList<DictionaryElement> result = new ArrayList<>();
        for (DictionaryElement elem : model.list_to_repeat) {
            if(elem.englishWord.matches(pattern) || elem.meanings.matches(pattern)) result.add(elem);
        }

        ArrayList<TableElement> list = new ArrayList<>();
        for(DictionaryElement elem : result) {
            list.add(new TableElement(elem.englishWord, elem.meanings));
        }
        fillTable(repeat_table, list);
        addEditButtonToTable(repeat_table, model.list_to_repeat);
        addMoveButtonToTable(repeat_table, model.list_to_repeat);
        number_of_repeat.setText("Current number of records: " + result.size());
    }

    @FXML TextField search_learn;
    @FXML
    void search_in_learn(){
        String pattern = search_learn.getText();
        if (pattern.isEmpty()) return;
        pattern = "^.*" + pattern + ".*$";
        ArrayList<DictionaryElement> result = new ArrayList<>();
        for (DictionaryElement elem : model.list_to_learn) {
            if(elem.englishWord.matches(pattern) || elem.meanings.matches(pattern)) result.add(elem);
        }

        ArrayList<TableElement> list = new ArrayList<>();
        for(DictionaryElement elem : result) {
            list.add(new TableElement(elem.englishWord, elem.meanings));
        }
        fillTable(learn_table, list);
        addEditButtonToTable(learn_table, model.list_to_learn);
        addMoveButtonToTable(learn_table, model.list_to_learn);
        number_of_learn.setText("Current number of records: " + result.size());
    }

    @FXML StackedBarChart daily_chart;
    @FXML PieChart overall_chart;

    @FXML
    void reload_charts() {
        estimateTimeToEnd();

        XYChart.Series KnownDataSeries = new XYChart.Series();
        KnownDataSeries.setName("known");
        for(StatisticData elem : model.statistics) {
            KnownDataSeries.getData().add(new XYChart.Data(elem.getDate(), elem.getKnown_number()));
        }

        XYChart.Series RepeatDataSeries = new XYChart.Series();
        RepeatDataSeries.setName("to repeat");
        for(StatisticData elem : model.statistics) {
            RepeatDataSeries.getData().add(new XYChart.Data(elem.getDate(), elem.getRepeat_number()));
        }

        XYChart.Series LearnDataSeries = new XYChart.Series();
        LearnDataSeries.setName("to learn");
        for(StatisticData elem : model.statistics) {
            LearnDataSeries.getData().add(new XYChart.Data(elem.getDate(), elem.getLearn_number()));
        }
        
        daily_chart.getData().clear();
        daily_chart.setAnimated(false);
        daily_chart.setLegendVisible(true);
        daily_chart.getData().addAll(KnownDataSeries, RepeatDataSeries, LearnDataSeries);
        //------------------------------------------------
        ObservableList<PieChart.Data> valueList = FXCollections.observableArrayList(
                new PieChart.Data("known (" +  model.list_known.size() + ")", model.list_known.size()),
                new PieChart.Data("to repeat (" +  model.list_to_repeat.size() + ")", model.list_to_repeat.size()),
                new PieChart.Data("to learn (" +  model.list_to_learn.size() + ")", model.list_to_learn.size()),
                new PieChart.Data("unknown (" +  model.list_unknown.size() + ")", model.list_unknown.size())
        );

        overall_chart.getData().clear();
        overall_chart.setAnimated(false);
        overall_chart.setData(valueList);
    }

    @FXML Label days_to_end;
    @FXML Label avg;
    @FXML Label overall;
    @FXML void estimateTimeToEnd() {
        int days = 10;

        int sum = 0;
        for(StatisticData elem : Model.statistics) {
            sum += elem.getKnown_number() + elem.getRepeat_number() + elem.getLearn_number();
        }

        double day_avarage = sum/(double)days;
        double daysToEnd = Model.list_unknown.size()/day_avarage;
        days_to_end.setText((int)daysToEnd + " days");

        //avg
        avg.setText(String.valueOf(day_avarage));

        // overall
        int done = Model.list_known.size() + Model.list_to_repeat.size() + Model.list_to_learn.size();
        int total = done + Model.list_unknown.size();
        overall.setText(done + "/" + total);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new Model();
        load();
        added_amount = model.list_known.size() + model.list_to_repeat.size() + model.list_to_learn.size();
        total_amount = added_amount + model.list_unknown.size();
        progress_bar.setProgress(added_amount/total_amount);
    }
}