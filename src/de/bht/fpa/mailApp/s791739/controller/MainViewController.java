package de.bht.fpa.mailApp.s791739.controller;

import de.bht.fpa.mailApp.s791739.model.applicationLogic.ApplicationLogicIF;
import de.bht.fpa.mailApp.s791739.model.applicationLogic.Facade;
import de.bht.fpa.mailApp.s791739.model.data.Account;
import de.bht.fpa.mailApp.s791739.model.data.Component;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.FileElement;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
 * @author Marco Kollosche, András Bucsi (FPA Strippgen) Gruppe 4
 * @version Aufgabe 10 2015-01-29
 */
public class MainViewController implements Initializable {
    
    // Format reference from EMail
    private static final DateFormat FORMAT = DateFormat.getDateTimeInstance( DateFormat.FULL, DateFormat.SHORT, Locale.GERMANY );
    
    // Image for the folder visualization
    private final Image FOLDER_ICON      = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/folder_Icon.png" ) );
    
    // Image for the folder visualization
    private final Image FOLDER_OPEN_ICON = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/folder_open_Icon.png" ) );
    
    // Image for the file visualization
    private final Image FILE_ICON        = new Image( getClass().getResourceAsStream( "/de/bht/fpa/mailApp/s791739/model/data/icons/file_Icon.png" ) );
    
    // String of root path
    private final static String S_DEFAULT_ROOTPATH = "/";
    
    // File for initial path
    private final static File DEFAULT_ROOTPATH     = new File( S_DEFAULT_ROOTPATH );
    
    // Dummy Element to show arrow of branch expander
    private final static TreeItem<Component> DUMMY = new TreeItem<> ( new Folder(new File( "" ), true ) );
    
    // ApplicationLogic
    private final ApplicationLogicIF facade;
    
    // Saves used directories
    private final TreeSet<File> historySet;
    
    /**
     * injections from FXMLDocument GUI
     */
    @FXML
    TreeView<Component> treeView;
    
    @FXML
    MenuBar menuBar;
    
    @FXML
    Menu    openAccount, 
            editAccount;
    
    @FXML
    MenuItem fileOpen   , 
             save       , 
             fileHistory,
             newAccount ;

    @FXML
    TextField textField_Search;

    @FXML
    Label label_ItemCount;

    @FXML
    TableView<Email> tableView;

    @FXML
    TableColumn<Email, String> importance, 
                               received  , 
                               read      , 
                               sender    , 
                               recipients, 
                               subject   ;
         
    @FXML
    Label l_sender_placeholder  , 
          l_subject_placeholder ,
          l_received_placeholder,
          l_receiver_placeholder;

    @FXML
    TextArea tA_mailBody;

    /**
     * Constructor
     */
    public MainViewController(){
        facade        = new Facade(DEFAULT_ROOTPATH);
        historySet    = new TreeSet<>();
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
        configureSearch();
    }   
    
    /**
     * Method to initially configure tree
     */
    private void configureTree(){
        setTreeRoot( DEFAULT_ROOTPATH );
        treeView.getSelectionModel().selectedItemProperty().addListener( 
                ( ObservableValue<? extends TreeItem<Component>> observable, TreeItem<Component> oldValue, TreeItem<Component> newValue ) 
                        -> handleTreeSelection(newValue) 
        );
    }
    
    /**
     * Method configures the Menu Items with event handler
     */
    private void configureMenue(){
        fileOpen   .setOnAction( ( event )-> handleMenueEvent( event ) );
        save       .setOnAction( ( event )-> handleMenueEvent( event ) );
        fileHistory.setOnAction( ( event )-> handleMenueEvent( event ) );
        newAccount .setOnAction( ( event )-> handleMenueEvent( event ) );
        save.setDisable(true); 
        loadAccountMenus();
        
    }
    
