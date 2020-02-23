package main.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.DictionaryElement;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    static DictionaryElement editing_word;
    static ArrayList<DictionaryElement> list;

    @FXML TextField edit_word;
    @FXML ImageView edit_image;
    @FXML TextArea edit_translation;
    @FXML Button save;

    @FXML
    void edit_save() {
        for(DictionaryElement elem : list) {
            if(elem.englishWord.equals(editing_word.englishWord) && elem.meanings.equals(editing_word.meanings)) {
                elem.englishWord = edit_word.getText();
                elem.meanings = edit_translation.getText().replace("\n", "; ");
            }
        }
        ((Stage)save.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        edit_word.setText(editing_word.englishWord);

        try {
            Image img = new Image(new File("files/pronunciation/" + editing_word.englishWord + ".png").toURI().toString());
            edit_image.setFitHeight(1.25 * img.getHeight());
            edit_image.setFitWidth(1.25 * img.getWidth());
            edit_image.setImage(img);
        }
        catch (Exception ignored) {
            edit_image.setImage(null);
        }

        edit_translation.setText(editing_word.meanings.replace("; ", "\n"));
    }
}