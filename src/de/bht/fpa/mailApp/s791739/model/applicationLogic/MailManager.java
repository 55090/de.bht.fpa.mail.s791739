package de.bht.fpa.mailApp.s791739.model.applicationLogic;

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
public class MailManager implements EmailManagerIF {
    /**
     * Constructor of MailManager
     */
    public MailManager(){
    }

    /**
     * Method adds emails to its parent folder
     * @param folder parent folder
     * @return folder with added emails
     */
    @Override
    public void loadMails( final Folder folder ) {
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
     * Method generates an ObservableList of all eMails contained in the passed Folder
     * @param folder current Folder
     * @return ObservableList of all eMails
     */
    @Override
    public ObservableList<Email> listMails( final Folder folder) {
        System.out.println("list A");
        ObservableList<Email> ol = FXCollections.observableArrayList();
        final List<Email> mails = folder.getEmails();
        mails.stream().forEach( ( final Email email_item ) -> { ol.add(email_item); });
        return ol;
    }
}
