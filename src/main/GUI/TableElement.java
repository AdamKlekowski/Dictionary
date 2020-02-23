package main.GUI;

import javafx.beans.property.SimpleStringProperty;

public class TableElement {
    private final SimpleStringProperty word;
    private final SimpleStringProperty meaning;

    public TableElement(String word, String meaning) {
        this.word = new SimpleStringProperty(word);
        this.meaning = new SimpleStringProperty(meaning);
    }

    public String getWord() {
        return word.get();
    }

    public SimpleStringProperty wordProperty() {
        return word;
    }

    public String getMeaning() {
        return meaning.get();
    }

    public SimpleStringProperty meaningProperty() {
        return meaning;
    }
}
