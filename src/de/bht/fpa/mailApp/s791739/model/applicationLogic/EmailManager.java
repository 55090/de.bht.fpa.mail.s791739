package de.bht.fpa.mailApp.s791739.model.applicationLogic;

import de.bht.fpa.mailApp.s791739.model.EmailManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXB;

/**
 * Class manages the Emails of a specific folder
 * @author Marco Kollosche, András Bucsi (FPA Strippgen) Gruppe 4
 * @version Aufgabe 7
 */
public class EmailManager implements EmailManagerIF {
    private Folder currentFolder;
    /**
     * Constructor of MailManager
     * @param folder
     */
    public EmailManager( final Folder folder ){
        this.currentFolder = folder;
    }

    /**
     * Method adds emails to its parent folder
     * @param folder parent folder
     */
    @Override
    public void loadEmails( final Folder folder ) {
        setCurrentFolder(folder);
        if ( !this.currentFolder.getEmails().isEmpty() ){
            return;
        }
        for ( final File filePath : new File( this.currentFolder.getPath() ).listFiles() ){
            if( filePath.getName().endsWith(".xml") ){
                this.currentFolder.addEmail( JAXB.unmarshal( filePath, Email.class ) );
            }
        }
    }
    
    /**
     * Prints Emails from a specific folder
     */
    @Override
    public void printMails( ) {
        List<Email> mails = this.currentFolder.getEmails();
        System.out.println( "Selected directory: " + this.currentFolder.getPath() );
        System.out.println( "Number of emails: "   + mails.size() );
        mails.stream().forEach( ( Email email_item ) -> {
            System.out.printf("[Email: sender=%s received=%s subject=%s] %n", email_item.getSender()   , 
                                                                              email_item.getReceived() , 
                                                                              email_item.getSubject()  );
        });
    }

    /**
     * Method marshals Email items from the TableView observableList and saves it
     * @param path chosen directory path
     */
    @Override
    public void saveEmails( final File path ) {
        List<Email> emailList = this.currentFolder.getEmails(); 
        if (!emailList.isEmpty() && path != null){
            emailList.stream().forEach((Email em) -> {
                File f = new File( path.getAbsolutePath() + "/" + em.getSent() + " " +em.getSender() + ".xml" );
                JAXB.marshal( em, f );
            });
        }
    }
    
    /**
     * Method sets current selected Folder 
     * @param folder 
     */
    @Override
    public void setCurrentFolder( final Folder folder ){
        this.currentFolder = folder;
    }
    
    /**
     * Method returns an ObservableList that contains emails matching the given pattern
     * @param pattern String with a specific charsequence / String
     * @return the List containing the matches
     */
    @Override
    public List<Email> search( final String pattern ){
        String regEx = ".*"+pattern+".*"; // concatenate RegEx
        List<Email> list = new ArrayList<>();
        this.currentFolder.getEmails().stream().filter( (Email email) -> 
                        ( email.getSubject() .toLowerCase().matches( regEx ) 
                       || email.getText()    .toLowerCase().matches( regEx ) 
                       || email.getReceived().toLowerCase().matches( regEx ) 
                       || email.getSent()    .toLowerCase().matches( regEx )
                       || email.getReceiver().toLowerCase().matches( regEx ) 
                       || email.getSender()  .toLowerCase().matches( regEx ) ))
                .forEach( (Email email) -> {
                    list.add( email ); 
                } );
        return list;
    }
}