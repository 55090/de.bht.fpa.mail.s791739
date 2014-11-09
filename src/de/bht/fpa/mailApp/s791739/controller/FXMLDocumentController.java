package de.bht.fpa.mailApp.s791739.controller;

import de.bht.fpa.mailApp.s791739.model.appLogic.FolderManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Component;
import de.bht.fpa.mailApp.s791739.model.data.FileElement;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

/**
 * Controller Class for FXMLDocument
 * @author András Bucsi
 * @version 1.0
 */
public class FXMLDocumentController implements Initializable {
    
    /**
     * Image for the folder visualization
     */
    private final Image FOLDER_ICON   
            = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/folder_Icon.png" ) );
    
    /**
     * Image for the file visualization
     */
    private final Image FILE_ICON
            = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/file_Icon.png" ) );
    
    /**
     * String of root path
     */
    private final String S_DEFAULT_ROOTPATH = "/";
    
    /**
     * File for initial path
     */
    private final File DEFAULT_ROOTPATH     = new File(S_DEFAULT_ROOTPATH);
    
    /**
     * Dummy Element to show arrow of branch expander
     */
    private final TreeItem<Folder> DUMMY = new TreeItem<> ( new Folder(new File(""), true ) );
    
    /**
     * FolderManager
     */
    private FolderManagerIF folderManager;
    
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
     * Constructor
     */
    public FXMLDocumentController(){
    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        configureTree();
        configureMenue();
    }   
    
    /**
     * Method to initially configure tree
     */
    private void configureTree(){
	setTreeRoot(DEFAULT_ROOTPATH);
    }
    
    /**
     * Method to set root to tree
     * @param rootPath given File to set as root for tree
     */
    private void setTreeRoot(final File rootPath){
	TreeItem<Component> rootItem = new TreeItem<> (new Folder(rootPath, true), new ImageView( FOLDER_ICON )); 
	rootItem.setExpanded(true);
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent <Component> e) -> handleExpandEvent(e));
        
        rootItem.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent <Component> e) -> handleCollapseEvent(e));
	treeView.setRoot(rootItem);
	loadFolderContent(rootPath, rootItem);
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
    
    /**
     * Method handles the events coming from the menu items
     * @param e event source
     */
    private void handleMenueEvent(Event e){
        switch(((MenuItem)e.getSource()).getId()){
            case "fileOpen":
                File openNewRoot = openDirectoryChooser(); 
                if (openNewRoot != null){
                    System.out.println(openNewRoot.getAbsolutePath());
                }
            break;
            //case "history": do sth.; break;
        }
    }
    
    /**
     * Method opens a Window to chose a base directory
     * @returns a File with the new chosen base directory (or null if no choice)
     */
    private File openDirectoryChooser(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose Base Directory!");
        return dc.showDialog(null); //returns File Instance with chosen directory / or null
    }
    
    /**
     * Callback method for TreeModificationEvents (return type TreeItem.branchExpandedEvent())
     * @param e TreeModificationEvent when expandable branch has been clicked
     */
    private void handleExpandEvent(TreeModificationEvent <Component> e){
        loadFolderContent(new File(e.getTreeItem().getValue().getPath()), e.getTreeItem());
    }
    
    /**
     * Callback method for TreeModificationEvents (return type TreeItem.branchCollapsedEvent())
     * @param e TreeModificationEvent when expanded branch has been clicked to collapse
     */
    private void handleCollapseEvent(TreeModificationEvent <Component> e){
        /**
         * node to reference the treeItem contained in the TreeModificationEvent
         */
        TreeItem node = e.getTreeItem();
        node.getChildren().removeAll(node.getChildren());
        addDummy(node);
    }
    
    /**
     * Method removes all children a first (including dummy elements ) and then loads all children of a TreeItem
     * @param path abstract File(path) of the TreeItem
     * @param node TreeItem
     */
    public void loadFolderContent(final File path, final TreeItem node){
        node.getChildren().remove( DUMMY );
        if (path.listFiles()==null){
            return;
        }
        try{
            for ( File current_path : path.listFiles() ) {
                if ( current_path.isDirectory() ) {
                    addFolder( current_path, node );
                } else {
                    addFile( current_path, node );
                }
            }
        } catch (NullPointerException ne){
            System.out.println("Zugriffsrechte fehlen für diese Operation");
        }
    }
    /**
     * Method adds a dummy folder to a directory treeItem, thus the Folder indicates to be expandable
     * @param node the node where the dummy element should be added
     */
    public void addDummy( final TreeItem node ){
        node.getChildren().add( DUMMY );
    }
    
    /**
     * Method adds a folder to a parent TreeItem
     * @param path File path of the folder that shall be added
     * @param node parent TreeItem
     */
    public void addFolder( final File path, final TreeItem node ){
        boolean isExpandable = !(path.listFiles()==null);
        TreeItem<Folder> folder = new TreeItem<> ( new Folder(path, isExpandable ), new ImageView( FOLDER_ICON ));
        if(isExpandable){
            addDummy(folder);
        }
        node.getChildren().add(folder);
    }
    
    /**
     * Method adds a folder to a parent TreeItem
     * @param path File path of the file that shall be added
     * @param node parent TreeItem
     */
    public void addFile( final File path, final TreeItem node ){
        TreeItem<FileElement> file = new TreeItem<> ( new FileElement( path ), new ImageView( FILE_ICON ));
        node.getChildren().add(file);
    }
}