package main.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.DictionaryElement;
import main.Model;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MoveController implements Initializable {
    static DictionaryElement moving_word;
    static ArrayList<DictionaryElement> list;

    @FXML Label move_word;
    @FXML ChoiceBox move_choice;
    @FXML Button save;

    @FXML
    void move_save() {
        switch (move_choice.getValue().toString()) {
            case "to known":
                for(int i=0; i < list.size(); i++) {
                    if(list.get(i).englishWord.equals(moving_word.englishWord)) {
                        list.remove(i);
                        break;
                    }
                }
                Model.list_known.add(moving_word);
                break;
            case "to repeat":
                for(int i=0; i < list.size(); i++) {
                    if(list.get(i).englishWord.equals(moving_word.englishWord)) {
                        list.remove(i);
                        break;
                    }
                }
                Model.list_to_repeat.add(moving_word);
                break;
            case "to learn":
                for(int i=0; i < list.size(); i++) {
                    if(list.get(i).englishWord.equals(moving_word.englishWord)) {
                        list.remove(i);
                        break;
                    }
                }
                Model.list_to_learn.add(moving_word);
                break;
        }

        ((Stage)save.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        move_word.setText(moving_word.englishWord);
        move_choice.getItems().add("to known");
        move_choice.getItems().add("to repeat");
        move_choice.getItems().add("to learn");
    }
}