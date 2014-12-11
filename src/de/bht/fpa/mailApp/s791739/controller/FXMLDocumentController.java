package de.bht.fpa.mailApp.s791739.controller;

import de.bht.fpa.mailApp.s791739.model.applicationLogic.EmailManagerIF;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.FileManager;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.FolderManagerIF;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.MailManager;
import de.bht.fpa.mailApp.s791739.model.data.Component;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.FileElement;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.TreeSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller Class for FXMLDocument
 * @author Marco Kollosche, András Bucsi (FPA Strippgen)
 * @version Aufgabe 4 2014-11-25
 */
public class FXMLDocumentController implements Initializable {
    
    /**
     * Image for the folder visualization
     */
    private final Image FOLDER_ICON      = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/folder_Icon.png" ) );
    
    /**
     * Image for the folder visualization
     */
    private final Image FOLDER_OPEN_ICON = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/folder_open_Icon.png" ) );
    
    /**
     * Image for the file visualization
     */
    private final Image FILE_ICON        = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/file_Icon.png" ) );
    
    /**
     * String of root path
     */
    private final static String S_DEFAULT_ROOTPATH = "C:/Users/Me/Desktop/Account";
    
    /**
     * File for initial path
     */
    private final static File DEFAULT_ROOTPATH     = new File( S_DEFAULT_ROOTPATH );
    
    /**
     * Dummy Element to show arrow of branch expander
     */
    private final static TreeItem<Component> DUMMY = new TreeItem<> ( new Folder(new File( "" ), true ) );
    
    /**
     * FolderManager
     */
    private final FolderManagerIF folderManager;
    
    /**
     * E-Mail manager
     */
    private final EmailManagerIF mailManager;
    
    /**
     * Saves used directories
     */
    private final TreeSet<File> historySet;
    
    /**
     * injection from FXMLDocument GUI
     */
    @FXML
    TreeView treeView;
    
    /**
     * injection from FXMLDocument GUI
     */
    @FXML
    MenuBar menuBar;
    
    /**
     * injection from FXMLDocument GUI
     */
    @FXML
    TableView tableView;

