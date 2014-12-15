package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.EmailManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.JAXB;

/**
 *
 * @author Me
 */
public class XMLEmailManager implements EmailManagerIF {
    public  final ObservableList<Email> emailList;
    private final ObservableList<Email> searchList;
    private Email selectedItem;
    
    /**
     * Constructor of MailManager
     * @param folder
     */
    public XMLEmailManager( final Folder folder ){
        this.emailList    = FXCollections.observableArrayList();
        this.emailList.addAll(folder.getEmails());
        this.searchList   = FXCollections.observableArrayList();
        this.emailList.addAll(folder.getEmails());
        this.selectedItem = null;
    }

    /**
     * Method adds emails to its parent folder
     * @param folder parent folder
     */
    @Override
    public void loadEmails( final Folder folder ) {
        if ( !folder.getEmails().isEmpty() ){
            return;
        }
        for ( final File filePath : new File( folder.getPath() ).listFiles() ){
            if( filePath.getName().endsWith(".xml") ){
                folder.addEmail( JAXB.unmarshal( filePath, Email.class ) );
            }
        }
    }
    
    /**
     * Prints Emails of a specific folder
     * @param folder folder containing emails
     */
    @Override
    public void printMails( final Folder folder ) {
        List<Email> mails = folder.getEmails();
        System.out.println( "Selected directory: " + folder.getPath() );
        System.out.println( "Number of emails: "   + mails.size() );
        mails.stream().forEach( ( Email email_item ) -> {
            System.out.printf("[Email: sender=%s received=%s subject=%s] %n", email_item.getSender()   , 
                                                                              email_item.getReceived() , 
                                                                              email_item.getSubject()  );
        });
    }

    /**
     * Method replaces searchList with current search results
     * @param pattern 
     */
    @Override
    public void updateSearchList(final String pattern) {
        String s = ".*"+pattern+".*"; // RegEx
        this.searchList.clear();
        this.emailList.stream().filter((email) -> ( email.getSubject() .toLowerCase().matches(s) 
                                                 || email.getText()    .toLowerCase().matches(s) 
                                                 || email.getReceived().toLowerCase().matches(s) 
                                                 || email.getSent()    .toLowerCase().matches(s)
                                                 || email.getReceiver().toLowerCase().matches(s) 
                                                 || email.getSender()  .toLowerCase().matches(s) ))
                .forEach((email) -> {this.searchList.add(email);});
    }

    /**
     * Method list adds emails to an observable List
     * @param folder current folder
     */
    @Override
    public void updateEmailList( final Folder folder ) {
        this.emailList.clear();
        this.emailList.addAll(folder.getEmails());
    }    

    /**
     * Method returns the current observable list of all emails
     * @return an observable list containing all emails of that current folder
     */
    @Override
    public ObservableList<Email> getEmailList() {
        return this.emailList;
    }

    /**
     * Method returns the current observable list of all emails that have matched the search pattern
     * @return an observable list containing all emails matching that search pattern
     */
    @Override
    public ObservableList<Email> getSearchList() {
        return this.searchList;
    }

    /**
     * Method marshals Email items from the TableView observableList and saves it
     * @param path chosen directory path
     */
    @Override
    public void saveEmails( final File path ) {
        if( path != null ){
            File f = new File( path.getAbsolutePath() + "/" + "email" + this.selectedItem.hashCode() + ".xml" );
            JAXB.marshal( this.selectedItem, f );
        }
    }

    /**
     * Method receives an item from the TableView ObservableList and sets it to this Manager
     * @param selectedItem the selected item to be set
     */
    @Override
    public void setSelectedTableViewItem(Email selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * Method returns the currently set selected Email item
     * @return the selected item
     */
    @Override
    public Email getSelectedTableViewItem() {
        return this.selectedItem;
    }
}
