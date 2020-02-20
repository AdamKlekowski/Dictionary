package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_view.fxml"));

        stage.setTitle("Dictionary");
        stage.setScene(new Scene(root));
        //stage.getIcons().add(new Image("file:icon.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}