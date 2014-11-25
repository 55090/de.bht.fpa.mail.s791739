package de.bht.fpa.mailApp.s791739.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.Window;
 
/**
 * Controller Class for the history view of used directories
 * 
 * @author Marco Kollosche, András Bucsi (FPA Strippgen)
 * @version Aufgabe 3 Zusatz 2014-11-13
 */
public class HistoryController implements Initializable{

    /**
     * injected open button 
     */
    @FXML
    private Button history_open;
    
    /**
     * injected cancel button 
     */
    @FXML
    private Button history_cancel;
    
    /**
     * injected List View 
     */
    @FXML
    private ListView<File> history_list_view;
    
    /**
     * Main stage Controller field
     */
    private final FXMLDocumentController fdController;
    
    /**
     * History View Controller Constructor
     * @param fdController controller of the main stage / view
     */
    public HistoryController( final FXMLDocumentController fdController ){
        this.fdController = fdController;
    }
   

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize( final URL location, final ResourceBundle resources ) {
        history_cancel. setOnAction( ( ActionEvent event ) -> handleButtonEvent( event ) );
        history_open.   setOnAction( ( ActionEvent event ) -> handleButtonEvent( event ) );
        
        configureListView();
        
    } 
    
    /**
     * Method handles events originating from buttons of the history view
     * @param event event source from buttons of the hstory view
     */
    private void handleButtonEvent( final ActionEvent event ) {
        switch( ( (Button) event.getSource() ).getId() ){
            case "history_open":    fdController.setTreeRoot( history_list_view.getSelectionModel().getSelectedItem() );
                                    close( history_open.getScene().getWindow() );
                                    break;
            case "history_cancel":  close( history_cancel.getScene().getWindow() );
                                    break;
            default:                break;
        }
    }
    
    /**
     * Method configures the ListView from a TreeSet<File>
     * it removes all ListView items initially and then adds all items from the TreeSet
     * if the TreeSet is empty it adds a default File to the list...
     */
    private void configureListView() {
        history_list_view.getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
        history_list_view.getItems().removeAll( history_list_view.getItems() );
        
        TreeSet<File> historySet = fdController.getHistorySet();
        if(historySet.isEmpty()){
            history_list_view.getItems().add( new File( "no history, yet..." ) );
            history_open.setDisable( true );
        } else {
            history_list_view.getItems().addAll( historySet );
        }
        
    }
    
    /**
     * Method closes the View
     * @param window origin view
     */
    private void close( final Window window ) {
        ( (Stage) window ).close();
    }
}
