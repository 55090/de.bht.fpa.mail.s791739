package de.bht.fpa.mailApp.s791739.model.applicationLogic.imap;

import de.bht.fpa.mailApp.s791739.model.applicationLogic.FolderManagerIF;
import de.bht.fpa.mailApp.s791739.model.data.Account;
import de.bht.fpa.mailApp.s791739.model.data.Component;
import de.bht.fpa.mailApp.s791739.model.data.Folder;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * Controller Class for FXMLDocument
 * @author Marco Kollosche, András Bucsi (FPA Strippgen) Gruppe 4
 * @version Aufgabe 10 2015-01-29
 */
public class IMapFolderManager implements FolderManagerIF{

    private Folder topFolder;

    private final Account account;
    
    /**
     * Constructor
     * @param account current Account
     */
    public IMapFolderManager( final Account account){
        final Store store = IMapConnectionHelper.connect(account);
        this.account   = account;
        this.topFolder = new Folder(new File(account.getTop().getName()), true);
        try {
            final javax.mail.Folder top = store.getDefaultFolder();
            this.topFolder.setPath(top.getFullName());
            store.close();
        } catch(MessagingException me){
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, me);
            this.topFolder = new Folder(new File(account.getName()), false);
        }
    }
    
    @Override
    public Folder getTopFolder() {
        return this.topFolder;
    }

    @Override
    public void setTopFolder(final File file) {
        // unused here
    }

    /**
     * Method loads content of each folder
     * @param folder 
     */
    @Override
    public void loadContent( final Folder folder ) {        
        try {
            if (folder.getComponents().isEmpty()) {
                Store store = IMapConnectionHelper.connect(account);

                if (store != null) {
                    final javax.mail.Folder childFolder = store.getFolder(folder.getPath());
                    if (childFolder.exists()) {
                        for (javax.mail.Folder subDirectory : childFolder.list()) {
                            if (subDirectory.list().length == 0) {
                                Folder dir = new Folder(new File(subDirectory.getName()), false);
                                dir.setPath(subDirectory.getFullName());
                                folder.addComponent(dir);
                            } else {
                                Folder dir = new Folder(new File(subDirectory.getName()), true);
                                dir.setPath(subDirectory.getFullName());

                                if (subDirectory.getName().equals("[Gmail]")) {
                                    loadContent(dir);
                                    for (Component subFolder : dir.getComponents()) {
                                        folder.addComponent(subFolder);
                                    }
                                    continue;
                                }
                                folder.addComponent(dir);
                            }
                        }
                    }
                    store.close();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
}
