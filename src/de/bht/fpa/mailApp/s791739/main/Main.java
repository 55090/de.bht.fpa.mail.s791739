package de.bht.fpa.mailApp.s791739.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Start class for Application
 * @author Marco Kollosche, András Bucsi (FPA Strippgen)
 * @version Aufgabe 3 2014-11-13
 */
public class Main extends Application {

    @Override
    public void start( Stage stage ) throws Exception {
	Parent root = FXMLLoader.load( getClass().getResource( "/de/bht/fpa/mailApp/s791739/view/FXMLDocument.fxml" ) );
	Scene scene = new Scene( root );
	
	stage.setScene( scene );
	stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main( final String[] args ) {
	launch( args );
    }
}
