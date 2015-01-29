package de.bht.fpa.mailApp.s791739.model.applicationLogic.imap;

import de.bht.fpa.mailApp.s791739.model.applicationLogic.EmailManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Account;
import de.bht.fpa.mailApp.s791739.model.data.Email;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.xml.bind.JAXB;


public class IMapEmailManager implements EmailManagerIF{

    private final Account account;
    private Folder currentFolder;
    
    public IMapEmailManager( final Account account){
        this.account = account;
    }
    
    
    
    @Override
    public void loadEmails(Folder folder) {
        try{
            setCurrentFolder(folder);
            if ( !this.currentFolder.getEmails().isEmpty() ){
                final javax.mail.Folder dir = IMapConnectionHelper.connect(account).getFolder(folder.getPath());

                if (dir.exists()) {
                    int dirType = dir.getType();
                        // 1 contains messages / 3 contain messages and folders

                    if ( dirType == 1 || dirType == 3 && dir.exists() ) {
                        dir.open( javax.mail.Folder.READ_ONLY );
                        for ( final Message m : dir.getMessages() ) {
                            Email message2emailConverted = IMapEmailConverter.convertMessage( m );
                            folder.addEmail( message2emailConverted );
                        }
                        dir.close(true);
                    }
                }
                dir.getStore().close();
            }
        } catch (Exception ex) {
            Logger.getLogger(IMapEmailManager.class.getName()).log(Level.SEVERE, null, ex);
                }       
    }

    @Override
    public void printMails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

    @Override
    public void setCurrentFolder( final Folder folder ) {
        this.currentFolder = folder;
    }

    @Override
    public List<Email> search( final String pattern ) {
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
