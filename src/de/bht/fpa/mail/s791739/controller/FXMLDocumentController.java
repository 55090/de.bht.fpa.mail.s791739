package de.bht.fpa.mail.s791739.controller;

import de.bht.fpa.mail.s791739.model.appLogic.FileElement;
import de.bht.fpa.mail.s791739.model.appLogic.Component;
import de.bht.fpa.mail.s791739.model.appLogic.Folder;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class FXMLDocumentController implements Initializable {
  
    /**
     * Image for the folders
     */
    private final Image FOLDER_ICON 
            = new Image(getClass().getResourceAsStream( "/de/bht/fpa/mail/s791739/model/data/icons/Folder-Generic.png"));
    
    /**
     * Image for the files
     */
    private final Image FILE_ICON 
            = new Image(getClass().getResourceAsStream( "/de/bht/fpa/mail/s791739/model/data/icons/file.png"));
    
    /**
     * File for the initial rootpath
     */
    private final File ROOTPATH = new File("/");
   
    /**
     * Dummy Element to show arrow of branch expander
     */
    private final TreeItem<Folder> DUMMY = new TreeItem<> ( new Folder(new File(""), true ) );
    
    @FXML
    private Label label;
    
    /**
     * injection from FXML Document GUI
     */
    @FXML
    TreeView tv;
    /**
     * Standard constructor
     */
    public FXMLDocumentController(){
    }
    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
        //tv.setOnAction((e)-> System.out.println("Start"));
    }
    /**
     * method to configure the tree
     */
    public void configureTree() {
        
        TreeItem<Component> treeItemRoot = new TreeItem<>(new Folder(ROOTPATH, true), new ImageView(FOLDER_ICON));
        treeItemRoot.setExpanded(true);
        treeItemRoot.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent <Component> e) -> handleExpandEvent(e) );
        
        treeItemRoot.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent <Component> e) -> handleCollapseEvent(e) );
        tv.setRoot(treeItemRoot);
        loadContent(treeItemRoot);
    }
    //*
    public void handleExpandEvent(TreeModificationEvent <Component> e){
        loadContent(e.getTreeItem());        
    }
    
    public void handleCollapseEvent (TreeModificationEvent <Component> e){
        TreeItem ti=e.getTreeItem();
        ti.getChildren().removeAll(ti.getChildren());
        addDummy(ti);
    }
    
    public void loadContent(final TreeItem treeItem) {
        treeItem.getChildren().remove(DUMMY);
        File file = new File(((TreeItem<Component>)treeItem).getValue().getPath());
       
        try{
            for (File f : file.listFiles()) {

                if (f.isDirectory()) {
                    boolean isExpandable=f.listFiles()!=null;
                            
                    TreeItem<Folder> folderComp = new TreeItem<>(new Folder(f.getAbsoluteFile(), isExpandable ), new ImageView(FOLDER_ICON));
                    if(isExpandable){
                        addDummy(folderComp);
                    }
                    treeItem.getChildren().add(folderComp);
                } else {
                    TreeItem<FileElement> fileComp = new TreeItem<>(new FileElement(f.getAbsoluteFile()), new ImageView(FILE_ICON));
                    treeItem.getChildren().add(fileComp);
                }
             }
        } catch(NullPointerException npe){
            System.out.println("Sie haben nicht die entsprechenden Rechte");
        }
        
    }
    public void addDummy(TreeItem ti){
        ti.getChildren().add(DUMMY);
    }
    @FXML
    public void handleMouseClicked(MouseEvent e){
        TreeItem<Component> node = ((TreeView)(e.getSource())).getRoot();
        loadContent(node);
        System.out.println("Hello");
    }
    
 
}