    /**
     * Constructor
     */
    public FXMLDocumentController(){
        folderManager   = new FileManager( DEFAULT_ROOTPATH );
        mailManager     = new MailManager();
        historySet      = new TreeSet<>();
    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize( final URL location, final ResourceBundle resources ) {
        configureTree();
        configureMenue();
        configureTableView();
    }   
    
    /**
     * Method to initially configure tree
     */
    private void configureTree(){
        setTreeRoot( DEFAULT_ROOTPATH );
        treeView.getFocusModel().focusedItemProperty().addListener( ( ObservableValue observable, Object oldValue, Object newValue ) -> handleEmailEvent( (TreeItem<Component>) newValue ) );
    }
    
    /**
     * Method configures the Menu Items with event handler
     */
    private void configureMenue(){
        menuBar.getMenus().stream().forEach( ( menu )-> { 
            menu.getItems().stream().forEach( ( items )-> {
                items.setOnAction( ( event )-> handleMenueEvent( event ) );
            });
        });
    }
    
    private void configureTableView(){
        for (Object tc : tableView.getColumns()){
            TableColumn tcol = (TableColumn) tc;
            switch (tcol.getId()){
                case "Importance": tcol.setCellValueFactory(new PropertyValueFactory<>("importance"));
                    System.out.println("Importance");
                    break;
                case "Received": tcol.setCellValueFactory(new PropertyValueFactory<>("received"));
                    System.out.println("Received");
                    break;
                case "Read": tcol.setCellValueFactory(new PropertyValueFactory<>("read"));
                    System.out.println("Read");
                    break;
                case "Sender": tcol.setCellValueFactory(new PropertyValueFactory<>("sender"));
                    System.out.println("Sender");
                    break;
                case "Recipients": tcol.setCellValueFactory(new PropertyValueFactory<>("receiver"));
                    System.out.println("Recipients");
                    break;
                case "Subject": tcol.setCellValueFactory(new PropertyValueFactory<>("subject"));
                    System.out.println("Subject");
                    break;
                default:break;
            }
        }
    }
    
    /**
     * Method to set root to tree
     * @param rootPath given File to set as root for tree
     */
    protected void setTreeRoot( final File rootPath ){
	folderManager.setTopFolder( rootPath );
        
        TreeItem<Component> rootItem = new TreeItem<> ( folderManager.getTopFolder(), new ImageView( FOLDER_OPEN_ICON ) ); 
	rootItem.setExpanded( true );
        
        rootItem.addEventHandler( TreeItem.branchExpandedEvent(),  ( TreeItem.TreeModificationEvent <Component> e ) -> handleExpandEvent( e ) );
        rootItem.addEventHandler( TreeItem.branchCollapsedEvent(), ( TreeItem.TreeModificationEvent <Component> e ) -> handleCollapseEvent( e ) );
        
	treeView.setRoot( rootItem );
	loadTreeItemContents( rootItem );
    }

    /**
     * Method handles the events coming from the menu items
     * @param e event source
     */
    private void handleMenueEvent( final Event e ){
        switch( ( (MenuItem)e.getSource() ).getId() ){
            case "fileOpen":
                File openNewRoot = openDirectoryChooser(); 
                if ( openNewRoot == null ){
                    break;
                } else{
                    setTreeRoot( openNewRoot );
                    if ( openNewRoot != DEFAULT_ROOTPATH ){
                        historySet.add( openNewRoot );
                    }
                }  
                break;
            case "fileHistory": showHistoryView(); 
                
                break;
        }
    }
    
    /**
     * Method opens a Window to chose a base directory
     * @returns a File with the new chosen base directory (or null if no choice)
     */
    private File openDirectoryChooser(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle( "Select new Base Directory!" );
        return dc.showDialog( null );
    }
    
    /**
     * Callback method for TreeModificationEvents (return type TreeItem.branchExpandedEvent())
     * @param e TreeModificationEvent when expandable branch has been clicked
     */
    private void handleExpandEvent( final TreeModificationEvent <Component> e ){
        loadTreeItemContents( e.getTreeItem() );
        e.getTreeItem().setGraphic( new ImageView( FOLDER_OPEN_ICON ) );
    }
    
    /**
     * Callback method for TreeModificationEvents (return type TreeItem.branchCollapsedEvent())
     * @param e TreeModificationEvent when expanded branch has been clicked to collapse
     */
    private void handleCollapseEvent( final TreeModificationEvent <Component> e ){
        /**
         * node to reference the treeItem contained in the TreeModificationEvent
         */
        TreeItem node = e.getTreeItem();
        e.getTreeItem().setGraphic( new ImageView( FOLDER_ICON ) );
        node.getChildren().removeAll( node.getChildren() );
        addDummy( node );
    }
    
    /**
     * Method responds to a ChangeEvent and loads all mails if contained in that folder
     * @param node folder that was clicked / fired the event/change
     */
    private void handleEmailEvent( final TreeItem<Component> node ) {
        if ( node != null ) {
            mailManager.loadMails( (Folder) node.getValue() );
            //mailManager.printMails( (Folder) node.getValue() );
            ObservableList<Email> ol = mailManager.listMails((Folder) node.getValue());
            if (ol!=null){
                tableView.setItems(ol);
                
            }
        }
    }
   
    /**
     * Method removes all children a first (including dummy elements ) and then loads all children of a TreeItem
     * @param node TreeItem
     */
    private void loadTreeItemContents( final TreeItem<Component> node ){
        Folder folder = (Folder)node.getValue();
        node.getChildren().remove( DUMMY );
        folderManager.loadContent( folder );
        
        folder.getComponents().stream().forEach( ( Component subComponent ) -> {
            if( subComponent instanceof Folder ){
                addFolder( new File( subComponent.getPath() ), node, subComponent.isExpandable() );
            }
        });
    }
    
    /**
     * Method adds a dummy folder to a directory treeItem, thus the Folder indicates to be expandable
     * @param node the node where the dummy element should be added
     */
    private void addDummy( final TreeItem node ){
        node.getChildren().add( DUMMY );
    }
    
    /**
     * Method adds a folder to a parent TreeItem
     * @param path File path of the folder that shall be added
     * @param node parent TreeItem
     * @param isExpandable indicates whether the folder contains further folders
     */
    private void addFolder( final File path, final TreeItem node , final boolean isExpandable ){
        TreeItem<Folder> folder = new TreeItem<> ( new Folder(path, isExpandable ), new ImageView( FOLDER_ICON ));
        if ( isExpandable ){
            addDummy( folder );
        }
        node.getChildren().add( folder );
    }
    
    /**
     * Method adds a folder to a parent TreeItem
     * @param path File path of the file that shall be added
     * @param node parent TreeItem
     */
    private void addFile( final File path, final TreeItem node ){
        TreeItem<FileElement> file = new TreeItem<> ( new FileElement( path ), new ImageView( FILE_ICON ));
        node.getChildren().add( file );
    }
    
    /**
     * Method configures and shows the history view
     */
    private void showHistoryView() {
        Stage editStage = new Stage( StageStyle.UTILITY );
        editStage.setTitle( "Select Base Directory" );
        URL location = getClass().getResource( "/de/bht/fpa/mailApp/s791739/view/FXMLHistory.fxml" );

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation( location );
        fxmlLoader.setController( new HistoryController( this ) );
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene( myPane );
            editStage.setScene( myScene );
            editStage.show();
        } catch ( IOException ex ) {
            Logger.getLogger( FXMLDocumentController.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    /**
     * returns the history list of used directories
     * @return 
     */
    public TreeSet<File> getHistorySet() {
        return historySet;
    }
}
