package main;


import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Model {
    public static ArrayList<String> list_unknown = new ArrayList<>();
    public static ArrayList<DictionaryElement> list_known = new ArrayList<>();
    public static ArrayList<DictionaryElement> list_to_repeat = new ArrayList<>();
    public static ArrayList<DictionaryElement> list_to_learn = new ArrayList<>();

    public static int daily_goal;
    public static ArrayList<StatisticData> statistics = new ArrayList<>();
    private int number_of_known;
    private int number_of_repeat;
    private int number_of_learn;


    public Model() {
        try {
            load_from_file("input.txt", list_unknown);
            load_from_CSVfile("known.txt", list_known);
            load_from_CSVfile("to_repeat.txt", list_to_repeat);
            load_from_CSVfile("to_learn.txt", list_to_learn);
            load_statistics("info.txt");
            number_of_known = list_known.size();
            number_of_repeat = list_to_repeat.size();
            number_of_learn = list_to_learn.size();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load_from_file(String file_name, ArrayList<String> list) throws IOException {
        FileInputStream fileStream = new FileInputStream("files/" + file_name);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();
        fileStream.close();
    }

    private void load_from_CSVfile(String file_name, ArrayList<DictionaryElement> list) throws IOException {
        FileInputStream fileStream = new FileInputStream("files/" + file_name);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

        String line;
        while ((line = br.readLine()) != null) {
            try {
                list.add(DictionaryElement.fromString(line));
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Some errors occur during loading file " + file_name);
                alert.setContentText("\"" + line + "\" was not recognised as a proper word. \n" +
                        "This value will be avoided during a next saving.");
                alert.show();
            }

        }
        br.close();
        fileStream.close();
    }

    private void load_statistics(String file_name) throws IOException {
        FileInputStream fileStream = new FileInputStream("files/" + file_name);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

        String line = br.readLine();
        daily_goal = Integer.parseInt(line.split(":")[1]);

        while ((line = br.readLine()) != null) {
            String [] array = line.split(":");
            if (array.length == 2) {
                statistics.add(new StatisticData(array[0], Integer.parseInt(array[1]), 0, 0));

            }
            else {
                statistics.add(new StatisticData(array[0], Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3])));
            }
        }
        br.close();
        fileStream.close();
    }

    private void save_to_file() throws IOException {
        BufferedWriter outputFile = new BufferedWriter(new FileWriter("files/input.txt"));
        for (String word : list_unknown) {
            outputFile.write(word + "\n" );
        }
        outputFile.close();
    }

    void save_to_CSVfile(String file_name, ArrayList<DictionaryElement> list) throws IOException {
        BufferedWriter outputFile = new BufferedWriter(new FileWriter("files/" + file_name));
        for (DictionaryElement elem : list) {
            outputFile.write(elem.toString() + "\n" );
        }
        outputFile.close();
    }

    void save_statistics() throws IOException {
        BufferedWriter outputFile = new BufferedWriter(new FileWriter("files/" + "info.txt"));
        outputFile.write("Daily goal:" + daily_goal + "\n" );
        for (StatisticData elem : statistics) {
            outputFile.write(elem.toString());
        }
        outputFile.close();
    }

    public void save(ProgressIndicator saving_progress, Label saving_info) {
        try {
            saving_info.setTextFill(Color.web("#ff0000", 0.8));
            saving_info.setText("saving ...");
            //int total_number = list_unknown.size() + number_of_known + number_of_learn + number_of_repeat + 11;

            update_daily_progress();
            save_to_file();
            save_to_CSVfile("known.txt", list_known);
            save_to_CSVfile("to_repeat.txt", list_to_repeat);
            save_to_CSVfile("to_learn.txt", list_to_learn);
            save_statistics();

            saving_info.setTextFill(Color.web("#008000", 1));
            saving_info.setText("saved");
        }
        catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Some errors occur during saving!");
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
    }

    void update_daily_progress() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");
        LocalDateTime now = LocalDateTime.now();

        if(statistics.get(0).date.equals(dtf.format(now))) {
            statistics.get(0).known_number += list_known.size()-number_of_known;
            statistics.get(0).repeat_number += list_to_repeat.size()-number_of_repeat;
            statistics.get(0).learn_number += list_to_learn.size()-number_of_learn;
        }
        else {
            statistics.add(0, new StatisticData(dtf.format(now),
                        list_known.size()-number_of_known,
                        list_to_repeat.size()-number_of_repeat,
                        list_to_learn.size()-number_of_learn));
        }

        number_of_known = list_known.size();
        number_of_repeat = list_to_repeat.size();
        number_of_learn = list_to_learn.size();

        if(statistics.size() > 10) {
            statistics.remove(10);
        }
    }
}
