package main;

import java.io.*;
import java.util.ArrayList;

class Model {
    static ArrayList<String> list_unknown = new ArrayList<>();
    static ArrayList<DictionaryElement> list_known = new ArrayList<>();
    static ArrayList<DictionaryElement> list_to_repeat = new ArrayList<>();
    static ArrayList<DictionaryElement> list_to_learn = new ArrayList<>();

    Model() {
        try {
            load_from_file("input.txt", list_unknown);
            load_from_CSVfile("known.txt", list_known);
            load_from_CSVfile("to_repeat.txt", list_to_repeat);
            load_from_CSVfile("to_learn.txt", list_to_learn);
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
            list.add(DictionaryElement.fromString(line));
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

    void save() {
        try {
            save_to_file();
            save_to_CSVfile("known.txt", list_known);
            save_to_CSVfile("to_repeat.txt", list_to_repeat);
            save_to_CSVfile("to_learn.txt", list_to_learn);
        }
        catch (IOException ignored) {}
    }
}