    /**
     * Method loads menu items (accounts) dynamically to account menus 
     * (open accound & edit account)
     */
    private void loadAccountMenus(){
        openAccount.getItems().clear();
        editAccount.getItems().clear();
        final List<String> accounts = facade.getAllAccounts();
        for(final String account : accounts){
            final MenuItem open, edit;
            open = new MenuItem(account);
            edit = new MenuItem(account);
            open.setUserData(facade.getAccount(account));
            edit.setUserData(facade.getAccount(account));
            open.setOnAction( ( event )-> handleMenueEvent( event ) );
            edit.setOnAction( ( event )-> handleMenueEvent( event ) );
            openAccount.getItems().add(open);
            editAccount.getItems().add(edit);
        }
    }
    /**
     * Method configures the TableView Columns
     */
    private void configureTableView(){
        importance.setCellValueFactory(new PropertyValueFactory<>("importance"));
        received.  setCellValueFactory(new PropertyValueFactory<>("received"));
        read.      setCellValueFactory(new PropertyValueFactory<>("read"));
        sender.    setCellValueFactory(new PropertyValueFactory<>("sender"));
        recipients.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        subject.   setCellValueFactory(new PropertyValueFactory<>("subject"));
        received.  setSortType(TableColumn.SortType.DESCENDING);
        //sortierung muss angepasst werden (String in Date umwandeln und dann sortieren (bzw Comparator)
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Email> observableVal, Email oldVal, Email newVal) 
                        -> handleTableViewSelection(newVal)
        );
        
        // inspired by https://community.oracle.com/thread/2407932
        received.setComparator((String t, String t1) -> {
            try {
                Date d1 = FORMAT.parse(t);
                Date d2 = FORMAT.parse(t1);
                return Long.compare(d1.getTime(),d2.getTime());
            } catch ( ParseException ex ) {
                //
            }
            return -1;
        }); 
        received.setCellValueFactory((CellDataFeatures<Email, String> param) -> new SimpleObjectProperty(param.getValue().getReceived()));
    }
    
    /**
     * Method adds EventHandler to search textfield
     */
    private void configureSearch(){
        textField_Search.textProperty().addListener((ObservableValue<? extends String> observableValue, String oldValue, String newValue) -> handleSearch(newValue));
        //textField_Search.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent e)->handleSearch(e));
        
        //Textproperty PCL
    }
    
    /**
     * Method to set root to tree
     * @param rootPath given File to set as root for tree
     */
    protected void setTreeRoot( final File rootPath ){
	facade.changeDirectory(rootPath );
        
        TreeItem<Component> rootItem = new TreeItem<> ( facade.getTopFolder(), new ImageView( FOLDER_OPEN_ICON ) ); 
	rootItem.setExpanded( true );
        
        rootItem.addEventHandler( TreeItem.<Component>branchExpandedEvent(),  ( TreeItem.TreeModificationEvent <Component> e ) -> handleExpandEvent( e ) );
        rootItem.addEventHandler( TreeItem.<Component>branchCollapsedEvent(), ( TreeItem.TreeModificationEvent <Component> e ) -> handleCollapseEvent( e ) );
        
	treeView.setRoot( rootItem );
	loadTreeItemContents( rootItem );
    }

    /**
     * Method handles the events coming from the menu items
     * @param e event source
     */
    private void handleMenueEvent( final Event e ){
        File path;
        final MenuItem item = (MenuItem)e.getSource();
        String id = item.getId();
        if(id == null){
            id = item.getParentMenu().getId();
        } 
        switch( id ){
            case "FileOpen":
                            path = openDirectoryChooser("Select new Base Directory!"); 
                            if ( path == null ){
                                System.err.println("No directory chosen!");    
                                break;
                            } else{
                                setTreeRoot( path );
                                if ( path != DEFAULT_ROOTPATH ){
                                    historySet.add( path );
                                }
                            }  
                            break;
            case "Save": 
                            path = openDirectoryChooser("Select Directory to save emails!"); 
                            if ( path == null ){
                                System.err.println("No directory chosen!");    
                                break;
                            } else{
                                facade.saveEmails(path);
                            }  
                            break;
            case "FileHistory": 
                            showHistoryView(); 
                            break;
                
            case "NewAccount": 
                            showCreateAccountView(null);
                            break;
            case "OpenAccount":
                            facade.openAccount(item.getText());
                            setTreeRoot(new File(facade.getAccount(item.getText()).getTop().getPath()));
                            break;
            case "EditAccount": 
                            showCreateAccountView(facade.getAccount(item.getText()));
                            break;
            default:
                            break;
        }
    }
    
    /**
     * Method saves newly created account
     * @param account new account
     */
    protected void saveAccount( final Account account ){
        facade.saveAccount(account);
        loadAccountMenus();
    }
    
    /**
     * Method returns result of comparison with all account names with the 
     * given name. If the name matches an existing account name then it 
     * returns true, else false.
     * @param name name of name creation
     * @return result of check (true = taken // false = available)
     */
    protected boolean accountNameTaken( final String name ){
        boolean result = false;
        for (final String accName : facade.getAllAccounts()){
            if(name.equals(accName)){
                return true;
            }
        }
        return result;
    }
    
    
    /**
     * Method updates existing account
     * @param account updated account
     */
    protected void updateAccount( final Account account ){
        this.facade.updateAccount(account);
        loadAccountMenus();
    }
    
    /**
     * Method opens a Window to chose directory
     * @returns a File with the new chosen directory (or null if no choice)
     */
    private File openDirectoryChooser(String title){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle( title );
        return dc.showDialog( null );
    }
    
    /**
     * Method is called when an treeItem was selected
     * @param item treeItem that was selected
     */
    private void handleTreeSelection(final TreeItem<Component> item){
        handleEmailEvent(item);
        clearContentView();
        treeView.setShowRoot(false);
        treeView.setShowRoot(true);
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
            final Folder folder = (Folder) node.getValue();
            facade.loadEmails( folder );
            if (folder.getEmails()!=null){
                tableView.setItems(FXCollections.observableArrayList(folder.getEmails()));
                setCurrentEmailSizeToLabel();
                save.setDisable(false);
            } else{
                save.setDisable(true);
            }
        }
    }
   
    /**
     * Method handles Events triggered by key release in search field
     * obtains String from EventSource and matches it with the attributes of the Email-objects
     * contained in the current ObservableList<Email> of the TableView
     * @param e Key Event (Javafx Scene) KeyEvent.KeyReleased
     */
    private void handleSearch(final String searchText) {
        String s = searchText.toLowerCase();
        tableView.setItems(FXCollections.observableArrayList(facade.search(s)));
        setCurrentEmailSizeToLabel();
    }
    
    /**
     * Method counts contained TableView items (Emails) and sets it to label
     */
    private void setCurrentEmailSizeToLabel() {
        label_ItemCount.setText("("+tableView.getItems().size()+")");
    }
    
    /**
     * Method shows Emails in a content window when a specific list item was selected 
     * @param newValue 
     */
    private void handleTableViewSelection( final Email mailItem ) {
        if(mailItem!=null){
            l_sender_placeholder  .setText(mailItem.getSender());
            l_subject_placeholder .setText(mailItem.getSubject());
            l_received_placeholder.setText(mailItem.getReceived());
            l_receiver_placeholder.setText(mailItem.getReceiver());
            tA_mailBody           .setText(mailItem.getText());
        }
    }
    
    /**
     * Method clears all fields of the eMail content view by setting them with an empty String or "(no item selected)"
     */
    private void clearContentView(){
        l_sender_placeholder  .setText("");
        l_subject_placeholder .setText("");
        l_received_placeholder.setText("");
        l_receiver_placeholder.setText("");
        tA_mailBody           .setText("(no item selected)");
        menuBar.getMenus().stream().forEach( ( Menu menu )-> { 
                menu.getItems().stream().forEach( ( MenuItem item )-> {
                    if(item.getId().equals("save")){
                        item.setDisable(true);
                    }
            });
        });
        
    }
    
    /**
     * Method removes all children a first (including dummy elements ) and then loads all children of a TreeItem
     * @param node TreeItem
     */
    private void loadTreeItemContents( final TreeItem<Component> node ){
        Folder folder = (Folder)node.getValue();
        node.getChildren().remove( DUMMY );
        facade.loadContent( folder );
        
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
        Stage historyStage = new Stage( StageStyle.UTILITY );
        historyStage.setTitle( "Select Base Directory" );
        URL location = getClass().getResource( "/de/bht/fpa/mailApp/s791739/view/FXMLHistory.fxml" );

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation( location );
        fxmlLoader.setController( new HistoryController( this ) );
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene( myPane );
            historyStage.setScene( myScene );
            historyStage.show();
        } catch ( IOException ex ) {
            Logger.getLogger(MainViewController.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }
    
    /**
     * Method configures a view that shows all fields that are required to setup an account
     * either to create a new account (if the parameter account is null) or to edit (if it contains an actual account)
     * @param account the referenced account
     */
    private void showCreateAccountView(final Account account) {
            Stage createAccountStage = new Stage(StageStyle.UTILITY);
            URL location = getClass().getResource("/de/bht/fpa/mailApp/s791739/view/FXMLCreateAccount.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            if(account==null){
                createAccountStage.setTitle( "New Account" );
                fxmlLoader.setController(new CreateAccountController(this));
            } else {
                createAccountStage.setTitle( "Update Account" );
                fxmlLoader.setController(new CreateAccountController(this, account));
            }
            try {
                Pane myPane = (Pane) fxmlLoader.load();
                Scene myScene = new Scene(myPane);
                createAccountStage.setScene(myScene);
                createAccountStage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
//            }
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