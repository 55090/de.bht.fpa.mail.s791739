package de.bht.fpa.mailApp.s791739.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Start class for Application
 * @author András Bucsi
 * @version 1.0
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("/de/bht/fpa/mailApp/s791739/view/FXMLDocument.fxml"));
	Scene scene = new Scene(root);
	
	stage.setScene(scene);
	stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	launch(args);
    }
}
